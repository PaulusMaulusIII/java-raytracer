package com.paulusmaulus.editor.shaders;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.Scene;
import com.paulusmaulus.editor.utilities.math.RayHit;

public class BasicShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, Scene scene, Material material, int depth) {
		return material.getColor(rayHit.getHitPoint());
	}

}
