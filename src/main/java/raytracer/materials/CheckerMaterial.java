package raytracer.materials;

import raytracer.core.interfaces.Shader;
import raytracer.utilities.Color;
import raytracer.utilities.Material;
import raytracer.utilities.math.Noise;
import raytracer.utilities.math.Vector3;

public class CheckerMaterial extends Material {

	double gridsize;
	Color secColor;
	Noise noise;

	public CheckerMaterial(Shader shader, Color color, Color color2, double gridsize) {
		super(shader, color);
		this.secColor = color2;
		this.gridsize = gridsize;
		noise = new Noise();
	}

	public void setSecColor(Color secColor) {
		this.secColor = secColor;
	}

	public Color getSecColor() {
		return secColor;
	}

	@Override
	public Color getColor(Vector3 point) {
		Vector3 pointRelative = shape.getAnchor().subtract(point);
		double x = Math.floor(pointRelative.x * (1 / gridsize) + 1e-6);
		double y = Math.floor(pointRelative.y * (1 / gridsize) + 1e-6);
		double z = Math.floor(pointRelative.z * (1 / gridsize) + 1e-6);

		boolean isEven = (x + y + z) % 2 == 0;

		return isEven ? color : secColor;
	}

	public double getGridsize() {
		return gridsize;
	}

	public void setGridsize(double gridsize) {
		this.gridsize = gridsize;
	}

	@Override
	public double getReflectivityAt(Vector3 point) {
		return noise.smoothNoise(point.x, point.y, point.z) / 10;
	}

}