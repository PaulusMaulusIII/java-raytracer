package raytracer.post_processing;

import raytracer.utilities.data.PixelData;

public interface Effect {
	public PixelData[][] apply(PixelData[][] pixelBuffer, double resolution);

}
