package gameboy.materials;

import java.awt.Color;

import gameboy.geometries.Cube;
import gameboy.utilities.math.Vector3;

public class CubeMaterial extends Material {

	public CubeMaterial() {
		super();
	}

	@Override
	public Color getColor(Vector3 point) {
		int side = ((Cube) shape).determineCubeSide(point); // RISKYYYY TODO

		Color[] colors = {
				// x+, x-, y+, y-, z+, z-
				Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PINK, Color.MAGENTA
		};

		if (side >= 0 && side < colors.length) {
			return colors[side];
		}

		return null;
	}
}