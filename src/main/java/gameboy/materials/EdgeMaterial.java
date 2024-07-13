package gameboy.materials;

import gameboy.shaders.Shader;
import gameboy.utilities.Color;
import gameboy.utilities.Material;
import gameboy.utilities.math.Vector3;

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
