package com.paulusmaulus.editor.materials;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.math.Vector3;

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

	@Override
	protected String getName() {
		return "Edge";
	}
}
