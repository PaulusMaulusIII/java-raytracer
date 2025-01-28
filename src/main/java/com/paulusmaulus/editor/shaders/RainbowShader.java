package com.paulusmaulus.editor.shaders;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.lights.Light;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.Scene;
import com.paulusmaulus.editor.utilities.math.RayHit;

public class RainbowShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, Scene scene, Material material, int depth) {
		double closestDistance = Double.MAX_VALUE;

		for (Light light : scene.getLights()) {
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
