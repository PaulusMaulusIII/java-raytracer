package gameboy.core.enums;

import java.util.LinkedList;
import java.util.List;

public enum Properties {
	// Properties
	POSITION("position"), SIDELENGTH("sidelength"), COLOR("color"), MATERIAL("material"), AXIS("axis"), FOV("fov"),
	RADIUS("radius"), PITCH("pitch"), YAW("yaw"), SIDE("side"), ANGLE("angle"), HEIGHT("height"), GRIDSIZE("gridsize"),
	SEC_COLOR("color2"), REFLECTIVENESS("refl"), SHADE("shade");

	private final String tokenString;

	private Properties(String tokenString) {
		this.tokenString = tokenString;
	}

	public String getTokenString() {
		return tokenString;
	}

	public static final List<Properties> PROPERTIES = new LinkedList<>(List.of(POSITION, SIDELENGTH, COLOR, MATERIAL,
			AXIS, FOV, RADIUS, PITCH, YAW, SHADE, SIDE, ANGLE, HEIGHT, GRIDSIZE, SEC_COLOR, REFLECTIVENESS));
}
