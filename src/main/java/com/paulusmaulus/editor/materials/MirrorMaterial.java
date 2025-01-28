package com.paulusmaulus.editor.materials;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;

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
