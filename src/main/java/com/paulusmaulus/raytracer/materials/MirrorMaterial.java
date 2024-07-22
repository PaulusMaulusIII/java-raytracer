package com.paulusmaulus.raytracer.materials;

import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;

public class MirrorMaterial extends Material {
	public MirrorMaterial(Shader shader) {
		super(shader, Color.BLACK);
		reflectivity = 1;
	}

	@Override
	protected String getName() {
		return "Mirror";
	}
}
