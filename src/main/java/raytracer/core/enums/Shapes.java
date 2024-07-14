package raytracer.core.enums;

import java.util.LinkedList;
import java.util.List;

public enum Shapes {

	// Shapes
	CUBE("Cube", List.of(Properties.POSITION, Properties.SIDELENGTH)),
	LINE("Line", List.of(Properties.POSITION, Properties.AXIS)),
	PLANE("Plane", List.of(Properties.POSITION, Properties.AXIS)),
	SPHERE("Sphere", List.of(Properties.POSITION, Properties.RADIUS)),
	CONE("Cone", List.of(Properties.POSITION, Properties.HEIGHT, Properties.ANGLE, Properties.AXIS));

	private final String propertyString;
	private final List<Properties> properties;

	private Shapes(String propertyString, List<Properties> properties) {
		this.propertyString = propertyString;
		this.properties = properties;
	}

	public String getPropertyString() {
		return propertyString;
	}

	public List<Properties> getProperties() {
		return properties;
	}

	public static final List<Shapes> Shapes = new LinkedList<>(List.of(CUBE, LINE, PLANE, SPHERE, CONE));
}
