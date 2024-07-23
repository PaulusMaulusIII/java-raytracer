package com.paulusmaulus.raytracer.utilities;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.paulusmaulus.raytracer.utilities.math.Vector3;

import java.io.File;
import java.io.IOException;

public class Texture {
	BufferedImage image;

	public Texture() {}

	public Texture(File file) throws IOException {
		this(ImageIO.read(file));
	}

	public Texture(BufferedImage image) {
		this.image = image;
	}

	public Color getColor(Vector3 d) {
		if (image == null)
			return Color.BLACK;

		float u = (float) (0.5 + Math.atan2(d.z, d.x) / (2 * Math.PI));
		float v = (float) (0.5 - Math.asin(d.y) / Math.PI);
		try {
			return Color.fromInt(image.getRGB((int) (u * (image.getWidth() - 1)), (int) (v * (image.getHeight() - 1))));
		} catch (Exception e) {
			return Color.MAGENTA;
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
