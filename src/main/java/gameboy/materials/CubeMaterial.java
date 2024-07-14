package gameboy.materials;

import gameboy.shaders.Shader;
import gameboy.utilities.Color;
import gameboy.utilities.Material;
import gameboy.utilities.math.Vector3;

public class CubeMaterial extends Material {

	public CubeMaterial(Shader shader) {
		super(shader, Color.WHITE);
	}

	@Override
	public Color getColor(Vector3 point) {
		int side = -1;
		double[] pointArray = point.toArray();
		double[] anchorArray = shape.getAnchor().toArray();

		double maxDistance = 0;
		int maxAxis = -1;
		for (int i = 0; i < 3; i++) {
			double distance = Math.abs(pointArray[i] - anchorArray[i]);
			if (distance > maxDistance) {
				maxDistance = distance;
				maxAxis = i;
			}
		}

		if (maxAxis == 0) {
			side = pointArray[0] > anchorArray[0] ? 0 : 1;
		}
		else if (maxAxis == 1) {
			side = pointArray[1] > anchorArray[1] ? 2 : 3;
		}
		else {
			side = pointArray[2] > anchorArray[2] ? 4 : 5;
		}

		Color[] colors = {
				// x+, x-, y+, y-, z+, z-
				Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PINK, Color.MAGENTA
		};

		if (side >= 0 && side < colors.length)
			return colors[side];
		return null;
	}
}