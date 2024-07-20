package raytracer.post_processing;

import raytracer.core.interfaces.Effect;
import raytracer.utilities.Color;
import raytracer.utilities.data.PixelData;

//TODO
public class Bloom implements Effect {

	@Override
	public PixelData[][] apply(PixelData[][] pixelBuffer, double resolution) {
		int width = pixelBuffer[0].length;
		int height = pixelBuffer.length;
		PixelData[][] bloomBuffer = new PixelData[height][width];

		// Initialize the bloom buffer with original pixel data and calculate total
		// brightness
		double totalBrightness = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				bloomBuffer[y][x] = new PixelData(pixelBuffer[y][x].getColor(), pixelBuffer[y][x].getDistance(),
						pixelBuffer[y][x].getEmission());
				totalBrightness += pixelBuffer[y][x].getColor().getValue();
			}
		}

		double averageBrightness = totalBrightness / (height * width);

		// Precompute spread weights
		int spreadRadius = 3; // Adjust this value for more or less spreading
		double[][] spreadWeights = calculateSpreadWeights(spreadRadius);

		// Apply bloom effect by spreading the bright areas
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color pixelColor = pixelBuffer[y][x].getColor();
				if (pixelColor.getValue() > averageBrightness) {
					spreadBrightness(bloomBuffer, pixelColor, x, y, width, height, spreadRadius, spreadWeights);
				}
			}
		}

		// Combine bloom buffer with original pixel data
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color originalColor = pixelBuffer[y][x].getColor();
				Color bloomColor = bloomBuffer[y][x].getColor();
				Color combinedColor = originalColor.add(bloomColor);
				bloomBuffer[y][x] = new PixelData(combinedColor, pixelBuffer[y][x].getDistance(),
						pixelBuffer[y][x].getEmission());
			}
		}

		return bloomBuffer;
	}

	private void spreadBrightness(PixelData[][] buffer, Color color, int x, int y, int width, int height,
			int spreadRadius, double[][] spreadWeights) {
		for (int dy = -spreadRadius; dy <= spreadRadius; dy++) {
			int ny = y + dy;
			if (ny >= 0 && ny < height) {
				for (int dx = -spreadRadius; dx <= spreadRadius; dx++) {
					int nx = x + dx;
					if (nx >= 0 && nx < width) {
						buffer[ny][nx].getColor()
								.add(color.multiply(spreadWeights[dy + spreadRadius][dx + spreadRadius]));
					}
				}
			}
		}
	}

	private double[][] calculateSpreadWeights(int radius) {
		double[][] weights = new double[2 * radius + 1][2 * radius + 1];
		for (int dy = -radius; dy <= radius; dy++) {
			for (int dx = -radius; dx <= radius; dx++) {
				double distance = Math.sqrt(dx * dx + dy * dy);
				weights[dy + radius][dx + radius] = 1.0 / (distance + 1); // Decrease the intensity with distance
			}
		}
		return weights;
	}

	@Override
	public String getName() {
		return "Bloom";
	}
}
