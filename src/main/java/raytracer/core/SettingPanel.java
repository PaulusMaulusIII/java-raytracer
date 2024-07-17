package raytracer.core;

import java.awt.Button;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
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
import javax.swing.event.ChangeListener;

import raytracer.geometries.Cube;
import raytracer.geometries.Cylinder;
import raytracer.geometries.Plane;
import raytracer.geometries.Sphere;
import raytracer.geometries.additional.Arrow;
import raytracer.lights.Light;
import raytracer.materials.BasicMaterial;
import raytracer.materials.CheckerMaterial;
import raytracer.materials.CubeMaterial;
import raytracer.materials.EdgeMaterial;
import raytracer.materials.GradientMaterial;
import raytracer.materials.MirrorMaterial;
import raytracer.materials.SphereMaterial;
import raytracer.materials.WaveMaterial;
import raytracer.shaders.BasicShader;
import raytracer.shaders.BloomShader;
import raytracer.shaders.DistanceShader;
import raytracer.shaders.PhongShader;
import raytracer.shaders.RainbowShader;
import raytracer.shaders.Shader;
import raytracer.utilities.Camera;
import raytracer.utilities.Color;
import raytracer.utilities.Material;
import raytracer.utilities.Object3D;
import raytracer.utilities.Scene;
import raytracer.utilities.Shape;
import raytracer.utilities.math.Vector3;

import javax.swing.event.ChangeEvent;

public class SettingPanel extends JPanel {
	List<Vector3> polygonVertices = Arrays.asList(new Vector3(-2, 0, 0), new Vector3(2, 0, 0), new Vector3(0, 0, 4),
			new Vector3(0, 4, 2));

	Scene scene = new Scene(new Camera(new Vector3(0, 0, 0), Math.toRadians(40)),
			List.of(new Plane(new Vector3(0, -2, 0),
					new CheckerMaterial(new PhongShader(), Color.WHITE, Color.BLACK, 4), new Vector3(0, 1, 0)),
					new Arrow(new Vector3(0, 0, 0), new BasicMaterial(new BasicShader(), Color.GREEN),
							new Vector3(0, 1, 0), 10)),
			List.of(new Light(new Vector3(7.5, 5, 20), new Color(255, 255, 72), 50),
					new Light(new Vector3(-7.5, 5, 20), new Color(255, 0, 72), 50)));
	JFrame main;
	SettingPanel settingPanel = this;
	CurrentItemDisplay currentItemDisplay;

	public SettingPanel(JFrame jFrame) {
		setSize(500, 800);
		main = jFrame;
		currentItemDisplay = new CurrentItemDisplay();
		add(new MenuBar());
		add(new JScrollPane(currentItemDisplay));
	}

	public Scene getScene() {
		return scene;
	}

	public interface ObjectModification {
		public void modify(Object3D object);
	}

	public class VBox extends Box {
		public VBox() {
			super(BoxLayout.Y_AXIS);
		}
	}

	public class MenuBar extends JMenuBar {
		private Button camera;
		private Menu shapes;
		private Menu lights;

		public MenuBar() {
			super();
			camera = new Button("Camera");
			lights = new Menu("Lights", scene.getLights());
			shapes = new Menu("Shapes", scene.getShapes());

			camera.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currentItemDisplay.setCurrentItem(scene.getCamera());
					currentItemDisplay.repaint();
				}
			});

			add(new Label("Java Ray-Tacer"));
			add(camera);
			add(shapes);
			add(lights);

			Button addObjectButton = new Button("Add Object3D");
			addObjectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new CreateObjectDialog(main);
				}
			});
			add(addObjectButton);

			Button removeCurrentObjectButton = new Button("Remove Object3D");
			removeCurrentObjectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Object3D currentItem = currentItemDisplay.getCurrentItem();
					if (currentItem != null) {
						Menu menu = null;
						if (currentItem instanceof Shape) {
							menu = shapes;
							scene.getShapes().remove(currentItem);
						}
						else if (currentItem instanceof Light) {
							menu = lights;
							scene.getLights().remove(currentItem);
						}
						for (int i = 0; i < menu.getItemCount(); i++) {
							if (((JMenuItem) menu.getItem(i)).getText().equals(currentItem.toString())) {
								menu.remove(i);
								break;
							}
						}
					}
				}
			});
			add(removeCurrentObjectButton);
		}

		public class CreateObjectDialog extends JDialog {

			public CreateObjectDialog(JFrame owner) {
				super(owner);
				JComboBox<? extends Object3D> objectSelector = new JComboBox<>(new Object3D[] {
						new Cylinder(new Vector3(0, 0, 0), new BasicMaterial(new BasicShader(), Color.WHITE),
								new Vector3(0, -1, 0), 2, 5),
						new Cube(new Vector3(0, 0, 0), new BasicMaterial(new BasicShader(), Color.WHITE), 4),
						new Plane(new Vector3(0, 0, 0), new BasicMaterial(new BasicShader(), Color.WHITE),
								new Vector3(0, 1, 0)),
						new Sphere(new Vector3(0, 0, 0), new BasicMaterial(new BasicShader(), Color.WHITE), 2),
						new Light(new Vector3(0, 0, 0))
				});
				setTitle("Select Shape To Add");
				setSize(400, 100);
				objectSelector.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Object3D objectToAdd = (Object3D) objectSelector.getSelectedItem();
						if (objectToAdd instanceof Shape) {
							Shape shapeToAdd = (Shape) objectToAdd;
							JMenuItem menuItem = new JMenuItem(shapeToAdd.toString());
							menuItem.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									currentItemDisplay.setCurrentItem(shapeToAdd);
								}
							});
							shapes.add(menuItem);
							scene.getShapes().add(shapeToAdd);
						}
						else if (objectToAdd instanceof Light) {
							Light lightToAdd = (Light) objectToAdd;
							JMenuItem menuItem = new JMenuItem(lightToAdd.toString());
							menuItem.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									currentItemDisplay.setCurrentItem(lightToAdd);
								}
							});
							lights.add(menuItem);
							scene.getLights().add(lightToAdd);
						}

						dispose();
					}
				});
				objectSelector.setAlignmentX(CENTER_ALIGNMENT);
				objectSelector.setAlignmentY(CENTER_ALIGNMENT);

				add(objectSelector);

				setVisible(true);
			}
		}

		public class Menu extends JMenu {
			public Menu(String label, List<? extends Object3D> items) {
				super(label);
				for (Object3D object : items) {
					if (!(object instanceof Arrow)) {
						JMenuItem menuItem = new JMenuItem(object.toString());
						menuItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								currentItemDisplay.setCurrentItem(object);
							}
						});
						add(menuItem);
					}
				}
			}
		}
	}

	public class CurrentItemDisplay extends VBox {

		private Object3D currentItem = null;
		private Label label = new Label();

		public CurrentItemDisplay() {
			super();
			add(label);
			label.setText("No Object Selected!");
		}

		public void setCurrentItem(Object3D currentItem) {
			this.currentItem = currentItem;
			if (currentItem != null) {
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
			else {
				label.setText("No Object Selected!");
				try {
					remove(1);
				} catch (Exception e) {
				}
			}
		}

		public Object3D getCurrentItem() {
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

			private ObjectModification setCameraPosition = (Object3D currentItem) -> ((Camera) currentItem)
					.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));

			public CameraSettings() {
				super();
				xSlider.setValue(cam.getAnchor().x);
				ySlider.setValue(cam.getAnchor().y);
				zSlider.setValue(cam.getAnchor().z);
				xSlider.setAction(setCameraPosition);
				ySlider.setAction(setCameraPosition);
				zSlider.setAction(setCameraPosition);

				pitchSlider.setAction(
						(Object3D object) -> ((Camera) object).setPitch(Math.toRadians(-pitchSlider.getValue())));
				add(pitchSlider);

				yawSlider
						.setAction((Object3D object) -> ((Camera) object).setYaw(Math.toRadians(yawSlider.getValue())));
				add(yawSlider);

				fovField.setAction((Object3D object) -> ((Camera) object)
						.setFOV(Math.toRadians(Double.parseDouble(fovField.getValue()))));
				add(fovField);
			}
		}

		public class LightSettings extends Settings {

			private Light light = (Light) currentItem;
			private ColorInput colorInput = new ColorInput(light.getColor());
			private InputField intensity = new InputField("Intensity", 1.0);

			private ObjectModification setLightPosition = (Object3D currentItem) -> ((Light) currentItem)
					.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));;

			public LightSettings() {
				super();
				xSlider.setValue(light.getAnchor().x);
				ySlider.setValue(light.getAnchor().y);
				zSlider.setValue(light.getAnchor().z);
				xSlider.setAction(setLightPosition);
				ySlider.setAction(setLightPosition);
				zSlider.setAction(setLightPosition);

				colorInput.setAction((Object3D object) -> ((Light) currentItem).setColor(colorInput.getColor()));
				intensity.setAction((Object3D object) -> ((Light) currentItem)
						.setIntensity(Double.parseDouble(intensity.getValue())));
				add(colorInput);
				add(intensity);
			}
		}

		public class ShapeSettings extends Settings {

			private ObjectModification setCameraPosition = (Object3D currentItem) -> ((Shape) currentItem)
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
					sideLengthInputField.setAction((Object3D object) -> ((Cube) object)
							.setSideLength(Double.parseDouble(sideLengthInputField.getValue())));
					add(sideLengthInputField);
				}
				else if (shape instanceof Sphere) {
					Sphere sphere = ((Sphere) shape);
					InputField radiusInputField = new InputField("Radius", sphere.getRadius());
					radiusInputField.setAction((Object3D object) -> ((Sphere) object)
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

					ObjectModification setPlaneAxis = (Object3D object) -> ((Plane) object)
							.setAxis(new Vector3(xDirectionSlider.getValue(), yDirectionSlider.getValue(),
									zDirectionSlider.getValue()).normalize());

					xDirectionSlider.setAction(setPlaneAxis);
					yDirectionSlider.setAction(setPlaneAxis);
					zDirectionSlider.setAction(setPlaneAxis);

					add(xDirectionSlider);
					add(yDirectionSlider);
					add(zDirectionSlider);
				}
				else if (shape instanceof Cylinder) {
					Cylinder cylinder = ((Cylinder) shape);

					Slider xDirectionSlider = new Slider("xDir", -10, 10);
					xDirectionSlider.setValue(cylinder.getAxis().x);
					Slider yDirectionSlider = new Slider("yDir", -10, 10);
					yDirectionSlider.setValue(cylinder.getAxis().y);
					Slider zDirectionSlider = new Slider("zDir", -10, 10);
					zDirectionSlider.setValue(cylinder.getAxis().z);

					ObjectModification setAxis = (Object3D object) -> ((Cylinder) object)
							.setAxis(new Vector3(xDirectionSlider.getValue(), yDirectionSlider.getValue(),
									zDirectionSlider.getValue()).normalize());

					xDirectionSlider.setAction(setAxis);
					yDirectionSlider.setAction(setAxis);
					zDirectionSlider.setAction(setAxis);

					InputField height = new InputField("Height", cylinder.getHeight());
					height.setAction(
							(Object3D object) -> ((Cylinder) object).setHeight(Double.parseDouble(height.getValue())));

					InputField radius = new InputField("Height", cylinder.getRadius());
					radius.setAction(
							(Object3D object) -> ((Cylinder) object).setRadius(Double.parseDouble(radius.getValue())));

					add(xDirectionSlider);
					add(yDirectionSlider);
					add(zDirectionSlider);
					add(height);
					add(radius);

				}
				add(new MaterialSettingsPanel());
				add(new ShaderSettingPanel());
			}

			public class MaterialSettingsPanel extends VBox {

				private Material material = shape.getMaterial();
				private Label label = new Label(material.getClass().toString());
				private JComboBox<? extends Material> materialSelector = new JComboBox<>(new Material[] {
						new BasicMaterial(new BasicShader(), Color.WHITE),
						new CheckerMaterial(new BasicShader(), Color.WHITE, Color.BLACK, 2),
						new CubeMaterial(new BasicShader()), new EdgeMaterial(new BasicShader(), Color.WHITE, .1),
						new GradientMaterial(new BasicShader(), Color.WHITE, Color.BLACK),
						new MirrorMaterial(new BasicShader()), new SphereMaterial(new BasicShader()),
						new WaveMaterial(new BasicShader())
				});
				private ColorInput mainColorInput = new ColorInput(material.getColor());
				private InputField reflectivity = new InputField("Refl", material.getReflectivity());
				private InputField transparency = new InputField("Trans", material.getTransparency());

				public MaterialSettingsPanel() {
					super();
					label.setAlignment(1);
					add(label);
					materialSelector.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							new ObjectModification() {
								@Override
								public void modify(Object3D object) {
									Material material = ((Material) materialSelector.getSelectedItem());
									material.setShape((Shape) object);
									((Shape) object).setMaterial(material);

								}
							}.modify(currentItem);
						}
					});
					add(materialSelector);
					mainColorInput.setAction(
							(Object3D object) -> ((Shape) object).getMaterial().setColor(mainColorInput.getColor()));
					add(mainColorInput);
					if (material instanceof CheckerMaterial) {
						ColorInput secColorInput = new ColorInput(((CheckerMaterial) material).getSecColor());
						secColorInput.setAction((Object3D object) -> ((CheckerMaterial) ((Shape) object).getMaterial())
								.setSecColor(secColorInput.getColor()));
						add(secColorInput);
						InputField gridSizeField = new InputField("Gridsize",
								((CheckerMaterial) material).getGridsize());
						gridSizeField.setAction((Object3D object) -> ((CheckerMaterial) ((Shape) object).getMaterial())
								.setGridsize(Double.parseDouble(gridSizeField.getValue())));
						add(gridSizeField);
					}
					if (!(material instanceof MirrorMaterial)) {
						reflectivity.setAction((Object3D object) -> ((Shape) object).getMaterial()
								.setReflectivity(Double.parseDouble(reflectivity.getValue())));
						add(reflectivity);
					}
					transparency.setAction((Object3D object) -> ((Shape) object).getMaterial()
							.setTransparency(Double.parseDouble(transparency.getValue())));
					add(transparency);
				}
			}

			public class ShaderSettingPanel extends VBox {
				private Shader shader = shape.getMaterial().getShader();
				private Label label = new Label(shader.getClass().toString());
				private JComboBox<? extends Shader> shaderSelector = new JComboBox<>(new Shader[] {
						new BasicShader(), new BloomShader(), new DistanceShader(), new PhongShader(),
						new RainbowShader()
				});

				private InputField emission = new InputField("Emission", 1);

				public ShaderSettingPanel() {
					super();
					label.setAlignment(1);
					add(label);
					shaderSelector.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							new ObjectModification() {
								@Override
								public void modify(Object3D object) {
									((Shape) object).getMaterial().setShader((Shader) shaderSelector.getSelectedItem());
								}
							}.modify(currentItem);
						}
					});
					add(shaderSelector);
					if (shader instanceof PhongShader) {
						emission.setAction((Object3D object) -> (((Shape) object).getMaterial())
								.setEmission(Double.parseDouble(emission.getValue())));

						add(emission);
					}
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