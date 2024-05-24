package gameboy.core;

import gameboy.utilities.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import gameboy.core.enums.Initializers;
import gameboy.core.enums.Materials;
import gameboy.core.enums.Properties;
import gameboy.geometries.Cone;
import gameboy.geometries.Cube;
import gameboy.geometries.Plane;
import gameboy.geometries.Sphere;
import gameboy.lights.Light;
import gameboy.materials.BasicMaterial;
import gameboy.materials.CheckerMaterial;
import gameboy.materials.CubeMaterial;
import gameboy.materials.MirrorMaterial;
import gameboy.materials.SphereMaterial;
import gameboy.materials.WaveMaterial;
import gameboy.utilities.Camera;
import gameboy.utilities.Material;
import gameboy.utilities.Scene;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Vector3;

public class Interpreter {

	public Scene interpret(String code) {
		List<Camera> cameras = new LinkedList<>();
		List<Light> lights = new LinkedList<>();
		List<Shape> shapes = new LinkedList<>();
		String[] lines = code.split("\n");

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();

			for (Initializers initializer : Initializers.INITIALIZERS) {
				if (line.startsWith(initializer.getPropertiestring())) {
					HashMap<Properties, String> properties = extractTokenValues(lines, i);
					switch (initializer) {
					case CAMERA:
						cameras.add(createCamera(properties));
						break;
					case LIGHT:
						lights.add(createLight(properties));
						break;
					case CUBE:
						shapes.add(createCube(properties));
						break;
					case SPHERE:
						shapes.add(createSphere(properties));
						break;
					case PLANE:
						shapes.add(createPlane(properties));
						break;
					case CONE:
						shapes.add(createCone(properties));
						break;
					default:
						System.err.println("Not an initializer");
					}
				}
			}
		}
		Scene scene = new Scene(cameras, shapes, lights);
		for (Shape shape : shapes) {
			shape.setScene(scene);
		}
		return scene;
	}

	private HashMap<Properties, String> extractTokenValues(String[] lines, int index) {
		HashMap<Properties, String> properties = new HashMap<>();
		for (int i = index + 1; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.equals("}"))
				break;
			String[] valString = line.split(":");
			if (valString.length == 2) {
				for (Properties token : Properties.PROPERTIES) {
					if (valString[0].trim().equals(token.getTokenString()))
						properties.put(token, valString[1].trim());
				}
			}
		}
		return properties;
	}

	private Shape createPlane(HashMap<Properties, String> properties) {
		Vector3 anchor = parseVector(properties.get(Properties.POSITION));
		Vector3 axis = parseVector(properties.get(Properties.AXIS).toUpperCase());
		Color color = parseColor(properties.getOrDefault((Properties.COLOR), "{255,255,255}"));
		Color color2 = parseColor(properties.getOrDefault(Properties.SEC_COLOR, "{" + color.toAWT().darker().getRed()
				+ "," + color.toAWT().darker().getGreen() + "," + color.toAWT().darker().getBlue() + "}"));
		double gridsize = Double.parseDouble(properties.getOrDefault(Properties.GRIDSIZE, "2"));
		double reflectivity = Double.parseDouble(properties.getOrDefault(Properties.REFELECTIVITY, "0"));
		double emission = Double.parseDouble(properties.getOrDefault(Properties.EMISSION, "0"));
		double shininess = Double.parseDouble(properties.getOrDefault(Properties.SHININESS, "1"));
		Material material = parseMaterial(properties.get(Properties.MATERIAL), color, color2, gridsize, reflectivity,
				emission, shininess);
		return new Plane(anchor, material, axis);
	}

	private Shape createCube(HashMap<Properties, String> properties) {
		Vector3 center = parseVector(properties.get(Properties.POSITION));
		double sideLength = Double.parseDouble(properties.get(Properties.SIDELENGTH));
		Color color = parseColor(properties.getOrDefault((Properties.COLOR), "{255,255,255}"));
		Color color2 = parseColor(properties.getOrDefault(Properties.SEC_COLOR, "{" + color.toAWT().darker().getRed()
				+ "," + color.toAWT().darker().getGreen() + "," + color.toAWT().darker().getBlue() + "}"));
		double gridsize = Double.parseDouble(properties.getOrDefault(Properties.GRIDSIZE, "2"));
		double reflectivity = Double.parseDouble(properties.getOrDefault(Properties.REFELECTIVITY, "0"));
		double emission = Double.parseDouble(properties.getOrDefault(Properties.EMISSION, "0"));
		double shininess = Double.parseDouble(properties.getOrDefault(Properties.SHININESS, "1"));
		Material material = parseMaterial(properties.get(Properties.MATERIAL), color, color2, gridsize, reflectivity,
				emission, shininess);
		return new Cube(center, material, sideLength);
	}

	private Shape createSphere(HashMap<Properties, String> properties) {
		Vector3 center = parseVector(properties.get(Properties.POSITION));
		double radius = Double.parseDouble(properties.get(Properties.RADIUS));
		Color color = parseColor(properties.getOrDefault((Properties.COLOR), "{255,255,255}"));
		Color color2 = parseColor(properties.getOrDefault(Properties.SEC_COLOR, "{" + color.toAWT().darker().getRed()
				+ "," + color.toAWT().darker().getGreen() + "," + color.toAWT().darker().getBlue() + "}"));
		double gridsize = Double.parseDouble(properties.getOrDefault(Properties.GRIDSIZE, "2"));
		double reflectivity = Double.parseDouble(properties.getOrDefault(Properties.REFELECTIVITY, "0"));
		double emission = Double.parseDouble(properties.getOrDefault(Properties.EMISSION, "0"));
		double shininess = Double.parseDouble(properties.getOrDefault(Properties.SHININESS, "1"));
		Material material = parseMaterial(properties.get(Properties.MATERIAL), color, color2, gridsize, reflectivity,
				emission, shininess);
		return new Sphere(center, material, radius);
	}

	private Shape createCone(HashMap<Properties, String> properties) {
		Vector3 center = parseVector(properties.get(Properties.POSITION));
		Vector3 axis = parseVector(properties.get(Properties.SIDE));
		double angle = parseAngle(properties.get(Properties.ANGLE));
		double height = Double.parseDouble(properties.get(Properties.HEIGHT));
		Color color = parseColor(properties.getOrDefault((Properties.COLOR), "{255,255,255}"));
		Color color2 = parseColor(properties.getOrDefault(Properties.SEC_COLOR, "{" + color.toAWT().darker().getRed()
				+ "," + color.toAWT().darker().getGreen() + "," + color.toAWT().darker().getBlue() + "}"));
		double gridsize = Double.parseDouble(properties.getOrDefault(Properties.GRIDSIZE, "2"));
		double reflectivity = Double.parseDouble(properties.getOrDefault(Properties.REFELECTIVITY, "0"));
		double emission = Double.parseDouble(properties.getOrDefault(Properties.EMISSION, "0"));
		double shininess = Double.parseDouble(properties.getOrDefault(Properties.SHININESS, "1"));
		Material material = parseMaterial(properties.get(Properties.MATERIAL), color, color2, gridsize, reflectivity,
				emission, shininess);
		return new Cone(center, material, axis, angle, height);
	}

	private Light createLight(HashMap<Properties, String> properties) {
		Vector3 position = parseVector(properties.get(Properties.POSITION));
		Color color = parseColor(properties.getOrDefault(Properties.COLOR, "{255,255,255}"));
		return new Light(position, color);
	}

	private Camera createCamera(HashMap<Properties, String> properties) {
		Vector3 position = parseVector(properties.get(Properties.POSITION));
		double fov = parseAngle(properties.getOrDefault(Properties.FOV, "40°"));
		double pitch = parseAngle(properties.getOrDefault(Properties.PITCH, "0"));
		double yaw = parseAngle(properties.getOrDefault(Properties.YAW, "0"));
		return new Camera(position, fov, pitch, yaw);
	}

	private Vector3 parseVector(String vectorString) {
		String[] values = vectorString.replaceAll("[{}]", "").split(",");
		return new Vector3(Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]));
	}

	private Color parseColor(String colorString) {
		if (colorString == null)
			return null;
		String[] values = colorString.replaceAll("[{}]", "").split(",");
		int red = Integer.parseInt(values[0].trim());
		int green = Integer.parseInt(values[1].trim());
		int blue = Integer.parseInt(values[2].trim());
		return new Color(red, green, blue);
	}

	private double parseAngle(String angleString) {
		return angleString.endsWith("°") ? Math.toRadians(Double.parseDouble(angleString.replace("°", "")))
				: Double.parseDouble(angleString);
	}

	private Material parseMaterial(String matString, Color color, Color color2, double gridsize, double reflectivity,
			double emission, double shininess) {
		Materials finalMat = Materials.BASIC;
		for (Materials material : Materials.MATERIALS) {
			if (matString.equals(material.getPropertiestring())) {
				finalMat = material;
			}
		}
		Material material = null;
		switch (finalMat) {
		case BASIC:
			material = new BasicMaterial(color);
			material.setReflectivity(reflectivity);
			material.setEmission(emission);
			material.setShininess(shininess);
			break;
		case CHECKER:
			material = new CheckerMaterial(color, color2, gridsize);
			material.setReflectivity(reflectivity);
			material.setEmission(emission);
			material.setShininess(shininess);
			break;
		case SPHEREMAT:
			material = new SphereMaterial();
			material.setReflectivity(reflectivity);
			material.setEmission(emission);
			material.setShininess(shininess);
			break;
		case CUBEMAT:
			material = new CubeMaterial();
			material.setReflectivity(reflectivity);
			material.setEmission(emission);
			material.setShininess(shininess);
			break;
		case WAVE:
			material = new WaveMaterial();
			material.setReflectivity(reflectivity);
			material.setEmission(emission);
			material.setShininess(shininess);
			break;
		case MIRROR:
			material = new MirrorMaterial();
			material.setEmission(emission);
			material.setShininess(shininess);
			break;
		default:
			break;
		}
		return material;
	}
}