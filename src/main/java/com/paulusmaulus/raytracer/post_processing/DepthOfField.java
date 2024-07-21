package com.paulusmaulus.raytracer.post_processing;

import com.paulusmaulus.raytracer.core.GlobalSettings;
import com.paulusmaulus.raytracer.core.interfaces.Effect;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.data.PixelData;

public class DepthOfField implements Effect {

	public interface DistanceGetter {
		public double getDistance();
	}

	private DistanceGetter getter;

	public DepthOfField(DistanceGetter distanceGetter) {
		super();
		this.getter = distanceGetter;
	}

	@Override
	public PixelData[][] apply(PixelData[][] pixelBuffer, double resolution) {
		double distance = getter.getDistance();
		int resHeight = pixelBuffer.length;
		int resWidth = pixelBuffer[0].length;
		PixelData[][] blurBuffer = new PixelData[resHeight][resWidth];
		int blurRadius = 0;

		for (int y = 0; y < resHeight; y++) {
			for (int x = 0; x < resWidth; x++) {
				int r = 0, g = 0, b = 0, count = 0;
				blurRadius = (int) (Math.abs(((distance - pixelBuffer[y][x].getDistance()))) * resolution);
				if (blurRadius > GlobalSettings.DOF_MAX_STRENGHT)
					blurRadius = GlobalSettings.DOF_MAX_STRENGHT;
				if (blurRadius >= 1) {
					for (int dy = -blurRadius; dy <= blurRadius; dy++) {
						for (int dx = -blurRadius; dx <= blurRadius; dx++) {
							try {
								PixelData p = pixelBuffer[y + dy][x + dx];
								r += p.getColor().getRed();
								g += p.getColor().getGreen();
								b += p.getColor().getBlue();
								count++;
							} catch (Exception e) {
							}
						}
					}
					r /= count;
					g /= count;
					b /= count;
					blurBuffer[y][x] = new PixelData(new Color(r, g, b), pixelBuffer[y][x].getDistance(),
							pixelBuffer[y][x].getEmission());
				}
			}
		}

		for (int y = 0; y < blurBuffer.length; y++)
			for (int x = 0; x < blurBuffer[0].length; x++)
				if (blurBuffer[y][x] == null)
					blurBuffer[y][x] = pixelBuffer[y][x];

		return blurBuffer;
	}

	@Override
	public String getName() {
		return "Depth of field";
	}
}
