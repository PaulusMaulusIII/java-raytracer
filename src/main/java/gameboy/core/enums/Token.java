package gameboy.core.enums;

import java.util.LinkedList;
import java.util.List;

public enum Token {

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

	//Assets
	CAMERA("Camera"),
	LIGHT("Light"),

	//Shapes
	CUBE("Cube"),
	LINE("Line"),
	PLANE("Plane"),
	SPHERE("Sphere");

	private final String tokenString;

	private Token(String tokenString) {
		this.tokenString = tokenString;
	}

	public String getTokenString() {
		return tokenString;
	}

	public static final List<Token> SHAPES = new LinkedList<>(
		List.of(
			CUBE, LINE, PLANE, SPHERE
		)
	);

	public static final List<Token> PROPERTIES = new LinkedList<>(
		List.of(
			POSITION, SIDELENGTH, COLOR, MATERIAL, AXIS, FOV, RADIUS, PITCH, YAW
		)
	);

	public static final List<Token> ASSETS = new LinkedList<>(
		List.of(
			CAMERA,LIGHT
		)
	);

	public static final List<Token> INITIALIZERS = new LinkedList<>(
		List.of(	
			CUBE, LINE, PLANE, SPHERE,CAMERA,LIGHT
		)
	);
}