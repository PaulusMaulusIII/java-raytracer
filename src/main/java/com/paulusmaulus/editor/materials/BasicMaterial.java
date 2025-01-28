package com.paulusmaulus.editor.materials;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;

public class BasicMaterial extends Material {

	public BasicMaterial(Shader shader, Color color) {
		super(shader, color);
	}

	@Override
	protected String getName() {
		return "Basic";
	}

}
