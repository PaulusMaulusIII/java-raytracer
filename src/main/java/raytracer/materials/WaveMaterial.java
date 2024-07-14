package raytracer.materials;

import raytracer.shaders.Shader;
import raytracer.utilities.Color;
import raytracer.utilities.Material;
import raytracer.utilities.math.Vector3;

public class WaveMaterial extends Material {

	private long time;
	private Vector3 normal = new Vector3(1, 2, 0);

	public WaveMaterial(Shader shader) {
		super(shader, Color.WHITE);
		time = System.currentTimeMillis();
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		if (System.currentTimeMillis() - time > 1) {
			normal = normal.rotate(0, Math.toRadians(1), 0); // TODO specular !!!!!!!!!!
			time = System.currentTimeMillis();
		}
		return normal;
	}
}
