package gameboy.materials;

import java.awt.Color;

import gameboy.utilities.math.Vector3;

public class CheckerMaterial extends Material {

	double gridsize;
	Color color2;

	public CheckerMaterial(Color color, Color color2, double gridsize) {
		super();
		this.gridsize = gridsize;
		this.color = color;
		this.color2 = color2;
	}

	@Override
	public Color getColor(Vector3 point) {
		int x = (int) Math.floor(point.x / gridsize);
		int y = (int) Math.floor(point.y / gridsize);
		int z = (int) Math.floor(point.z / gridsize);

		boolean isEven = (x + y + z) % 2 == 0;

		return isEven ? color : color2;
	}
}