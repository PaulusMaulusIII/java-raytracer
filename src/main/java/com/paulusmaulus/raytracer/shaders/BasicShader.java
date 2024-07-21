package com.paulusmaulus.raytracer.shaders;

import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Scene;
import com.paulusmaulus.raytracer.utilities.math.RayHit;

public class BasicShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, Scene scene, Material material, int depth) {
		return material.getColor(rayHit.getHitPoint());
	}

}
