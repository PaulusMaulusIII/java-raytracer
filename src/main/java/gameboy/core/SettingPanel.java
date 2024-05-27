package gameboy.core;

import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gameboy.geometries.Sphere;
import gameboy.lights.Light;
import gameboy.materials.BasicMaterial;
import gameboy.utilities.Camera;
import gameboy.utilities.Color;
import gameboy.utilities.Scene;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Vector3;

public class SettingPanel extends JPanel {

	Scene scene = new Scene(new Camera(new Vector3(0, 2, -10), Math.toRadians(40)),
			new LinkedList<Shape>(List.of(new Sphere(new Vector3(0, 0, 0), new BasicMaterial(Color.PINK), 2))),
			new LinkedList<Light>(List.of(new Light(new Vector3(-5, 10, -5), Color.BLUE),
					new Light(new Vector3(5, 10, -5), Color.GREEN))));
	JFrame main;
	SettingPanel settingPanel = this;
	CurrentItemDisplay currentItemDisplay;

	public SettingPanel(JFrame jFrame) {
		setSize(400, 720);
		main = jFrame;
		currentItemDisplay = new CurrentItemDisplay();
		add(new MenuBar());
		add(currentItemDisplay);
	}

	public Scene getScene() {
		return scene;
	}

	public interface ObjectModification {
		public void modify(Object object);
	}

	public class MenuBar extends JMenuBar {
		public MenuBar() {
			super();
			add(new Menu("Cameras", scene.getCameras()));
			add(new Menu("Shapes", scene.getShapes()));
			add(new Menu("Lights", scene.getLights()));
			setPreferredSize(new Dimension(settingPanel.getWidth() - 10, 20));
		}

		public class Menu extends JMenu {
			public <T> Menu(String label, List<T> items) {
				super(label);
				for (T t : items) {
					JMenuItem menuItem = new JMenuItem(t.toString());
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							currentItemDisplay.setCurrentItem(t);
						}
					});
					add(menuItem);
				}
			}
		}
	}

	public class CurrentItemDisplay extends JPanel {

		Object currentItem = null;
		Label label = new Label();

		public CurrentItemDisplay() {
			super();
			setPreferredSize(new Dimension(settingPanel.getWidth() - 10, settingPanel.getHeight() - 20));
			label.setPreferredSize(new Dimension(settingPanel.getWidth() - 10, 20));
			label.setAlignment(1);
			add(label);
		}

		public void setCurrentItem(Object currentItem) {
			this.currentItem = currentItem;
			label.setText(currentItem.toString());
			try {
				remove(1);
			} catch (Exception e) {
			}
			if (currentItem instanceof Camera) {
				add(new CameraSettings());
			}
			else if (currentItem instanceof Shape) {
				add(new ShapeSettings());
			}
			else if (currentItem instanceof Light) {
				add(new LightSettings());
			}
		}

		public class Settings extends JPanel {
			Slider xSlider = new Slider("xAxis", -10, 10);
			Slider ySlider = new Slider("yAxis", -10, 10);
			Slider zSlider = new Slider("zAxis", -10, 10);

			public Settings() {
				setPreferredSize(new Dimension(settingPanel.getWidth() - 10, settingPanel.getHeight() - 30));
				xSlider.setPreferredSize(new Dimension(settingPanel.getWidth() - 10, 70));
				add(xSlider);
				ySlider.setPreferredSize(new Dimension(settingPanel.getWidth() - 10, 70));
				add(ySlider);
				zSlider.setPreferredSize(new Dimension(settingPanel.getWidth() - 10, 70));
				add(zSlider);
			}

			public void setPosValue(Vector3 pos) {
				xSlider.setValue(pos.x);
				ySlider.setValue(pos.y);
				zSlider.setValue(pos.z);
			}
		}

		public class CameraSettings extends Settings {
			Camera cam = (Camera) currentItem;
			Slider pitchSlider = new Slider("Pitch", -10, 10);
			Slider yawSlider = new Slider("Yaw", -10, 10);
			InputField fovField = new InputField("FOV", Math.toDegrees(cam.getFOV()), "°");

			ObjectModification setCameraPosition = (Object currentItem) -> {
				Camera camera = (Camera) currentItem;
				camera.setPosition(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));
			};

			public CameraSettings() {
				super();
				xSlider.setValue(cam.getPosition().x);
				ySlider.setValue(cam.getPosition().y);
				zSlider.setValue(cam.getPosition().z);
				xSlider.setAction(setCameraPosition);
				ySlider.setAction(setCameraPosition);
				zSlider.setAction(setCameraPosition);

				pitchSlider.setAction((Object object) -> {
					Camera camera = (Camera) object;
					camera.setPitch(Math.toRadians(-pitchSlider.getValue()));
				});
				add(pitchSlider);

				yawSlider.setAction((Object object) -> {
					Camera camera = (Camera) object;
					camera.setYaw(Math.toRadians(yawSlider.getValue()));
				});
				add(yawSlider);

				fovField.setAction((Object object) -> {
					Camera camera = (Camera) object;
					camera.setFOV(Double.parseDouble(fovField.getValue()));
				});
				add(fovField);
			}
		}

		public class LightSettings extends Settings {

			Light light = (Light) currentItem;
			InputField redField = new InputField("Red", light.getColor().getRed());
			InputField greenField = new InputField("Green", light.getColor().getGreen());
			InputField blueField = new InputField("Blue", light.getColor().getBlue());

			ObjectModification setLightPosition = (Object currentItem) -> {
				Light light = (Light) currentItem;
				light.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));
			};

			ObjectModification setLightColor = (Object object) -> {
				Light light = (Light) object;
				light.setColor(new Color(Integer.parseInt(redField.getValue()), Integer.parseInt(greenField.getValue()),
						Integer.parseInt(blueField.getValue())));
			};

			public LightSettings() {
				super();
				xSlider.setValue(light.getAnchor().x);
				ySlider.setValue(light.getAnchor().y);
				zSlider.setValue(light.getAnchor().z);
				xSlider.setAction(setLightPosition);
				ySlider.setAction(setLightPosition);
				zSlider.setAction(setLightPosition);

				redField.setAction(setLightColor);
				greenField.setAction(setLightColor);
				blueField.setAction(setLightColor);

				add(redField);
				add(greenField);
				add(blueField);
			}
		}

		public class ShapeSettings extends Settings {

			Shape shape = (Shape) currentItem;

			ObjectModification setCameraPosition = (Object currentItem) -> {
				Shape shape = (Shape) currentItem;
				shape.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));
			};

			public ShapeSettings() {
				super();
				xSlider.setValue(shape.getAnchor().x);
				ySlider.setValue(shape.getAnchor().y);
				zSlider.setValue(shape.getAnchor().z);
				xSlider.setAction(setCameraPosition);
				ySlider.setAction(setCameraPosition);
				zSlider.setAction(setCameraPosition);
			}
		}

		public class Slider extends JPanel {
			Label label;
			JSlider slider;
			TextField value;

			public Slider(String labelText, int min, int max) {
				super();
				label = new Label(labelText);
				label.setPreferredSize(new Dimension((int) (settingPanel.getWidth() - 10) / 3, 60));
				slider = new JSlider(min * 10, max * 10);
				slider.setPreferredSize(new Dimension((int) (settingPanel.getWidth() - 10) / 2, 60));
				slider.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {}

					@Override
					public void mousePressed(MouseEvent e) {}

					@Override
					public void mouseReleased(MouseEvent e) {
						value.setText("" + (Double.parseDouble(value.getText()) + ((double) slider.getValue() / 10)));
						slider.setValue(0);
					}

					@Override
					public void mouseEntered(MouseEvent e) {}

					@Override
					public void mouseExited(MouseEvent e) {}
				});
				slider.setMinorTickSpacing(10);
				slider.setMajorTickSpacing(50);
				slider.setPaintTicks(true);
				slider.setPaintLabels(true);

				value = new TextField("0", 1);
				value.setPreferredSize(new Dimension((int) (settingPanel.getWidth() - 10) / 6, 60));

				add(label);
				add(slider);
				add(value);
			}

			public void setValue(double value) {
				this.value.setText("" + value);
			}

			public double getValue() {
				return Double.parseDouble(value.getText());
			}

			public void setAction(ObjectModification modification) {
				slider.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						modification.modify(currentItem);
					}
				});

				value.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						modification.modify(currentItem);
					}
				});
			}
		}

		public class InputField extends JPanel {
			Label label = new Label();
			TextField field = new TextField(1);
			Label unit = new Label();

			public InputField(String labelText, Number value) {
				super();
				setSize(new Dimension((settingPanel.getWidth() - 10) / 3, 70));
				label.setSize(new Dimension(((settingPanel.getWidth() - 10) / 3) / 2, 70));
				label.setText(labelText);
				add(label);
				field.setSize(new Dimension(((settingPanel.getWidth() - 10) / 3) / 4, 70));
				field.setText("" + value);
				add(field);
				unit.setSize(new Dimension(((settingPanel.getWidth() - 10) / 3) / 4, 70));
				add(unit);
			}

			public InputField(String labelText, Number value, String unitText) {
				this(labelText, value);
				unit.setText(unitText);
			}

			public void setValue(Number value) {
				field.setText("" + value);
			}

			public String getValue() {
				return field.getText();
			}

			public void setAction(ObjectModification modification) {
				field.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						modification.modify(currentItem);
					}
				});
			}
		}
	}
}
