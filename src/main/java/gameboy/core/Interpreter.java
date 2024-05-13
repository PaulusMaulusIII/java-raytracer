package gameboy.core;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import gameboy.core.enums.Axis;
import gameboy.core.enums.Token;
import gameboy.geometries.Cone;
import gameboy.geometries.Cube;
import gameboy.geometries.Plane;
import gameboy.geometries.Sphere;
import gameboy.lights.Light;
import gameboy.utilities.Camera;
import gameboy.utilities.Scene;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Vector3;

public class Interpreter {

	public Scene interpret(String code) {
		List<Camera> cameras = new LinkedList<>();
		List<Light> lights = new LinkedList<>();
		List<Shape> shapes = new LinkedList<>();
		String[] lines = code.split("\n");
		HashMap<Token, String> options = new HashMap<>();

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();

			for (Token initializer : Token.INITIALIZERS) {
				if (line.startsWith(initializer.getTokenString())) {
					HashMap<Token, String> properties = extractTokenValues(lines, i);
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

	private HashMap<Token, String> extractTokenValues(String[] lines, int index) {
		HashMap<Token, String> tokenVals = new HashMap<>();
		for (int i = index + 1; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.equals("}"))
				break;
			String[] valString = line.split(":");
			if (valString.length == 2) {
				for (Token token : Token.PROPERTIES) {
					if (valString[0].trim().equals(token.getTokenString()))
						tokenVals.put(token, valString[1].trim());
				}
			}
		}
		return tokenVals;
	}

	private Shape createPlane(HashMap<Token, String> properties) {
		Vector3 anchor = parseVector(properties.get(Token.POSITION));
		Axis axis = Axis.valueOf(properties.get(Token.AXIS).toUpperCase());
		Color color = parseColor(properties.get(Token.COLOR));
		return new Plane(anchor, axis, color);
	}

	private Shape createCube(HashMap<Token, String> properties) {
		Vector3 center = parseVector(properties.get(Token.POSITION));
		double sideLength = Double.parseDouble(properties.get(Token.SIDELENGTH));
		return new Cube(center, sideLength);
	}

	private Shape createSphere(HashMap<Token, String> properties) {
		Vector3 center = parseVector(properties.get(Token.POSITION));
		double radius = Double.parseDouble(properties.get(Token.RADIUS));
		return new Sphere(center, radius);
	}

	private Shape createCone(HashMap<Token, String> properties) {
		Vector3 center = parseVector(properties.get(Token.POSITION));
		Vector3 axis = parseVector(properties.get(Token.SIDE));
		double angle = parseAngle(properties.get(Token.ANGLE));
		double height = Double.parseDouble(properties.get(Token.HEIGHT));
		Color color = parseColor(properties.get(Token.COLOR));
		return new Cone(center, axis, angle, height, color);
	}

	private Light createLight(HashMap<Token, String> properties) {
		Vector3 position = parseVector(properties.get(Token.POSITION));
		Color color = parseColor(properties.getOrDefault(Token.COLOR, "{255,255,255}"));
		return new Light(position, color);
	}

	private Camera createCamera(HashMap<Token, String> properties) {
		Vector3 position = parseVector(properties.get(Token.POSITION));
		double fov = parseAngle(properties.getOrDefault(Token.FOV, "40°"));
		double pitch = parseAngle(properties.getOrDefault(Token.PITCH, "0"));
		double yaw = parseAngle(properties.getOrDefault(Token.YAW, "0"));
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
}