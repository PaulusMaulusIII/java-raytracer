package com.paulusmaulus.raytracer.core.swing_assets.settings;

import java.awt.Button;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import com.paulusmaulus.raytracer.core.interfaces.ObjectModification;
import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.core.swing_assets.SettingPanel;
import com.paulusmaulus.raytracer.core.swing_assets.additional.VBox;
import com.paulusmaulus.raytracer.core.swing_assets.inputs.ColorInput;
import com.paulusmaulus.raytracer.core.swing_assets.inputs.InputField;
import com.paulusmaulus.raytracer.core.swing_assets.inputs.Slider;
import com.paulusmaulus.raytracer.geometries.Cube;
import com.paulusmaulus.raytracer.geometries.Cylinder;
import com.paulusmaulus.raytracer.geometries.Plane;
import com.paulusmaulus.raytracer.geometries.Sphere;
import com.paulusmaulus.raytracer.materials.BasicMaterial;
import com.paulusmaulus.raytracer.materials.CheckerMaterial;
import com.paulusmaulus.raytracer.materials.CubeMaterial;
import com.paulusmaulus.raytracer.materials.EdgeMaterial;
import com.paulusmaulus.raytracer.materials.GradientMaterial;
import com.paulusmaulus.raytracer.materials.MirrorMaterial;
import com.paulusmaulus.raytracer.materials.SphereMaterial;
import com.paulusmaulus.raytracer.materials.TextureMaterial;
import com.paulusmaulus.raytracer.materials.WaveMaterial;
import com.paulusmaulus.raytracer.shaders.BasicShader;
import com.paulusmaulus.raytracer.shaders.BloomShader;
import com.paulusmaulus.raytracer.shaders.DistanceShader;
import com.paulusmaulus.raytracer.shaders.PhongShader;
import com.paulusmaulus.raytracer.shaders.RainbowShader;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.Texture;
import com.paulusmaulus.raytracer.utilities.math.Vector3;;

public class ShapeSettings extends Settings {

	private ObjectModification setCameraPosition = (Object3D currentItem) -> ((Shape) currentItem)
			.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));

	private Shape shape;
	private SettingPanel settingPanel;

	public ShapeSettings(SettingPanel parent) {
		super();
		this.settingPanel = parent;
		shape = (Shape) settingPanel.getCurrentItemDisplay().getCurrentItem();

		xSlider.setValue(shape.getAnchor().x);
		ySlider.setValue(shape.getAnchor().y);
		zSlider.setValue(shape.getAnchor().z);
		xSlider.setAction(setCameraPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());
		ySlider.setAction(setCameraPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());
		zSlider.setAction(setCameraPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());

		if (shape instanceof Cube) {
			Cube cube = ((Cube) shape);
			InputField sideLengthInputField = new InputField("Sidelength", cube.getSideLength());
			sideLengthInputField.setAction(
					(Object3D object) -> ((Cube) object)
							.setSideLength(Double.parseDouble(sideLengthInputField.getValue())),
					settingPanel.getCurrentItemDisplay().getCurrentItem());
			add(sideLengthInputField);
		}
		else if (shape instanceof Sphere) {
			Sphere sphere = ((Sphere) shape);
			InputField radiusInputField = new InputField("Radius", sphere.getRadius());
			radiusInputField.setAction(
					(Object3D object) -> ((Sphere) object).setRadius(Double.parseDouble(radiusInputField.getValue())),
					settingPanel.getCurrentItemDisplay().getCurrentItem());
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

			ObjectModification setPlaneAxis = (Object3D object) -> ((Plane) object).setAxis(
					new Vector3(xDirectionSlider.getValue(), yDirectionSlider.getValue(), zDirectionSlider.getValue())
							.normalize());

			xDirectionSlider.setAction(setPlaneAxis, settingPanel.getCurrentItemDisplay().getCurrentItem());
			yDirectionSlider.setAction(setPlaneAxis, settingPanel.getCurrentItemDisplay().getCurrentItem());
			zDirectionSlider.setAction(setPlaneAxis, settingPanel.getCurrentItemDisplay().getCurrentItem());

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

			ObjectModification setAxis = (Object3D object) -> ((Cylinder) object).setAxis(
					new Vector3(xDirectionSlider.getValue(), yDirectionSlider.getValue(), zDirectionSlider.getValue())
							.normalize());

			xDirectionSlider.setAction(setAxis, settingPanel.getCurrentItemDisplay().getCurrentItem());
			yDirectionSlider.setAction(setAxis, settingPanel.getCurrentItemDisplay().getCurrentItem());
			zDirectionSlider.setAction(setAxis, settingPanel.getCurrentItemDisplay().getCurrentItem());

			InputField height = new InputField("Height", cylinder.getHeight());
			height.setAction((Object3D object) -> ((Cylinder) object).setHeight(Double.parseDouble(height.getValue())),
					settingPanel.getCurrentItemDisplay().getCurrentItem());

			InputField radius = new InputField("Radius", cylinder.getRadius());
			radius.setAction((Object3D object) -> ((Cylinder) object).setRadius(Double.parseDouble(radius.getValue())),
					settingPanel.getCurrentItemDisplay().getCurrentItem());

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
				new WaveMaterial(new BasicShader()), new TextureMaterial(new Texture(), new BasicShader())
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
					}.modify(settingPanel.getCurrentItemDisplay().getCurrentItem());
				}
			});
			add(materialSelector);
			mainColorInput.setAction(
					(Object3D object) -> ((Shape) object).getMaterial().setColor(mainColorInput.getColor()),
					settingPanel.getCurrentItemDisplay().getCurrentItem());
			add(mainColorInput);
			if (material instanceof CheckerMaterial) {
				ColorInput secColorInput = new ColorInput(((CheckerMaterial) material).getSecColor());
				secColorInput.setAction(
						(Object3D object) -> ((CheckerMaterial) ((Shape) object).getMaterial())
								.setSecColor(secColorInput.getColor()),
						settingPanel.getCurrentItemDisplay().getCurrentItem());
				add(secColorInput);
				InputField gridSizeField = new InputField("Gridsize", ((CheckerMaterial) material).getGridsize());
				gridSizeField.setAction(
						(Object3D object) -> ((CheckerMaterial) ((Shape) object).getMaterial())
								.setGridsize(Double.parseDouble(gridSizeField.getValue())),
						settingPanel.getCurrentItemDisplay().getCurrentItem());
				add(gridSizeField);
			}
			if (!(material instanceof MirrorMaterial)) {
				reflectivity.setAction(
						(Object3D object) -> ((Shape) object).getMaterial()
								.setReflectivity(Double.parseDouble(reflectivity.getValue())),
						settingPanel.getCurrentItemDisplay().getCurrentItem());
				add(reflectivity);
			}
			if (material instanceof TextureMaterial) {
				Button selectImage = new Button("Select Image");
				selectImage.addActionListener((ActionEvent e) -> {
					JFileChooser jfc = new JFileChooser();
					jfc.showOpenDialog(selectImage);
					BufferedImage image = null;
					try {
						image = ImageIO.read(jfc.getSelectedFile());
					} catch (IOException e1) {
						System.out.println("A");
						e1.printStackTrace();
					}
					((TextureMaterial) material).getTexture().setImage(image);
				});
				add(selectImage);
			}
			transparency.setAction(
					(Object3D object) -> ((Shape) object).getMaterial()
							.setTransparency(Double.parseDouble(transparency.getValue())),
					settingPanel.getCurrentItemDisplay().getCurrentItem());
			add(transparency);
		}
	}

	public class ShaderSettingPanel extends VBox {
		private Shader shader = shape.getMaterial().getShader();
		private Label label = new Label(shader.getClass().toString());
		private JComboBox<? extends Shader> shaderSelector = new JComboBox<>(new Shader[] {
				new BasicShader(), new BloomShader(), new DistanceShader(), new PhongShader(), new RainbowShader()
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
					}.modify(settingPanel.getCurrentItemDisplay().getCurrentItem());
				}
			});
			add(shaderSelector);
			if (shader instanceof PhongShader) {
				emission.setAction(
						(Object3D object) -> (((Shape) object).getMaterial())
								.setEmission(Double.parseDouble(emission.getValue())),
						settingPanel.getCurrentItemDisplay().getCurrentItem());

				add(emission);
			}
		}
	}
}
