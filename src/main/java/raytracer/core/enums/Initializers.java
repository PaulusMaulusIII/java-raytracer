package raytracer.core.enums;

import java.util.List;

public enum Initializers {
	// Shapes
	CUBE("Cube"), LINE("Line"), PLANE("Plane"), SPHERE("Sphere"), CONE("Cone"), CAMERA("Camera"), LIGHT("Light");

	private final String Propertiestring;

	private Initializers(String Propertiestring) {
		this.Propertiestring = Propertiestring;
	}

	public String getPropertiestring() {
		return Propertiestring;
	}

	public static final List<Initializers> INITIALIZERS = List.of(CUBE, LINE, PLANE, SPHERE, CONE, CAMERA, LIGHT);
}
