package com.paulusmaulus.raytracer.materials;

import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;

public class BasicMaterial extends Material {

	public BasicMaterial(Shader shader, Color color) {
		super(shader, color);
	}

	@Override
	protected String getName() {
		return "Basic";
	}

}
