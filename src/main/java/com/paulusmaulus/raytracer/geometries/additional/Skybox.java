package com.paulusmaulus.raytracer.geometries.additional;

import java.io.File;
import java.io.IOException;

import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Texture;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Skybox {
	Texture texture;
	Color alternate;

	public Skybox(File file) {
		this(Color.MAGENTA);
		try {
			texture = new Texture(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Skybox(Color alternate) {
		super();
		this.alternate = alternate;
	}

	public Color getColor(Vector3 d) {
		if (texture != null)

			return texture.getColor(d);
		return alternate;
	}
}
