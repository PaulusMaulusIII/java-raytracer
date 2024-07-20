package com.paulusmaulus.raytracer.shaders;

import java.util.List;

import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.math.RayHit;

public class DistanceShader implements Shader {
	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		return (rayHit.getRay().getOrigin().distance(rayHit.getHitPoint()) * 10 > 360) ? Color.BLACK
				: Color.hsv((int) rayHit.getRay().getOrigin().distance(rayHit.getHitPoint()) * 10, 1, 1);
	}
}
