package raytracer.materials;

import raytracer.shaders.Shader;
import raytracer.utilities.Color;
import raytracer.utilities.Material;
import raytracer.utilities.math.Vector3;

public class EdgeMaterial extends Material {

	private double edgeThreshold;

	public EdgeMaterial(Shader shader, Color color, double edgeThreshold) {
		super(shader, color);
		this.edgeThreshold = edgeThreshold;
	}

	@Override
	public Color getColor(Vector3 point) {
		if (shape != null) {
			double distance = shape.distanceToEdge(point);
			if (distance < edgeThreshold) {
				return color; // White
			}
		}
		return Color.BLACK; // Black
	}
}
