package com.paulusmaulus.raytracer.materials;

import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

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
