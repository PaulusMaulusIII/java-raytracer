package gameboy.materials;

import gameboy.utilities.Color;

import gameboy.geometries.Cube;
import gameboy.utilities.Material;
import gameboy.utilities.math.Vector3;

public class CubeMaterial extends Material {

	public CubeMaterial() {
		super(Color.WHITE);
	}

	@Override
	public Color getColor(Vector3 point) {
		int side = ((Cube) shape).determineCubeSide(point); // TODO RISKYYYY

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