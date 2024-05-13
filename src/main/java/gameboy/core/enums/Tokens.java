package gameboy.core.enums;

import java.util.LinkedList;
import java.util.List;

public enum Tokens {

	//Properties
	POSITION("position"),
	SIDELENGTH("sidelength"),
	COLOR("color"),
	MATERIAL("material"),
	AXIS("axis"),
	FOV("fov"),
	RADIUS("radius"),
	PITCH("pitch"),
	YAW("yaw"),
	SIDE("side"),
	ANGLE("angle"),
	HEIGHT("height"),
	GRIDSIZE("gridsize"),
	SEC_COLOR("color2"),
	REFLECTIVENESS("refl"),

	//Options
	SHADE("shade"),

	//Assets
	CAMERA("Camera"),
	LIGHT("Light"),

	//Shapes
	OPTIONS("Options"),
	CUBE("Cube"),
	LINE("Line"),
	PLANE("Plane"),
	SPHERE("Sphere"),
	CONE("Cone"),

	//Materials
	CHECKER("checker"),
	MIRROR("mirror"),
	CUBEMAT("cube"),
	SPHEREMAT("sphere"),
	BASIC("basic");

	private final String tokenString;

	private Tokens(String tokenString) {
		this.tokenString = tokenString;
	}

	public String getTokenString() {
		return tokenString;
	}

	public static final List<Tokens> SHAPES = new LinkedList<>(
		List.of(
			CUBE, LINE, PLANE, SPHERE, CONE
		)
	);

	public static final List<Tokens> PROPERTIES = new LinkedList<>(
		List.of(
			POSITION, SIDELENGTH, COLOR, MATERIAL, AXIS, FOV, RADIUS, PITCH, YAW, SHADE, SIDE, ANGLE, HEIGHT, GRIDSIZE, SEC_COLOR, REFLECTIVENESS
		)
	);

	public static final List<Tokens> ASSETS = new LinkedList<>(
		List.of(
			CAMERA,LIGHT
		)
	);

	public static final List<Tokens> INITIALIZERS = new LinkedList<>(
		List.of(	
			CUBE, LINE, PLANE, SPHERE, CONE, CAMERA, LIGHT, OPTIONS
		)
	);

	public static final List<Tokens> MATERIALS = new LinkedList<>(
		List.of(	
			CHECKER, MIRROR, CUBEMAT, SPHEREMAT, BASIC
		)
	);
}