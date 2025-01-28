package com.paulusmaulus.editor.shaders;

import java.util.List;

import com.paulusmaulus.editor.core.GlobalSettings;
import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.geometries.additional.Arrow;
import com.paulusmaulus.editor.lights.Light;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.Object3D;
import com.paulusmaulus.editor.utilities.Scene;
import com.paulusmaulus.editor.utilities.Shape;
import com.paulusmaulus.editor.utilities.data.PixelData;
import com.paulusmaulus.editor.utilities.math.Ray;
import com.paulusmaulus.editor.utilities.math.RayHit;
import com.paulusmaulus.editor.utilities.math.Vector3;

public class PhongShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, Scene scene, Material material, int depth) {
		if (depth > GlobalSettings.MAX_REFLECTION_DEPTH) { // If depth exceeds set
			// max value funtions returns skybox
			// color
			return scene.getSkybox().getColor(rayHit.getRay().getDirection());
		}
		Color baseColor = material.getColor(rayHit.getHitPoint());
		Color ambientComponent = baseColor.multiply(GlobalSettings.AMBIENT_BRIGHTNESS);
		Color diffuseComponent = new Color(0, 0, 0);
		Color specularComponent = new Color(0, 0, 0);
		// double diffuseFactor = 0;
		List<Light> lights = scene.getLights();
		List<Object3D> objects = scene.getObjects();
		for (Light light : lights) {
			if (!isInShadow(rayHit, light, objects)) {
				double distance = rayHit.getHitPoint().distance(light.getAnchor());
				double attenuation = Math.min(1, GlobalSettings.ATTENUATION / (distance *
						distance));
				Color lightColor = light.getColor().multiply(attenuation);
				double diffuseLighting = calculateDiffuseLighting(rayHit, light);
				double specularLighting = calculateSpecularLighting(rayHit, light, material);
				diffuseComponent = diffuseComponent.add(baseColor.multiply(diffuseLighting).multiply(attenuation));
				specularComponent = specularComponent.add(lightColor.multiply(specularLighting));
			}
		}
		double reflectivity = material.getReflectivityAt(rayHit.getHitPoint());
		diffuseComponent = diffuseComponent.multiply(1 - reflectivity);
		specularComponent = specularComponent.multiply(reflectivity);
		PixelData reflection = calculateReflection(rayHit, scene, material, depth);
		Color finalColor = ambientComponent.add(reflection.getColor().multiply(reflectivity)).add(diffuseComponent)
				.add(specularComponent);
		return finalColor;
	}

	protected boolean isInShadow(RayHit rayHit, Light light, List<Object3D> objects) {
		Vector3 hitPoint = rayHit.getHitPoint();
		Vector3 lightDirection = light.getAnchor().subtract(hitPoint).normalize();
		Ray shadowRay = new Ray(hitPoint.add(lightDirection.scale(1e-6)), lightDirection);

		RayHit hit = shadowRay.cast(objects);

		return (hit != null && hit.getObject() instanceof Shape && !(hit.getShape() instanceof Arrow))
				&& hit.getHitPoint().distance(shadowRay.getOrigin()) < rayHit.getHitPoint().distance(light.getAnchor());
	}

	protected double calculateDiffuseLighting(RayHit rayHit, Light light) {
		Vector3 normal = rayHit.getShape().getNormal(rayHit.getHitPoint()).normalize();
		Vector3 lightDirection = light.getAnchor().subtract(rayHit.getHitPoint()).normalize();
		return Math.min(1, Math.max(0, normal.dot(lightDirection) * light.getIntensity()));
	}

	protected double calculateSpecularLighting(RayHit rayHit, Light light, Material material) {
		Vector3 hitPos = rayHit.getHitPoint();
		Vector3 cameraDirection = rayHit.getRay().getOrigin().subtract(hitPos).normalize();
		Vector3 lightDirection = hitPos.subtract(light.getAnchor()).normalize();
		Vector3 lightReflectionVector = lightDirection
				.subtract(material.getNormal(hitPos).scale(2 * lightDirection.dot(material.getNormal(hitPos))));

		double specularFactor = Math.max(0, Math.min(1, lightReflectionVector.dot(cameraDirection)));
		return (double) Math.pow(specularFactor, 2)
				* rayHit.getShape().getMaterial().getReflectivityAt(rayHit.getHitPoint());
	}

	protected PixelData calculateReflection(RayHit rayHit, Scene scene, Material material, int depth) {
		Vector3 hitPoint = rayHit.getHitPoint();
		Vector3 normal = material.getNormal(hitPoint);
		Vector3 incident = rayHit.getRay().getDirection();
		Vector3 reflectedDirection = incident.subtract(normal.scale(2 * incident.dot(normal))).normalize();
		Ray reflectedRay = new Ray(hitPoint.add(reflectedDirection.scale(1e-4)), reflectedDirection);

		if (depth > GlobalSettings.MAX_REFLECTION_DEPTH || material.getReflectivityAt(rayHit.getHitPoint()) <= 0) {
			return new PixelData(scene.getSkybox().getColor(reflectedDirection), Double.POSITIVE_INFINITY,
					GlobalSettings.SKY_EMISSION);
		}

		RayHit reflectedHit = reflectedRay.cast(scene.getObjects());
		if (reflectedHit != null && reflectedHit.getObject() instanceof Shape
				&& !(reflectedHit.getShape() instanceof Arrow)) {
			return new PixelData(
					reflectedHit.getShape().getMaterial().getShader().shade(reflectedHit, scene,
							reflectedHit.getShape().getMaterial(), depth + 1),
					reflectedRay.getOrigin().distance(reflectedHit.getHitPoint())
							+ (rayHit.getHitPoint().distance(rayHit.getRay().getOrigin())),
					reflectedHit.getShape().getMaterial().getEmissionAt(reflectedHit.getHitPoint()));
		}
		return new PixelData(scene.getSkybox().getColor(reflectedDirection), Double.POSITIVE_INFINITY,
				GlobalSettings.SKY_EMISSION);
	}
}
