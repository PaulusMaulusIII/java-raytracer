package gameboy.shaders;

import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Color;
import gameboy.utilities.GlobalSettings;
import gameboy.utilities.Material;
import gameboy.utilities.Object3D;
import gameboy.utilities.Shape;
import gameboy.utilities.data.PixelData;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class PhongShader implements Shader {
	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material) {
		Color baseColor = rayHit.getShape().getMaterial().getColor(rayHit.getHitPoint());

		Color ambientComponent = baseColor.multiply(GlobalSettings.AMBIENT_BRIGHTNESS);

		Color diffuseComponent = new Color(0, 0, 0);
		Color specularComponent = new Color(0, 0, 0);
		double diffuseFactor = 0;

		for (Light light : lights) {
			if (!isInShadow(rayHit, lights, objects)) {
				double distance = rayHit.getHitPoint().distance(light.getAnchor());
				double attenuation = GlobalSettings.ATTENUATION / (distance * distance);
				Color lightColor = light.getColor().multiply(attenuation);

				double diffuseLighting = calculateDiffuseLighting(rayHit, light);
				double specularLighting = calculateSpecularLighting(rayHit, light, material);
				if (diffuseLighting > diffuseFactor)
					diffuseFactor = diffuseLighting;

				diffuseComponent = diffuseComponent
						.add(baseColor.multiply(material.getEmission()).multiply(diffuseLighting).multiply(lightColor));
				specularComponent = specularComponent.add(lightColor.multiply(specularLighting));
			}
		}

		double reflectivity = material.getReflectivity();
		if (diffuseFactor > 1)
			diffuseFactor = 1;

		PixelData reflection = calculateReflection(rayHit, lights, objects, material);

		Color finalColor = ambientComponent.add(reflection.getColor().multiply(reflectivity)).multiply(diffuseFactor)
				.add(diffuseComponent).add(specularComponent);

		return finalColor;
	}

	protected boolean isInShadow(RayHit rayHit, List<Light> lights, List<Object3D> objects) {
		Vector3 hitPoint = rayHit.getHitPoint();
		for (Light light : lights) {
			Vector3 lightDirection = light.getAnchor().subtract(hitPoint).normalize();
			Ray shadowRay = new Ray(hitPoint.add(lightDirection.scale(1e-6)), lightDirection);

			RayHit hit = shadowRay.cast(objects);

			if (hit != null && hit.getObject() instanceof Shape) {
				return true;
			}
		}
		return false;
	}

	protected double calculateDiffuseLighting(RayHit rayHit, Light light) {
		Vector3 normal = rayHit.getShape().getNormal(rayHit.getHitPoint()).normalize();
		Vector3 lightDirection = light.getAnchor().subtract(rayHit.getHitPoint()).normalize();
		return Math.max(0, normal.dot(lightDirection) * light.getIntensity());
	}

	protected double calculateSpecularLighting(RayHit rayHit, Light light, Material material) {
		Vector3 hitPos = rayHit.getHitPoint();
		Vector3 cameraDirection = rayHit.getRay().getOrigin().subtract(hitPos).normalize();
		Vector3 lightDirection = hitPos.subtract(light.getAnchor()).normalize();
		Vector3 lightReflectionVector = lightDirection
				.subtract(material.getNormal(hitPos).scale(2 * lightDirection.dot(material.getNormal(hitPos))));

		double specularFactor = Math.max(0, Math.min(1, lightReflectionVector.dot(cameraDirection)));
		return (double) Math.pow(specularFactor, 2) * rayHit.getShape().getMaterial().getReflectivity();
	}

	protected PixelData calculateReflection(RayHit rayHit, List<Light> lights, List<Object3D> objects,
			Material material) {
		if (material.getReflectivity() <= 0) {
			return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
		}

		Vector3 hitPoint = rayHit.getHitPoint();
		Vector3 normal = material.getNormal(hitPoint);
		Vector3 incident = rayHit.getRay().getDirection();
		Vector3 reflectedDirection = incident.subtract(normal.scale(2 * incident.dot(normal))).normalize();
		Ray reflectedRay = new Ray(hitPoint.add(reflectedDirection.scale(1e-4)), reflectedDirection);

		RayHit reflectedHit = reflectedRay.cast(objects);
		if (reflectedHit != null && reflectedHit.getObject() instanceof Shape) {
			return new PixelData(
					reflectedHit.getShape().getMaterial().getShader().shade(reflectedHit, lights, objects,
							reflectedHit.getShape().getMaterial()),
					reflectedRay.getOrigin().distance(reflectedHit.getHitPoint()),
					reflectedHit.getShape().getMaterial().getEmission());
		}

		return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
	}
}
