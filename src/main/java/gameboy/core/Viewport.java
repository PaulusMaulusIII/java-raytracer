package gameboy.core;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import gameboy.utilities.Camera;
import gameboy.utilities.Scene;
import gameboy.utilities.math.Vector3;

public class Viewport extends JPanel {

	protected Renderer renderer;
	protected BufferedImage frame;
	protected Vector3 deltaCamera = new Vector3(0, 0, 0);
	protected double resolution = 0.5F;
	protected boolean captureCursor = true;
	protected double cameraYaw;
	protected double cameraPitch;
	protected Robot robot;
	protected Scene scene;
	protected BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	protected Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0),
			"blank cursor");
	protected Container container;
	protected JDialog settingsDialog;

	public Viewport(Container container, SettingPanel settings) {
		this.container = container;
		this.scene = settings.getScene();
		setSize(container.getWidth(), container.getHeight());
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
						renderer.renderToImage(scene, 3840, 2160);
					} catch (IOException ex) {
						ex.printStackTrace();
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
						int i = JOptionPane.showConfirmDialog(null, "Beenden?");
						if (i == 0)
							System.exit(0);
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

		Camera cam = scene.getCurrentCamera();
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
			renderer = new Renderer(scene, getWidth(), getHeight());
			Camera cam = scene.getCurrentCamera();
			if (captureCursor) {
				cam.setYaw(cameraYaw);
				cam.setPitch(cameraPitch);
				cam.translate(deltaCamera.rotate(cam.getPitch(), cam.getYaw()));
			}
			frame = renderer.render(resolution);
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(frame, 0, 0, this);
	}
}