package gameboy.core;

import gameboy.utilities.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import gameboy.core.enums.Axis;
import gameboy.core.enums.Tokens;
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
		HashMap<Tokens, String> options = new HashMap<>();

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();

			for (Tokens initializer : Tokens.INITIALIZERS) {
				if (line.startsWith(initializer.getTokenString())) {
					HashMap<Tokens, String> properties = extractTokenValues(lines, i);
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
					case OPTIONS:
						options.putAll(properties);
						break;
					default:
						System.err.println("Not an initializer");
					}
				}
			}
		}
		return new Scene(cameras, shapes, lights, options);
	}

	private HashMap<Tokens, String> extractTokenValues(String[] lines, int index) {
		HashMap<Tokens, String> tokenVals = new HashMap<>();
		for (int i = index + 1; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.equals("}"))
				break;
			String[] valString = line.split(":");
			if (valString.length == 2) {
				for (Tokens token : Tokens.PROPERTIES) {
					if (valString[0].trim().equals(token.getTokenString()))
						tokenVals.put(token, valString[1].trim());
				}
			}
		}
		return tokenVals;
	}

	private Shape createPlane(HashMap<Tokens, String> properties) {
		Vector3 anchor = parseVector(properties.get(Tokens.POSITION));
		Axis axis = Axis.valueOf(properties.get(Tokens.AXIS).toUpperCase());
		Color color = parseColor(properties.getOrDefault((Tokens.COLOR), "{255,255,255}"));
		Color color2 = parseColor(properties.getOrDefault(Tokens.SEC_COLOR, "{" + color.toAWT().darker().getRed() + ","
				+ color.toAWT().darker().getGreen() + "," + color.toAWT().darker().getBlue() + "}"));
		double gridsize = Double.parseDouble(properties.getOrDefault(Tokens.GRIDSIZE, "2"));
		double reflectiveness = Double.parseDouble(properties.getOrDefault(Tokens.REFLECTIVENESS, "0"));
		Material material = parseMaterial(properties.get(Tokens.MATERIAL), color, color2, gridsize, reflectiveness);
		return new Plane(anchor, material, axis);
	}

	private Shape createCube(HashMap<Tokens, String> properties) {
		Vector3 center = parseVector(properties.get(Tokens.POSITION));
		double sideLength = Double.parseDouble(properties.get(Tokens.SIDELENGTH));
		Color color = parseColor(properties.getOrDefault((Tokens.COLOR), "{255,255,255}"));
		Color color2 = parseColor(properties.getOrDefault(Tokens.SEC_COLOR, "{" + color.toAWT().darker().getRed() + ","
				+ color.toAWT().darker().getGreen() + "," + color.toAWT().darker().getBlue() + "}"));
		double gridsize = Double.parseDouble(properties.getOrDefault(Tokens.GRIDSIZE, "2"));
		double reflectiveness = Double.parseDouble(properties.getOrDefault(Tokens.REFLECTIVENESS, "0"));
		Material material = parseMaterial(properties.get(Tokens.MATERIAL), color, color2, gridsize, reflectiveness);
		return new Cube(center, material, sideLength);
	}

	private Shape createSphere(HashMap<Tokens, String> properties) {
		Vector3 center = parseVector(properties.get(Tokens.POSITION));
		double radius = Double.parseDouble(properties.get(Tokens.RADIUS));
		Color color = parseColor(properties.getOrDefault((Tokens.COLOR), "{255,255,255}"));
		Color color2 = parseColor(properties.getOrDefault(Tokens.SEC_COLOR, "{" + color.toAWT().darker().getRed() + ","
				+ color.toAWT().darker().getGreen() + "," + color.toAWT().darker().getBlue() + "}"));
		double gridsize = Double.parseDouble(properties.getOrDefault(Tokens.GRIDSIZE, "2"));
		double reflectiveness = Double.parseDouble(properties.getOrDefault(Tokens.REFLECTIVENESS, "0"));
		Material material = parseMaterial(properties.get(Tokens.MATERIAL), color, color2, gridsize, reflectiveness);
		return new Sphere(center, material, radius);
	}

	private Shape createCone(HashMap<Tokens, String> properties) {
		Vector3 center = parseVector(properties.get(Tokens.POSITION));
		Vector3 axis = parseVector(properties.get(Tokens.SIDE));
		double angle = parseAngle(properties.get(Tokens.ANGLE));
		double height = Double.parseDouble(properties.get(Tokens.HEIGHT));
		Color color = parseColor(properties.getOrDefault((Tokens.COLOR), "{255,255,255}"));
		Color color2 = parseColor(properties.getOrDefault(Tokens.SEC_COLOR, "{" + color.toAWT().darker().getRed() + ","
				+ color.toAWT().darker().getGreen() + "," + color.toAWT().darker().getBlue() + "}"));
		double gridsize = Double.parseDouble(properties.getOrDefault(Tokens.GRIDSIZE, "2"));
		double reflectiveness = Double.parseDouble(properties.getOrDefault(Tokens.REFLECTIVENESS, "0"));
		Material material = parseMaterial(properties.get(Tokens.MATERIAL), color, color2, gridsize, reflectiveness);
		return new Cone(center, material, axis, angle, height);
	}

	private Light createLight(HashMap<Tokens, String> properties) {
		Vector3 position = parseVector(properties.get(Tokens.POSITION));
		Color color = parseColor(properties.getOrDefault(Tokens.COLOR, "{255,255,255}"));
		return new Light(position, color);
	}

	private Camera createCamera(HashMap<Tokens, String> properties) {
		Vector3 position = parseVector(properties.get(Tokens.POSITION));
		double fov = parseAngle(properties.getOrDefault(Tokens.FOV, "40°"));
		double pitch = parseAngle(properties.getOrDefault(Tokens.PITCH, "0"));
		double yaw = parseAngle(properties.getOrDefault(Tokens.YAW, "0"));
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

	private Material parseMaterial(String matString, Color color, Color color2, double gridsize,
			double reflectiveness) {
		Tokens finalMat = Tokens.BASIC;
		for (Tokens material : Tokens.MATERIALS) {
			if (matString.equals(material.getTokenString())) {
				finalMat = material;
			}
		}
		Material material = null;
		switch (finalMat) {
		case BASIC:
			material = new BasicMaterial(color);
			material.setReflectiveness(reflectiveness);
			break;
		case CHECKER:
			material = new CheckerMaterial(color, color2, gridsize);
			material.setReflectiveness(reflectiveness);
			break;
		case SPHEREMAT:
			material = new SphereMaterial();
			material.setReflectiveness(reflectiveness);
			break;
		case CUBEMAT:
			material = new CubeMaterial();
			material.setReflectiveness(reflectiveness);
			break;
		case MIRROR:
			material = new MirrorMaterial();
			break;
		default:
			break;
		}
		return material;
	}
}