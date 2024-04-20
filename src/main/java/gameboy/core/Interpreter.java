package gameboy.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import gameboy.core.enums.Axis;
import gameboy.core.enums.Token;
import gameboy.geometries.Cube;
import gameboy.geometries.Plane;
import gameboy.geometries.Sphere;
import gameboy.lights.Light;
import gameboy.utilities.Camera3D;
import gameboy.utilities.Scene3D;
import gameboy.utilities.Shape3D;
import gameboy.utilities.math.Vector3;
import javafx.scene.paint.Color;

public class Interpreter {

	public Scene3D interpret(String code) {
		List<Camera3D> cameras = new LinkedList<>();
		List<Light> lights = new LinkedList<>();
		List<Shape3D> shapes = new LinkedList<>();
		String[] lines = code.split("\n");

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();

			for (Token initializer : Token.INITIALIZERS) {
				if (line.startsWith(initializer.getTokenString())) {
					HashMap<Token, String> properties = extractProperties(lines, i);
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
					default:
						System.err.println("Not an initializer");
					}
				}
			}
		}
		return new Scene3D(cameras, shapes, lights);
	}

	private HashMap<Token, String> extractProperties(String[] lines, int index) {
		HashMap<Token, String> properties = new HashMap<>();
		for (int i = index + 1; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.equals("}"))
				break;
			String[] propertyString = line.split(":");
			if (propertyString.length == 2) {
				for (Token property : Token.PROPERTIES) {
					if (propertyString[0].trim().equals(property.getTokenString()))
						properties.put(property, propertyString[1].trim());
				}
			}
		}
		return properties;
	}

	private Shape3D createPlane(HashMap<Token, String> properties) {
		Vector3 anchor = parseVector(properties.get(Token.POSITION));
		Axis axis = Axis.valueOf(properties.get(Token.AXIS).toUpperCase());
		Color color = parseColor(properties.get(Token.COLOR));
		return new Plane(anchor, axis, color);
	}

	private Shape3D createCube(HashMap<Token, String> properties) {
		Vector3 center = parseVector(properties.get(Token.POSITION));
		int sideLength = Integer.parseInt(properties.get(Token.SIDELENGTH));
		return new Cube(center, sideLength);
	}

	private Shape3D createSphere(HashMap<Token, String> properties) {
		Vector3 center = parseVector(properties.get(Token.POSITION));
		int radius = Integer.parseInt(properties.get(Token.RADIUS));
		return new Sphere(center, radius);
	}

	private Light createLight(HashMap<Token, String> properties) {
		Vector3 position = parseVector(properties.get(Token.POSITION));
		return new Light(position);
	}

	private Camera3D createCamera(HashMap<Token, String> properties) {
		Vector3 position = parseVector(properties.get(Token.POSITION));
		double fov = parseAngle(properties.getOrDefault(Token.FOV, "40"));
		double pitch = parseAngle(properties.getOrDefault(Token.PITCH, "0"));
		double yaw = parseAngle(properties.getOrDefault(Token.YAW, "0"));
		return new Camera3D(position, fov, pitch, yaw);
	}

	private Vector3 parseVector(String vectorString) {
		String[] values = vectorString.replaceAll("[{}]", "").split(",");
		return new Vector3(Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]));
	}

	private Color parseColor(String colorString) {
		if (colorString == null)
			return null;
		String[] values = colorString.replaceAll("[{}]", "").split(",");
		double red = Integer.parseInt(values[0].trim()) / 255.0;
		double green = Integer.parseInt(values[1].trim()) / 255.0;
		double blue = Integer.parseInt(values[2].trim()) / 255.0;
		return new Color(red, green, blue, 1);
	}

	private double parseAngle(String angleString) {
		return angleString.endsWith("°") ? Math.toRadians(Double.parseDouble(angleString.replace("°", "")))
				: Double.parseDouble(angleString);
	}
}