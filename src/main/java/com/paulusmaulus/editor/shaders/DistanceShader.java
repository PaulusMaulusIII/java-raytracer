package com.paulusmaulus.editor.shaders;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.Scene;
import com.paulusmaulus.editor.utilities.math.RayHit;

public class DistanceShader implements Shader {
	@Override
	public Color shade(RayHit rayHit, Scene scene, Material material, int depth) {
		return (rayHit.getRay().getOrigin().distance(rayHit.getHitPoint()) * 10 > 360) ? Color.BLACK
				: Color.hsv((int) rayHit.getRay().getOrigin().distance(rayHit.getHitPoint()) * 10, 1, 1);
	}
}
