package gameboy.core;

import java.awt.Button;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gameboy.geometries.Cone;
import gameboy.geometries.Cube;
import gameboy.geometries.Line;
import gameboy.geometries.Plane;
import gameboy.geometries.Sphere;
import gameboy.lights.Light;
import gameboy.materials.BasicMaterial;
import gameboy.materials.CheckerMaterial;
import gameboy.materials.CubeMaterial;
import gameboy.materials.GradientMaterial;
import gameboy.materials.MirrorMaterial;
import gameboy.materials.SphereMaterial;
import gameboy.materials.WaveMaterial;
import gameboy.utilities.Camera;
import gameboy.utilities.Color;
import gameboy.utilities.Material;
import gameboy.utilities.Scene;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Vector3;

public class SettingPanel extends JPanel {

	Scene scene = new Scene(new Camera(new Vector3(0, 2, -10), Math.toRadians(40)),
			new LinkedList<Shape>(List.of(new Sphere(new Vector3(0, 0, 0), new SphereMaterial(), 2),
					new Plane(new Vector3(0, -5, 0), new CheckerMaterial(Color.BLACK, Color.WHITE, 4),
							new Vector3(0, 1, 0)))),
			new LinkedList<Light>(List.of(new Light(new Vector3(-5, 10, -5), Color.BLUE),
					new Light(new Vector3(5, 10, -5), Color.GREEN))));
	JFrame main;
	SettingPanel settingPanel = this;
	CurrentItemDisplay currentItemDisplay;

	public SettingPanel(JFrame jFrame) {
		setSize(400, 800);

		main = jFrame;
		currentItemDisplay = new CurrentItemDisplay();
		add(new MenuBar());
		add(new JScrollPane(currentItemDisplay));
	}

	public Scene getScene() {
		return scene;
	}

	public interface ObjectModification {
		public void modify(Object object);
	}

	public class VBox extends Box {
		public VBox() {
			super(BoxLayout.Y_AXIS);
		}
	}

	public class MenuBar extends JMenuBar {
		private Menu cameras;
		private Menu shapes;
		private Menu lights;

		private JComboBox<? extends Shape> shapeSelector = new JComboBox<>(new Shape[] {
				new Cone(new Vector3(0, 0, 0), new BasicMaterial(Color.WHITE), new Vector3(0, 1, 0), Math.toRadians(10),
						5),
				new Cube(new Vector3(0, 0, 0), new BasicMaterial(Color.WHITE), 4),
				new Line(new Vector3(0, 0, 0), new Vector3(0, 0, 1), new BasicMaterial(Color.WHITE)),
				new Plane(new Vector3(0, 0, 0), new BasicMaterial(Color.WHITE), new Vector3(0, 1, 0)),
				new Sphere(new Vector3(0, 0, 0), new BasicMaterial(Color.WHITE), 2)
		});

		public MenuBar() {
			super();
			cameras = new Menu("Cameras", scene.getCameras());
			lights = new Menu("Lights", scene.getLights());
			shapes = new Menu("Shapes", scene.getShapes());

			add(new Label("Java Ray-Tacer"));
			add(cameras);
			add(shapes);
			add(lights);

			Button addShapeButton = new Button("Add Shape");
			addShapeButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new NewShapeDialog(main);
				}
			});
			add(addShapeButton);
		}

		public class NewShapeDialog extends JDialog {

			public NewShapeDialog(JFrame owner) {
				super(owner);
				setTitle("Select Shape To Add");
				setSize(400, 100);
				shapeSelector.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Shape shapeToAdd = ((Shape) shapeSelector.getSelectedItem());
						JMenuItem menuItem = new JMenuItem(shapeToAdd.toString());
						menuItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								currentItemDisplay.setCurrentItem(shapeToAdd);
							}
						});
						shapes.add(menuItem);
						scene.getShapes().add(shapeToAdd);
						dispose();
					}
				});
				shapeSelector.setAlignmentX(CENTER_ALIGNMENT);
				shapeSelector.setAlignmentY(CENTER_ALIGNMENT);

				add(shapeSelector);

				setVisible(true);
			}
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

	public class CurrentItemDisplay extends VBox {

		private Object currentItem = null;
		private Label label = new Label();

		public CurrentItemDisplay() {
			super();
			add(label);
		}

		public void setCurrentItem(Object currentItem) {
			this.currentItem = currentItem;
			label.setText(currentItem.toString());
			try {
				remove(1);
			} catch (Exception e) {
			}
			if (currentItem instanceof Camera)
				add(new CameraSettings());
			else if (currentItem instanceof Shape)
				add(new ShapeSettings());
			else if (currentItem instanceof Light)
				add(new LightSettings());

		}

		public Object getCurrentItem() {
			return currentItem;
		}

		public class Settings extends VBox {

			protected Slider xSlider = new Slider("xAxis", -10, 10);
			protected Slider ySlider = new Slider("yAxis", -10, 10);
			protected Slider zSlider = new Slider("zAxis", -10, 10);

			public Settings() {
				add(xSlider);
				add(ySlider);
				add(zSlider);
			}

			public void setPosValue(Vector3 pos) {
				xSlider.setValue(pos.x);
				ySlider.setValue(pos.y);
				zSlider.setValue(pos.z);
			}
		}

		public class CameraSettings extends Settings {
			private Camera cam = (Camera) currentItem;
			private Slider pitchSlider = new Slider("Pitch", -10, 10);
			private Slider yawSlider = new Slider("Yaw", -10, 10);
			private InputField fovField = new InputField("FOV", Math.toDegrees(cam.getFOV()), "°");

			private ObjectModification setCameraPosition = (Object currentItem) -> ((Camera) currentItem)
					.setPosition(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));

			public CameraSettings() {
				super();
				xSlider.setValue(cam.getPosition().x);
				ySlider.setValue(cam.getPosition().y);
				zSlider.setValue(cam.getPosition().z);
				xSlider.setAction(setCameraPosition);
				ySlider.setAction(setCameraPosition);
				zSlider.setAction(setCameraPosition);

				pitchSlider.setAction(
						(Object object) -> ((Camera) object).setPitch(Math.toRadians(-pitchSlider.getValue())));
				add(pitchSlider);

				yawSlider.setAction((Object object) -> ((Camera) object).setYaw(Math.toRadians(yawSlider.getValue())));
				add(yawSlider);

				fovField.setAction((Object object) -> ((Camera) object)
						.setFOV(Math.toRadians(Double.parseDouble(fovField.getValue()))));
				add(fovField);
			}
		}

		public class LightSettings extends Settings {

			private Light light = (Light) currentItem;
			private ColorInput colorInput = new ColorInput(light.getColor());

			private ObjectModification setLightPosition = (Object currentItem) -> ((Light) currentItem)
					.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));;

			public LightSettings() {
				super();
				xSlider.setValue(light.getAnchor().x);
				ySlider.setValue(light.getAnchor().y);
				zSlider.setValue(light.getAnchor().z);
				xSlider.setAction(setLightPosition);
				ySlider.setAction(setLightPosition);
				zSlider.setAction(setLightPosition);

				colorInput.setAction((Object object) -> ((Light) currentItem).setColor(colorInput.getColor()));
				add(colorInput);
			}
		}

		public class ShapeSettings extends Settings {

			private ObjectModification setCameraPosition = (Object currentItem) -> ((Shape) currentItem)
					.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));

			private Shape shape = (Shape) currentItem;

			public ShapeSettings() {
				super();
				xSlider.setValue(shape.getAnchor().x);
				ySlider.setValue(shape.getAnchor().y);
				zSlider.setValue(shape.getAnchor().z);
				xSlider.setAction(setCameraPosition);
				ySlider.setAction(setCameraPosition);
				zSlider.setAction(setCameraPosition);

				if (shape instanceof Cube) {
					Cube cube = ((Cube) shape);
					InputField sideLengthInputField = new InputField("Sidelength", cube.getSideLength());
					sideLengthInputField.setAction((Object object) -> ((Cube) object)
							.setSideLength(Double.parseDouble(sideLengthInputField.getValue())));
					add(sideLengthInputField);
				}
				else if (shape instanceof Sphere) {
					Sphere sphere = ((Sphere) shape);
					InputField radiusInputField = new InputField("Radius", sphere.getRadius());
					radiusInputField.setAction((Object object) -> ((Sphere) object)
							.setRadius(Double.parseDouble(radiusInputField.getValue())));
					add(radiusInputField);
				}
				else if (shape instanceof Plane) {
					Plane plane = ((Plane) shape);

					Slider xDirectionSlider = new Slider("xDir", -10, 10);
					xDirectionSlider.setValue(plane.getAxis().x);
					Slider yDirectionSlider = new Slider("yDir", -10, 10);
					yDirectionSlider.setValue(plane.getAxis().y);
					Slider zDirectionSlider = new Slider("zDir", -10, 10);
					zDirectionSlider.setValue(plane.getAxis().z);

					ObjectModification setPlaneAxis = (Object object) -> ((Plane) object)
							.setAxis(new Vector3(xDirectionSlider.getValue(), yDirectionSlider.getValue(),
									zDirectionSlider.getValue()).normalize());

					xDirectionSlider.setAction(setPlaneAxis);
					yDirectionSlider.setAction(setPlaneAxis);
					zDirectionSlider.setAction(setPlaneAxis);

					add(xDirectionSlider);
					add(yDirectionSlider);
					add(zDirectionSlider);
				}
				add(new MaterialSettingsPanel());
			}

			public class MaterialSettingsPanel extends VBox {

				private Material material = shape.getMaterial();
				private Label label = new Label(material.getClass().toString());
				private JComboBox<? extends Material> materialSelector = new JComboBox<>(new Material[] {
						new BasicMaterial(Color.WHITE), new CheckerMaterial(Color.WHITE, Color.BLACK, 2),
						new CubeMaterial(), new GradientMaterial(Color.WHITE, Color.BLACK), new MirrorMaterial(),
						new SphereMaterial(), new WaveMaterial()
				});
				private ColorInput mainColorInput = new ColorInput(material.getColor());
				private InputField reflectivity = new InputField("Refl", material.getReflectivity());
				private InputField shininess = new InputField("Shiny", material.getShininess());
				private InputField emission = new InputField("Emiss", material.getEmission());

				public MaterialSettingsPanel() {
					super();
					label.setAlignment(1);
					add(label);
					materialSelector.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							new ObjectModification() {
								@Override
								public void modify(Object object) {
									Material material = ((Material) materialSelector.getSelectedItem());
									material.setShape((Shape) object);
									((Shape) object).setMaterial(material);

								}
							}.modify(currentItem);
						}
					});
					add(materialSelector);
					mainColorInput.setAction(
							(Object object) -> ((Shape) object).getMaterial().setColor(mainColorInput.getColor()));
					add(mainColorInput);
					if (material instanceof CheckerMaterial) {
						ColorInput secColorInput = new ColorInput(((CheckerMaterial) material).getSecColor());
						secColorInput.setAction((Object object) -> ((CheckerMaterial) ((Shape) object).getMaterial())
								.setSecColor(secColorInput.getColor()));
						add(secColorInput);
						InputField gridSizeField = new InputField("Gridsize",
								((CheckerMaterial) material).getGridsize());
						gridSizeField.setAction((Object object) -> ((CheckerMaterial) ((Shape) object).getMaterial())
								.setGridsize(Double.parseDouble(gridSizeField.getValue())));
						add(gridSizeField);
					}
					if (!(material instanceof MirrorMaterial)) {
						reflectivity.setAction((Object object) -> ((Shape) object).getMaterial()
								.setReflectivity(Double.parseDouble(reflectivity.getValue())));
						add(reflectivity);
					}
					shininess.setAction((Object object) -> ((Shape) object).getMaterial()
							.setShininess(Double.parseDouble(shininess.getValue())));
					add(shininess);
					emission.setAction((Object object) -> ((Shape) object).getMaterial()
							.setEmission(Double.parseDouble(emission.getValue())));
					add(emission);
				}
			}
		}

		public class Slider extends JPanel {
			private Label label;
			private JSlider slider;
			private TextField value;

			public Slider(String labelText, int min, int max) {
				super();
				label = new Label(labelText);
				slider = new JSlider(min * 10, max * 10);
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

		public class ColorInput extends JPanel {
			private InputField redField;
			private InputField greenField;
			private InputField blueField;

			public ColorInput(Color color) {
				redField = new InputField("Red", color.getRed());
				greenField = new InputField("Green", color.getGreen());
				blueField = new InputField("Blue", color.getBlue());

				redField.setBackground(Color.RED.toAWT());
				greenField.setBackground(Color.GREEN.toAWT());
				blueField.setBackground(Color.BLUE.toAWT());

				add(redField);
				add(greenField);
				add(blueField);
			}

			public void setAction(ObjectModification objectModification) {
				redField.setAction(objectModification);
				greenField.setAction(objectModification);
				blueField.setAction(objectModification);
			}

			public Color getColor() {
				return new Color(Integer.parseInt(redField.getValue()), Integer.parseInt(greenField.getValue()),
						Integer.parseInt(blueField.getValue()));
			}
		}

		public class InputField extends JPanel {
			private Label label = new Label();
			private TextField field = new TextField(1);
			private Label unit = new Label();

			public InputField(String labelText, Number value) {
				super();
				label.setText(labelText);
				add(label);
				field.setText("" + value);
				add(field);
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
