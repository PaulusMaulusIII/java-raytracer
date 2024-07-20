package com.paulusmaulus.raytracer.shaders;

import java.util.List;

import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.math.RayHit;

public class RainbowShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		double closestDistance = Double.MAX_VALUE;

		for (Light light : lights) {
			double distance = rayHit.getHitPoint().distance(light.getAnchor()) * 2;
			if (distance < closestDistance) {
				closestDistance = distance * 2;
			}
		}

		if (closestDistance > 360) {
			return Color.BLACK;
		}
		return Color.hsv((int) closestDistance, 1, 1);
	}
}
