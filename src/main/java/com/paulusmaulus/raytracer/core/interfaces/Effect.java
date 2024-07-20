package com.paulusmaulus.raytracer.core.interfaces;

import com.paulusmaulus.raytracer.utilities.data.PixelData;

public interface Effect {
	public String getName();

	public PixelData[][] apply(PixelData[][] pixelBuffer, double resolution);
}
