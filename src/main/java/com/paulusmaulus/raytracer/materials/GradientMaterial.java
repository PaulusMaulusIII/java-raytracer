package com.paulusmaulus.raytracer.materials;

import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;

public class GradientMaterial extends Material {

	public GradientMaterial(Shader shader, Color color1, Color color2) {
		super(shader, color1);
	}

	@Override
	protected String getName() {
		return "Gradient";
	}

}
