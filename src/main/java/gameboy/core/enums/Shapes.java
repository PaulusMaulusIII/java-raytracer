package gameboy.core.enums;

import java.util.LinkedList;
import java.util.List;

public enum Shapes {

	// Shapes
	CUBE("Cube"), LINE("Line"), PLANE("Plane"), SPHERE("Sphere"), CONE("Cone");

	private final String propertyString;

	private Shapes(String propertyString) {
		this.propertyString = propertyString;
	}

	public String getPropertyString() {
		return propertyString;
	}

	public static final List<Shapes> Shapes = new LinkedList<>(List.of(CUBE, LINE, PLANE, SPHERE, CONE));
}
