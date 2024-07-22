package com.paulusmaulus.raytracer.materials;

import com.paulusmaulus.raytracer.core.interfaces.Shader;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Texture;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

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
