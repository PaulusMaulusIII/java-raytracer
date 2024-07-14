package gameboy.shaders;

import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Color;
import gameboy.utilities.GlobalSettings;
import gameboy.utilities.Material;
import gameboy.utilities.Object3D;
import gameboy.utilities.data.PixelData;
import gameboy.utilities.math.RayHit;

public class BloomShader extends PhongShader {

	private double brightnessThreshold = 0.8;
	private double bloomFactor = 1.5;

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		Color baseColor = rayHit.getShape().getMaterial().getColor(rayHit.getHitPoint());

		Color ambientComponent = baseColor.multiply(GlobalSettings.AMBIENT_BRIGHTNESS);

		Color diffuseComponent = new Color(0, 0, 0);
		Color specularComponent = new Color(0, 0, 0);

		for (Light light : lights) {
			if (!isInShadow(rayHit, lights, objects)) {
				double distance = rayHit.getHitPoint().distance(light.getAnchor());
				double attenuation = GlobalSettings.ATTENUATION / (distance * distance);
				Color lightColor = light.getColor().multiply(attenuation);

				double diffuseLighting = calculateDiffuseLighting(rayHit, light);
				double specularLighting = calculateSpecularLighting(rayHit, light, material);

				diffuseComponent = diffuseComponent
						.add(baseColor.multiply(material.getEmission()).multiply(diffuseLighting).multiply(lightColor));
				specularComponent = specularComponent.add(lightColor.multiply(specularLighting));
			}
		}

		double reflectivity = material.getReflectivity();
		diffuseComponent = diffuseComponent.multiply(1 - reflectivity);

		PixelData reflection = calculateReflection(rayHit, lights, objects, material, depth);

		Color finalColor = Color.lerp(ambientComponent, reflection.getColor(), reflectivity) // Reflected color
				.multiply(diffuseComponent) // Diffuse lighting
				.add(specularComponent) // Specular lighting
				.add(baseColor.multiply(material.getEmission())) // Object emission
				.add(reflection.getColor().multiply(reflection.getEmission() * reflectivity)); // Indirect illumination

		return finalColor;
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
