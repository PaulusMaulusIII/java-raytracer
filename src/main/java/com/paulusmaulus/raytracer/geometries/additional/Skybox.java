package com.paulusmaulus.raytracer.geometries.additional;

import java.io.File;
import java.io.IOException;

import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Texture;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Skybox {
	Texture texture;

	public Skybox(File file) {
		super();
		try {
			texture = new Texture(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Color getColor(Vector3 d) {
		return texture.getColor(d);
	}
}
