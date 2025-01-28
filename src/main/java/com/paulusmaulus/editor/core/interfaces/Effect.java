package com.paulusmaulus.editor.core.interfaces;

import com.paulusmaulus.editor.utilities.data.PixelData;

public interface Effect {
	public String getName();

	public PixelData[][] apply(PixelData[][] pixelBuffer, double resolution);
}
