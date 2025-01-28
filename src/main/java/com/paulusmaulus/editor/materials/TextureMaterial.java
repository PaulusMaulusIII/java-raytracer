package com.paulusmaulus.editor.materials;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.Texture;
import com.paulusmaulus.editor.utilities.math.Vector3;

public class TextureMaterial extends Material {
	Texture texture;

	public TextureMaterial(Texture texture, Shader shader) {
		super(shader, Color.BLACK);
		this.texture = texture;
	}

	@Override
	public Color getColor(Vector3 point) {
		return texture.getColor(point.subtract(shape.getAnchor()));
	}

	public Texture getTexture() {
		return texture;
	}

	@Override
	protected String getName() {
		return "Texture";
	}
}
