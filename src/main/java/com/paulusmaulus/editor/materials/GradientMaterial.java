package com.paulusmaulus.editor.materials;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;

public class GradientMaterial extends Material {

	public GradientMaterial(Shader shader, Color color1, Color color2) {
		super(shader, color1);
	}

	@Override
	protected String getName() {
		return "Gradient";
	}

}
