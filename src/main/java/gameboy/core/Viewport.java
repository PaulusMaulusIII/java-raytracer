package gameboy.core;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.IOException;

import gameboy.utilities.Scene;
import gameboy.utilities.math.Vector3;

public class Viewport extends JPanel {

	Renderer renderer;
	BufferedImage frame;
	Vector3 deltaCamera = new Vector3(0, 0, 0);
	double resolution = 0.5F;
	boolean captureCursor = true;
	protected double cameraYaw;
	protected double cameraPitch;
	protected Robot robot;
	Scene scene;

	public Viewport(int width, int height, String code) {
		scene = new Interpreter().interpret(code);
		setName("Renderer");
		setSize(width, height);
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
				else if (e.getKeyCode() == KeyEvent.VK_F12) {
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
			}
		});
	}

	public void run() {
		while (true) {
			renderer = new Renderer(scene, getWidth(), getHeight());
			scene.getCurrentCamera().setPosition(scene.getCurrentCamera().getPosition().add(deltaCamera));
			frame = renderer.render(resolution);
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(frame, 0, 0, this);
	}
}