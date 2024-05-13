package gameboy.materials;

import java.awt.Color;

import gameboy.utilities.math.Vector3;

public class CheckerMaterial extends Material {

	double gridsize;

	public CheckerMaterial(Color color, double gridsize) {
		super(color);
		this.gridsize = gridsize;
	}

	@Override
	public Color getColor(Vector3 point) {
		// Calculate the position of the point relative to the checkerboard grid
		int x = (int) Math.floor(point.x / gridsize);
		int y = (int) Math.floor(point.y / gridsize);
		int z = (int) Math.floor(point.z / gridsize);

		// Check if the sum of x, y, and z is even or odd to determine the square color
		boolean isEven = (x + y + z) % 2 == 0;

		// Return either this.color or this.color.darker() based on the square color
		return isEven ? color : color.darker();
	}
}