package com.paulusmaulus.editor.core.swing_assets;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import com.paulusmaulus.editor.core.Renderer;
import com.paulusmaulus.editor.core.interfaces.Effect;
import com.paulusmaulus.editor.core.swing_assets.additional.CreateObjectDialog;
import com.paulusmaulus.editor.geometries.additional.Arrow;
import com.paulusmaulus.editor.lights.Light;
import com.paulusmaulus.editor.post_processing.DepthOfField;
import com.paulusmaulus.editor.utilities.Camera;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Object3D;
import com.paulusmaulus.editor.utilities.Scene;
import com.paulusmaulus.editor.utilities.Shape;
import com.paulusmaulus.editor.utilities.math.RayHit;
import com.paulusmaulus.editor.utilities.math.Vector2;
import com.paulusmaulus.editor.utilities.math.Vector3;

public class Viewport extends JPanel {

	// Fields
	private boolean autoDOF = true;
	private boolean ctrl = false;
	private Vector3 deltaCamera = new Vector3(0, 0, 0);
	private Vector2 deltaPY = new Vector2(0, 0);
	private double tilt = 0;
	private double distance = 0.1;
	private Effect dof = new DepthOfField(() -> distance);
	private List<Effect> effects = new LinkedList<>();
	private boolean captureCursor = false;
	private int hud = 2;
	private BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	private double resolution = 0.125;
	private double speed = 0;
	private Vector3 axis = new Vector3(0, 0, 0);

	private Cursor blankCursor;
	private Container container;
	private BufferedImage frame;
	private int origX;
	private int origY;
	private Robot robot;
	private Scene scene;
	private SettingPanel settings;
	private JDialog settingsDialog;
	private Object3D selectedObject;

	// Constructors
	public Viewport(Container container, SettingPanel settings, JDialog settingsDialog, int width, int height) {
		this.container = container;
		this.scene = settings.getScene();
		this.settings = settings;
		this.settingsDialog = settingsDialog;
		setSize(width, height);
		initialize();
	}

	// Initialization methods
	private void initialize() {
		Camera cam = scene.getCamera();
		deltaPY.x = cam.getPitch();
		deltaPY.y = cam.getYaw();
		tilt = cam.getTilt();
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}

		setSize(getWidth() - settings.getWidth(), getHeight());
		addKeyListener(keyAdapter);
		addMouseListener(mouseClickAdapter);
		addMouseMotionListener(mouseMoveAdapter);
		addMouseWheelListener(mouseWheelListener);
		setFocusable(true);
		setEnabled(true);
		setCaptureCursor(false);
	}

	// Main loop
	public void run() {
		while (true) {
			long startTime = System.currentTimeMillis();
			Camera cam = scene.getCamera();
			applyMovement(cam);
			RayHit lookingAt = Renderer.getLookingAt(scene, getWidth(), getHeight());
			if (autoDOF && lookingAt != null)
				distance = cam.getAnchor().distance(lookingAt.getHitPoint());
			frame = renderFrame();
			displayHUD(cam, lookingAt, startTime);
			repaint();
		}
	} // Render the final image

	private BufferedImage renderFrame() {
		BufferedImage renderedFrame = Renderer.render(scene, getWidth(), getHeight(), resolution, false, effects,
				distance, true);
		return renderedFrame;
	}

	// Display HUD and other overlays
	private void displayHUD(Camera cam, RayHit lookingAt, long startTime) {
		if (hud > 0) {
			Graphics gfx = frame.getGraphics();
			gfx.setColor(Color.BLACK.toAWT());
			gfx.drawString("+", getWidth() / 2, getHeight() / 2);
			if (hud > 1) {
				gfx.drawString(
						"CameraPos: " + cam.getAnchor() + ", " + Math.toDegrees(cam.getPitch()) + "°, "
								+ Math.toDegrees(cam.getYaw()) + "°, " + Math.toDegrees(cam.getTilt()) + "°, " + speed,
						10, 20);
				if (lookingAt != null) {
					gfx.drawString(
							"Looking at: " + lookingAt.getHitPoint() + ", " + lookingAt.getObject().getName() + ", "
									+ lookingAt.getShape().getMaterial().getShader().shade(lookingAt, scene,
											lookingAt.getShape().getMaterial(), 0)
									+ ", " + lookingAt.getHitPoint().distance(cam.getAnchor()),
							10, 40);
					gfx.drawString("Normal: " + lookingAt.getShape().getNormal(lookingAt.getHitPoint()), 10, 60);
				}
				if (hud > 2) {
					gfx.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3);
					gfx.drawLine(0, (getHeight() / 3) * 2, getWidth(), (getHeight() / 3) * 2);
					gfx.drawLine(getWidth() / 3, 0, getWidth() / 3, getHeight());
					gfx.drawLine((getWidth() / 3) * 2, 0, (getWidth() / 3) * 2, getHeight());
				}
			}
			long deltaTime = System.currentTimeMillis() - startTime;
			gfx.drawString("FPS: " + 1000 / (deltaTime + 1) + " @ " + (int) (getWidth() * resolution) + "x"
					+ (int) (getHeight() * resolution), getWidth() - 150, 20);
		}
	}

	// Apply camera movement based on input
	private void applyMovement(Camera cam) {
		deltaCamera.z = (Math.abs(deltaCamera.z) > Math.abs(speed)) ? deltaCamera.z : speed;
		if (captureCursor) {
			cam.setPitch(deltaPY.x);
			cam.setYaw(deltaPY.y);
			cam.setTilt(cam.getTilt() + tilt);

			// Apply the camera translation considering the tilt
		}
		Vector3 rotatedDeltaCamera = deltaCamera.rotate(cam.getPitch(), cam.getYaw(), cam.getTilt());
		cam.translate(rotatedDeltaCamera);
	}

	private void setCaptureCursor(boolean captureCursor) {
		this.captureCursor = captureCursor;
		if (captureCursor) {
			setCursor(blankCursor);
			int centerX = container.getX() + getWidth() / 2;
			int centerY = container.getY() + getHeight() / 2;
			robot.mouseMove(centerX, centerY);
		} else {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(frame, 0, 0, this);
	}// Key adapter for handling keyboard input

	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			handleKeyPress(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			handleKeyRelease(e);
		}
	};

	private void handleKeyPress(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_D -> deltaCamera.setX((ctrl) ? 2 : 0.2);
			case KeyEvent.VK_A -> deltaCamera.setX((ctrl) ? -2 : -0.2);
			case KeyEvent.VK_W -> deltaCamera.setZ((ctrl) ? 2 : 0.2);
			case KeyEvent.VK_S -> deltaCamera.setZ((ctrl) ? -2 : -0.2);
			case KeyEvent.VK_SPACE -> deltaCamera.setY((ctrl) ? 2 : 0.2);
			case KeyEvent.VK_SHIFT -> deltaCamera.setY((ctrl) ? -2 : -0.2);
			case KeyEvent.VK_1 -> resolution = 1;
			case KeyEvent.VK_2 -> resolution = 0.5;
			case KeyEvent.VK_3 -> resolution = 0.25;
			case KeyEvent.VK_4 -> resolution = 0.125;
			case KeyEvent.VK_5 -> resolution = 0.0625;
			case KeyEvent.VK_6 -> resolution = 0.03125;
			case KeyEvent.VK_7 -> resolution = 0.015625;
			case KeyEvent.VK_Q -> tilt = Math.toRadians(1);
			case KeyEvent.VK_E -> tilt = -Math.toRadians(1);
			case KeyEvent.VK_V -> scene.getCamera().setAnchor(new Vector3(0, 0, 0));
			case KeyEvent.VK_F12 -> {
				try {
					Renderer.renderToImage(scene, 1920, 1080, effects, distance);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			case KeyEvent.VK_H -> hud = (hud < 3) ? hud + 1 : 0;
			case KeyEvent.VK_F -> {
				if (!effects.contains(dof))
					effects.add(dof);
				else
					effects.remove(dof);
			}
			case KeyEvent.VK_PLUS -> distance += 0.1;
			case KeyEvent.VK_MINUS -> distance = Math.max(0.1, distance - 0.1);
			case KeyEvent.VK_R -> autoDOF = !autoDOF;
			case KeyEvent.VK_X -> speed = 0;
			case KeyEvent.VK_CONTROL -> {
				ctrl = true;
			}
			case KeyEvent.VK_C -> new CreateObjectDialog(((JFrame) container), settingsDialog, settings);
			case KeyEvent.VK_DELETE -> {
				if (selectedObject != null) {
					Menu menu = null;
					if (selectedObject instanceof Shape) {
						menu = ((JFrame) container).getMenuBar().getMenu(2);
						settings.getScene().getShapes().remove(selectedObject);
					} else if (selectedObject instanceof Light) {
						menu = ((JFrame) container).getMenuBar().getMenu(3);
						settings.getScene().getLights().remove(selectedObject);
					}
					for (int i = 0; i < menu.getItemCount(); i++) {
						if (((MenuItem) menu.getItem(i)).getLabel().equals(selectedObject.getName())) {
							menu.remove(i);
							break;
						}
					}
				}
			}
			case KeyEvent.VK_ALT -> setCaptureCursor(true);
		}
	}

	private void handleKeyRelease(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_D, KeyEvent.VK_A -> deltaCamera.setX(0);
			case KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT -> deltaCamera.setY(0);
			case KeyEvent.VK_W, KeyEvent.VK_S -> deltaCamera.setZ(0);
			case KeyEvent.VK_Q, KeyEvent.VK_E -> tilt = 0;
			case KeyEvent.VK_ESCAPE -> handleEscapeKey();
			case KeyEvent.VK_CONTROL -> ctrl = false;
			case KeyEvent.VK_ALT -> setCaptureCursor(false);
		}
	}

	private void handleEscapeKey() {
		if (captureCursor) {
			setCaptureCursor(false);
		} else {
			if (settingsDialog.isVisible()) {
				int i = JOptionPane.showConfirmDialog(null, "Beenden?");
				if (i == 0)
					System.exit(0);
			} else {
				settingsDialog.setVisible(true);
			}
		}
	}

	// Mouse adapter for handling mouse input
	private MouseAdapter mouseClickAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			handleMousePress(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			switch (e.getButton()) {
				case MouseEvent.BUTTON2 -> setCaptureCursor(false);
			}
			axis = new Vector3(0, 0, 0);
		}
	};

	private void handleMousePress(MouseEvent e) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1 -> handleLeftClick(e);
			case MouseEvent.BUTTON2 -> setCaptureCursor(true);
			case MouseEvent.BUTTON3 -> handleRightClick(e);
		}

	}

	private void handleLeftClick(MouseEvent e) {
		RayHit atCursor = Renderer.getAt(scene, e.getX(), e.getY(), getWidth(), getHeight());
		if (atCursor != null && atCursor.getObject() instanceof Shape && atCursor.getShape() instanceof Arrow) {
			origX = e.getX(); // TODO only works if axis+ = x/y+
			origY = e.getY(); // TODO only works if axis+ = x/y+
			axis = ((Arrow) atCursor.getShape()).getAxis();
		}
	}

	private void handleRightClick(MouseEvent e) {
		RayHit atCursor = Renderer.getAt(scene, e.getX(), e.getY(), getWidth(), getHeight());
		List<Shape> shapes = new LinkedList<>(scene.getShapes());
		shapes.removeIf(shape -> shape instanceof Arrow);
		if (atCursor != null && atCursor.getObject() instanceof Shape && !(atCursor.getShape() instanceof Arrow)) {
			if (atCursor.getShape() == selectedObject) {
				selectedObject = null;
			} else {
				selectedObject = atCursor.getShape();
				selectedObject.addChangeListener((origPos, newPos) -> {
					for (Shape shape : scene.getShapes()) {
						if (shape instanceof Arrow)
							shape.setAnchor(newPos);
					}
				});
				shapes.addAll(List.of(new Arrow(selectedObject, new Vector3(1, 0, 0), Color.RED),
						new Arrow(selectedObject, new Vector3(0, 1, 0), Color.GREEN),
						new Arrow(selectedObject, new Vector3(0, 0, 1), Color.BLUE)));
				settings.setCurrentItem(selectedObject);
				if (ctrl) {
					if (settingsDialog.isVisible()) {
						settingsDialog.setVisible(false);
						setCaptureCursor(true);
					} else {
						settingsDialog.setVisible(true);
						setCaptureCursor(false);
					}
				}
			}
		}
		scene.setShapes(shapes);
	}

	private MouseAdapter mouseMoveAdapter = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			if (captureCursor) {
				int centerX = container.getX() + getWidth() / 2;
				int centerY = container.getY() + getHeight() / 2;
				int mouseXOffset = e.getXOnScreen() - centerX;
				int mouseYOffset = e.getYOnScreen() - centerY;
				deltaPY.x = Math.min(90, Math.max(-90, deltaPY.x + mouseYOffset * 0.001));
				deltaPY.y += mouseXOffset * 0.001;
				robot.mouseMove(centerX, centerY);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (captureCursor) {
				mouseMoved(e);
			} else if (selectedObject != null && axis != null) {
				double scale = ctrl ? 1 : 0.1;
				if (axis.x > 0 || axis.z > 0) {
					selectedObject.setAnchor(
							selectedObject.getAnchor().add(axis.scale(e.getX() - origX > 0 ? scale : -scale)));
				} else {
					selectedObject.setAnchor(
							selectedObject.getAnchor().add(axis.scale(origY - e.getY() > 0 ? scale : -scale)));
				}
				origX = e.getX(); // TODO only works if axis+ = x/y+
				origY = e.getY(); // TODO only works if axis+ = x/y+
			}
		}
	};

	private MouseWheelListener mouseWheelListener = new MouseWheelListener() {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (ctrl)
				speed -= ((double) e.getUnitsToScroll() / 100);
			else
				speed -= ((double) e.getUnitsToScroll() / 1000);
		}

	};
}