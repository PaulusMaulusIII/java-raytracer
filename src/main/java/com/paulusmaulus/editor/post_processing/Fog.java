package com.paulusmaulus.editor.post_processing;

import com.paulusmaulus.editor.core.interfaces.Effect;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.data.PixelData;

public class Fog implements Effect {

	@Override
	public PixelData[][] apply(PixelData[][] pixelBuffer, double resolution) {
		for (int y = 0; y < pixelBuffer.length; y++) {
			for (int x = 0; x < pixelBuffer[0].length; x++) {
				pixelBuffer[y][x] = new PixelData(
						pixelBuffer[y][x].getColor().interpolate(Color.DARK_GRAY,
								pixelBuffer[y][x].getDistance() / 100),
						pixelBuffer[y][x].getDistance(), pixelBuffer[y][x].getEmission());
			}
		}
		return pixelBuffer;
	}

	@Override
	public String getName() {
		return "Fog";
	}

}
