package gameboy.post_processing;

import gameboy.utilities.data.PixelData;

public interface Effect {
	public String getName();

	public PixelData[][] apply(PixelData[][] pixelBuffer, double resolution);
}
