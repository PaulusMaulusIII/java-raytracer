package gameboy.core;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import gameboy.utilities.Camera;
import gameboy.utilities.Scene;
import gameboy.utilities.data.PixelData;
import gameboy.utilities.math.Vector3;

public class Viewport extends JPanel {

	private BufferedImage frame;
	private Vector3 deltaCamera = new Vector3(0, 0, 0);
	private double resolution = 0.5F;
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

	public Viewport(Container container, SettingPanel settings, JDialog settingsDialog) {
		this.container = container;
		this.scene = settings.getScene();
		setSize(container.getWidth() - settings.getWidth(), container.getHeight());
		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_D) {
					deltaCamera.setX(0.2F);
				}
				else if (e.getKeyCode() == KeyEvent.VK_A) {
					deltaCamera.setX(-0.2F);
				}
				else if (e.getKeyCode() == KeyEvent.VK_W) {
					deltaCamera.setZ(0.2F);
				}
				else if (e.getKeyCode() == KeyEvent.VK_S) {
					deltaCamera.setZ(-0.2F);
				}
				else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					deltaCamera.setY(0.2F);
				}
				else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					deltaCamera.setY(-0.2F);
				}
				else if (e.getKeyCode() == KeyEvent.VK_1) {
					resolution = 1;
				}
				else if (e.getKeyCode() == KeyEvent.VK_2) {
					resolution = 0.5F;
				}
				else if (e.getKeyCode() == KeyEvent.VK_3) {
					resolution = 0.25F;
				}
				else if (e.getKeyCode() == KeyEvent.VK_4) {
					resolution = 0.125F;
				}
				else if (e.getKeyCode() == KeyEvent.VK_F12 || e.getKeyCode() == KeyEvent.VK_PRINTSCREEN
						|| e.getKeyCode() == KeyEvent.VK_F2) {
					try {
						Renderer.renderToImage(scene, 3840 * 2, 2160 * 2);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_H) {
					if (hud < 3) {
						hud++;
					}
					else {
						hud = 0;
					}
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
			frame = Renderer.render(scene, container.getWidth(), container.getHeight(), resolution, false);

			if (hud > 0) {
				frame.getGraphics().drawString("+", container.getWidth() / 2, container.getHeight() / 2);

				if (hud > 1) {
					frame.getGraphics().drawString("CameraPos: " + scene.getCamera().getAnchor(), 10, 20);
					PixelData lookingAt = Renderer.getLookingAt(scene, container.getWidth(), container.getHeight());
					if (lookingAt != null) {
						frame.getGraphics().drawString(
								"Looking at: " + lookingAt.getHit().getHitPoint() + ", " + lookingAt.getShape() + ", "
										+ lookingAt.getMaterial().getShader().shade(lookingAt.getHit(),
												scene.getLights(), scene.getObjects(), lookingAt.getMaterial()),
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
				frame.getGraphics().drawString("FPS: " + 1000 / deltaTime, container.getWidth() - 75, 20);
			}

			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(frame, 0, 0, this);
	}
}