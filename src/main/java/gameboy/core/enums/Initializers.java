package gameboy.core.enums;

import java.util.LinkedList;
import java.util.List;

public enum Initializers {
	// Shapes
	OPTIONS("Options"), CUBE("Cube"), LINE("Line"), PLANE("Plane"), SPHERE("Sphere"), CONE("Cone"), CAMERA("Camera"),
	LIGHT("Light");

	private final String Propertiestring;

	private Initializers(String Propertiestring) {
		this.Propertiestring = Propertiestring;
	}

	public String getPropertiestring() {
		return Propertiestring;
	}

	public static final List<Initializers> INITIALIZERS = new LinkedList<>(
			List.of(CUBE, LINE, PLANE, SPHERE, CONE, CAMERA, LIGHT, OPTIONS));
}
