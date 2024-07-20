package raytracer.materials;

import raytracer.shaders.Shader;
import raytracer.utilities.Color;
import raytracer.utilities.Material;
import raytracer.utilities.math.Noise;
import raytracer.utilities.math.Vector3;

public class SphereMaterial extends Material {

	Noise noise;

	public SphereMaterial(Shader shader) {
		super(shader, Color.WHITE);
		noise = new Noise();
	}

	/**
	 * Calculates, which side of the {@code Sphere} the {@code Vector3} point is on,
	 * based on relation to {@code Vector3} anchor property. Implemented at
	 * {@code Shape3D}
	 * 
	 * @param point {@code Vector3} point on {@code Sphere} dividided in 8
	 * @return {@code int} side of {@code Sphere} from 0-8
	 */
	public int getSide(Vector3 point) {
		int side = 0;
		side |= (point.x - shape.getAnchor().x >= 0) ? 1 : 0;
		side |= (point.y - shape.getAnchor().y >= 0) ? 2 : 0;
		side |= (point.z - shape.getAnchor().z >= 0) ? 4 : 0;
		return side;
	}

	@Override
	public Color getColor(Vector3 point) {
		Color[] colors = {
				Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.LIGHT_GRAY,
				Color.PINK
		};
		return colors[getSide(point)];
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		return super.getNormal(hitPoint).add(
				new Vector3(noise.noise(hitPoint.x / 10), noise.noise(hitPoint.y / 10), noise.noise(hitPoint.z / 10)));
	}
}