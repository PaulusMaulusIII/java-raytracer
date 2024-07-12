package gameboy.shaders;

import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Color;
import gameboy.utilities.GlobalSettings;
import gameboy.utilities.Material;
import gameboy.utilities.Object3D;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class PhongShader implements Shader {

	protected double emission = 1;
	protected double shininess = 128;

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material) {
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
				double specularLighting = calculateSpecularLighting(rayHit, light);

				diffuseComponent = diffuseComponent
						.add(baseColor.multiply(emission).multiply(diffuseLighting).multiply(lightColor));
				specularComponent = specularComponent.add(lightColor.multiply(specularLighting));
			}
		}

		double reflectivity = material.getReflectivity();
		diffuseComponent = diffuseComponent.multiply(1 - reflectivity);

		Color reflectionComponent = calculateReflection(rayHit, lights, objects, material);

		Color finalColor = ambientComponent.add(diffuseComponent).add(specularComponent)
				.add(reflectionComponent.multiply(reflectivity));

		return finalColor;
	}

	private boolean isInShadow(RayHit rayHit, List<Light> lights, List<Object3D> objects) {
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

	private double calculateDiffuseLighting(RayHit rayHit, Light light) {
		Vector3 normal = rayHit.getShape().getNormal(rayHit.getHitPoint()).normalize();
		Vector3 lightDirection = light.getAnchor().subtract(rayHit.getHitPoint()).normalize();
		return Math.max(0, normal.dot(lightDirection) * light.getIntensity());
	}

	private double calculateSpecularLighting(RayHit rayHit, Light light) {
		Vector3 viewDirection = rayHit.getRay().getDirection().normalize();
		Vector3 lightDirection = light.getAnchor().subtract(rayHit.getHitPoint()).normalize();
		Vector3 normal = rayHit.getShape().getNormal(rayHit.getHitPoint()).normalize();
		Vector3 reflectDirection = lightDirection.subtract(normal.scale(2 * lightDirection.dot(normal))).normalize();

		double specularStrength = GlobalSettings.SPECULAR_STRENGTH * light.getIntensity();
		double specularFactor = Math.pow(Math.max(0, viewDirection.dot(reflectDirection)), shininess);

		return specularStrength * specularFactor;
	}

	private Color calculateReflection(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material) {
		if (material.getReflectivity() <= 0) {
			return GlobalSettings.SKY_BOX_COLOR;
		}

		Vector3 hitPoint = rayHit.getHitPoint();
		Vector3 normal = material.getNormal(hitPoint);
		Vector3 incident = rayHit.getRay().getDirection();
		Vector3 reflectedDirection = incident.subtract(normal.scale(2 * incident.dot(normal))).normalize();
		Ray reflectedRay = new Ray(hitPoint.add(reflectedDirection.scale(1e-4)), reflectedDirection);

		RayHit reflectedHit = reflectedRay.cast(objects);
		if (reflectedHit != null && reflectedHit.getObject() instanceof Shape) {
			Material reflectedMaterial = reflectedHit.getShape().getMaterial();
			Color reflectedColor = reflectedMaterial.getShader().shade(reflectedHit, lights, objects,
					reflectedMaterial);
			return material.getColor(hitPoint).interpolate(reflectedColor, material.getReflectivity());
		}

		return GlobalSettings.SKY_BOX_COLOR;
	}

	public void setEmission(double emission) {
		this.emission = emission;
	}

	public double getEmission() {
		return emission;
	}

	public void setShininess(double shininess) {
		this.shininess = shininess;
	}

	public double getShininess() {
		return shininess;
	}
}
