package gameboy.core;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import gameboy.utilities.Camera;
import gameboy.utilities.Scene;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class Viewport extends JPanel {

	private BufferedImage frame;
	private Vector3 deltaCamera = new Vector3(0, 0, 0);
	private double resolution = .125F;
	private boolean captureCursor = true;
	private double cameraYaw;
	private double cameraPitch;
	private Robot robot;
	private Scene scene;
	private BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	private Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0),
			"blank cursor");
	private Container container;
	private int hud = 2;
	private boolean dof = false;
	private double distance = 0.1;
	private boolean autoDOF = true;

	public Viewport(Container container, SettingPanel settings, JDialog settingsDialog) {
		this.container = container;
		this.scene = settings.getScene();
		setSize(container.getWidth() - settings.getWidth(), container.getHeight());
		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_D:
					deltaCamera.setX(0.2F);
					break;
				case KeyEvent.VK_A:
					deltaCamera.setX(-0.2F);
					break;
				case KeyEvent.VK_W:
					deltaCamera.setZ(0.2F);
					break;
				case KeyEvent.VK_S:
					deltaCamera.setZ(-0.2F);
					break;
				case KeyEvent.VK_SPACE:
					deltaCamera.setY(0.2F);
					break;
				case KeyEvent.VK_SHIFT:
					deltaCamera.setY(-0.2F);
					break;
				case KeyEvent.VK_1:
					resolution = 1;
					break;
				case KeyEvent.VK_2:
					resolution = 0.5F;
					break;
				case KeyEvent.VK_3:
					resolution = 0.25F;
					break;
				case KeyEvent.VK_4:
					resolution = 0.125F;
					break;
				case KeyEvent.VK_F12:
					try {
						Renderer.renderToImage(scene, 3840, 2160, dof, distance);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					break;
				case KeyEvent.VK_H:
					if (hud < 3)
						hud++;
					else
						hud = 0;
					break;
				case KeyEvent.VK_F:
					dof = !dof;
					break;
				case KeyEvent.VK_PLUS:
					distance += 0.1;
					break;
				case KeyEvent.VK_MINUS:
					distance -= 0.1;
					if (distance < 0.1)
						distance = 0;
					break;
				case KeyEvent.VK_R:
					autoDOF = !autoDOF;
					break;

				default:
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_D) {
					deltaCamera.setX(0);
				}
				else if (e.getKeyCode() == KeyEvent.VK_A) {
					deltaCamera.setX(0);
				}
				else if (e.getKeyCode() == KeyEvent.VK_W) {
					deltaCamera.setZ(0);
				}
				else if (e.getKeyCode() == KeyEvent.VK_S) {
					deltaCamera.setZ(0);
				}
				else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					deltaCamera.setY(0);
				}
				else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					deltaCamera.setY(0);
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (captureCursor) {
						setCaptureCursor(false);
					}
					else {
						if (settingsDialog.isVisible()) {
							int i = JOptionPane.showConfirmDialog(null, "Beenden?");
							if (i == 0)
								System.exit(0);
						}
						else {
							settingsDialog.setVisible(true);
						}
					}
				}
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (captureCursor) {
					int centerX = container.getX() + container.getWidth() / 2;
					int centerY = container.getY() + container.getHeight() / 2;

					int mouseXOffset = e.getXOnScreen() - centerX;
					int mouseYOffset = e.getYOnScreen() - centerY;
					cameraYaw = (cameraYaw + mouseXOffset * 0.001);
					cameraPitch = (Math.min(90, Math.max(-90, cameraPitch + mouseYOffset * 0.001)));
					robot.mouseMove(centerX, centerY);
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!captureCursor)
					setCaptureCursor(true);
			}
		});

		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Camera cam = scene.getCamera();
		this.cameraYaw = cam.getYaw();
		this.cameraPitch = cam.getPitch();
		setCaptureCursor(true);
	}

	private void setCaptureCursor(boolean captureCursor) {
		this.captureCursor = captureCursor;

		if (captureCursor) {
			setCursor(blankCursor);
			int centerX = container.getX() + container.getWidth() / 2;
			int centerY = container.getY() + container.getHeight() / 2;
			robot.mouseMove(centerX, centerY);
		}
		else {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	public void run() {
		while (true) {
			long startTime = System.currentTimeMillis();
			Camera cam = scene.getCamera();
			if (captureCursor) {
				cam.setYaw(cameraYaw);
				cam.setPitch(cameraPitch);
				cam.translate(deltaCamera.rotate(cam.getPitch(), cam.getYaw()));
			}
			RayHit lookingAt = Renderer.getLookingAt(scene, container.getWidth(), container.getHeight());
			if (autoDOF)
				if (lookingAt != null)
					distance = scene.getCamera().getAnchor().distance(lookingAt.getHitPoint());

			frame = Renderer.render(scene, container.getWidth(), container.getHeight(), resolution, false, dof,
					distance);

			if (hud > 0) {
				frame.getGraphics().drawString("+", container.getWidth() / 2, container.getHeight() / 2);

				if (hud > 1) {
					frame.getGraphics().drawString("CameraPos: " + scene.getCamera().getAnchor(), 10, 20);
					if (lookingAt != null) {
						frame.getGraphics().drawString(
								"Looking at: " + lookingAt.getHitPoint() + ", " + lookingAt.getShape() + ", "
										+ lookingAt.getShape().getMaterial().getShader().shade(lookingAt,
												scene.getLights(), scene.getObjects(),
												lookingAt.getShape().getMaterial()),
								10, 40);
					}

					if (hud > 2) {
						frame.getGraphics().drawLine(0, container.getHeight() / 3, container.getWidth(),
								container.getHeight() / 3);
						frame.getGraphics().drawLine(0, (container.getHeight() / 3) * 2, container.getWidth(),
								(container.getHeight() / 3) * 2);

						frame.getGraphics().drawLine(container.getWidth() / 3, 0, container.getWidth() / 3,
								container.getHeight());
						frame.getGraphics().drawLine((container.getWidth() / 3) * 2, 0, (container.getWidth() / 3) * 2,
								container.getHeight());
					}
				}

				long deltaTime = System.currentTimeMillis() - startTime;
				frame.getGraphics()
						.drawString(
								"FPS: " + 1000 / (deltaTime + 1) + " @ " + (int) (container.getWidth() * resolution)
										+ "x" + (int) (container.getHeight() * resolution),
								container.getWidth() - 150, 20);
			}

			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(frame, 0, 0, this);
	}
}