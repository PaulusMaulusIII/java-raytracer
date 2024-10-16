package com.paulusmaulus.raytracer.shaders;

import java.util.List;

import com.paulusmaulus.raytracer.core.GlobalSettings;
import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.Scene;
import com.paulusmaulus.raytracer.utilities.data.PixelData;
import com.paulusmaulus.raytracer.utilities.math.RayHit;

public class BloomShader extends PhongShader {

	private double brightnessThreshold = 0.8;
	private double bloomFactor = 1.5;

	@Override
	public Color shade(RayHit rayHit, Scene scene, Material material, int depth) {
		Color baseColor = rayHit.getShape().getMaterial().getColor(rayHit.getHitPoint());

		Color ambientComponent = baseColor.multiply(GlobalSettings.AMBIENT_BRIGHTNESS);

		Color diffuseComponent = new Color(0, 0, 0);
		Color specularComponent = new Color(0, 0, 0);

		List<Light> lights = scene.getLights();
		List<Object3D> objects = scene.getObjects();

		for (Light light : lights) {
			if (!isInShadow(rayHit, light, objects)) {
				double distance = rayHit.getHitPoint().distance(light.getAnchor());
				double attenuation = GlobalSettings.ATTENUATION / (distance * distance);
				Color lightColor = light.getColor().multiply(attenuation);

				double diffuseLighting = calculateDiffuseLighting(rayHit, light);
				double specularLighting = calculateSpecularLighting(rayHit, light, material);

				diffuseComponent = diffuseComponent.add(baseColor.multiply(material.getEmissionAt(rayHit.getHitPoint()))
						.multiply(diffuseLighting).multiply(lightColor));
				specularComponent = specularComponent.add(lightColor.multiply(specularLighting));
			}
		}

		double reflectivity = material.getReflectivityAt(rayHit.getHitPoint());
		diffuseComponent = diffuseComponent.multiply(1 - reflectivity);

		PixelData reflection = calculateReflection(rayHit, scene, material, depth);

		Color finalColor = Color.lerp(ambientComponent, reflection.getColor(), reflectivity) // Reflected color
				.multiply(diffuseComponent) // Diffuse lighting
				.add(specularComponent) // Specular lighting
				.add(baseColor.multiply(material.getEmissionAt(rayHit.getHitPoint()))) // Object emission
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
