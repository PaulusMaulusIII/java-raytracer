package gameboy.shaders;

import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Color;
import gameboy.utilities.Material;
import gameboy.utilities.Object3D;
import gameboy.utilities.math.RayHit;

public class BloomShader implements Shader {

	private double brightnessThreshold = 0.8;
	private double bloomFactor = 1.5;

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material) {
		Color baseColor = rayHit.getShape().getMaterial().getColor(rayHit.getHitPoint());

		double brightness = (baseColor.getRed() + baseColor.getGreen() + baseColor.getBlue()) / 3.0;

		if (brightness > brightnessThreshold) {
			baseColor = baseColor.multiply(bloomFactor);
		}

		return baseColor;
	}

	public void setBrightnessThreshold(double brightnessThreshold) {
		this.brightnessThreshold = brightnessThreshold;
	}

	public double getBrightnessThreshold() {
		return brightnessThreshold;
	}

	public void setBloomFactor(double bloomFactor) {
		this.bloomFactor = bloomFactor;
	}

	public double getBloomFactor() {
		return bloomFactor;
	}
}
