package gameboy.materials;

import gameboy.utilities.Color;

import gameboy.utilities.Material;
import gameboy.utilities.math.Vector3;

public class CheckerMaterial extends Material {

	double gridsize;
	Color secColor;

	public CheckerMaterial(Color color, Color color2, double gridsize) {
		super(color);
		this.secColor = color2;
		this.gridsize = gridsize;
	}

	public void setSecColor(Color secColor) {
		this.secColor = secColor;
	}

	public Color getSecColor() {
		return secColor;
	}

	@Override
	public Color getColor(Vector3 point) {
		double x = Math.floor(point.x * (1 / gridsize) + 1e-6);
		double y = Math.floor(point.y * (1 / gridsize) + 1e-6);
		double z = Math.floor(point.z * (1 / gridsize) + 1e-6);

		boolean isEven = (x + y + z) % 2 == 0;

		return isEven ? color : secColor;
	}

	public double getGridsize() {
		return gridsize;
	}

	public void setGridsize(double gridsize) {
		this.gridsize = gridsize;
	}
}