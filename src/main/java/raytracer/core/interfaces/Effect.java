package raytracer.core.interfaces;

import raytracer.utilities.data.PixelData;

public interface Effect {
	public String getName();

	public PixelData[][] apply(PixelData[][] pixelBuffer, double resolution);
}
