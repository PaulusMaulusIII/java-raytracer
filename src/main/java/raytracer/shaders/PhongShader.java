package raytracer.shaders;

import java.util.List;

import raytracer.lights.Light;
import raytracer.utilities.Color;
import raytracer.utilities.GlobalSettings;
import raytracer.utilities.Material;
import raytracer.utilities.Object3D;
import raytracer.utilities.Shape;
import raytracer.utilities.data.PixelData;
import raytracer.utilities.math.Ray;
import raytracer.utilities.math.RayHit;
import raytracer.utilities.math.Vector3;

public class PhongShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		if (depth > GlobalSettings.MAX_REFLECTION_DEPTH) {
			return GlobalSettings.SKY_BOX_COLOR;
		}

		Color baseColor = material.getColor(rayHit.getHitPoint());
		Color ambientComponent = baseColor.multiply(GlobalSettings.AMBIENT_BRIGHTNESS);

		Color diffuseComponent = new Color(0, 0, 0);
		Color specularComponent = new Color(0, 0, 0);
		double diffuseFactor = 0;

		for (Light light : lights) {
			if (!isInShadow(rayHit, light, objects)) {
				double distance = rayHit.getHitPoint().distance(light.getAnchor());
				double attenuation = GlobalSettings.ATTENUATION / (distance * distance);
				Color lightColor = light.getColor().multiply(attenuation);

				double diffuseLighting = calculateDiffuseLighting(rayHit, light);
				double specularLighting = calculateSpecularLighting(rayHit, light, material);

				diffuseFactor = Math.max(diffuseFactor, diffuseLighting);

				diffuseComponent = diffuseComponent.add(baseColor.multiply(diffuseLighting).multiply(lightColor));
				specularComponent = specularComponent.add(lightColor.multiply(specularLighting));
			}
		}

		diffuseFactor = Math.min(1, diffuseFactor);
		double reflectivity = material.getReflectivityAt(rayHit.getHitPoint());

		diffuseComponent = diffuseComponent.multiply(1 - reflectivity);
		specularComponent = specularComponent.multiply(reflectivity);

		PixelData reflection = calculateReflection(rayHit, lights, objects, material, depth);
		PixelData transparentComponent = calculateTransparency(rayHit, lights, objects, material, depth);

		Color finalColor = ambientComponent
				.add(transparentComponent.getColor().multiply(material.getTransparencyAt(rayHit.getHitPoint())))
				.add(reflection.getColor().multiply(reflectivity)).multiply(diffuseFactor).add(diffuseComponent)
				.add(specularComponent);

		return finalColor;
	}

	private PixelData calculateTransparency(RayHit rayHit, List<Light> lights, List<Object3D> objects,
			Material material, int depth) {
		if (material.getTransparencyAt(rayHit.getHitPoint()) <= 0 || depth > GlobalSettings.MAX_REFLECTION_DEPTH)
			return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);

		Vector3 transparencyStart = rayHit.getHitPoint();
		Vector3 transparencyDirection = rayHit.getRay().getDirection();
		Ray transparencyRay = new Ray(transparencyStart.scale(1e-6), transparencyDirection);

		RayHit transparencyHit = transparencyRay.cast(objects);

		if (transparencyHit != null && transparencyHit.getObject() instanceof Shape) {
			return new PixelData(
					transparencyHit.getShape().getMaterial().getShader().shade(transparencyHit, lights, objects,
							transparencyHit.getShape().getMaterial(), depth + 1),
					transparencyRay.getOrigin().distance(transparencyHit.getHitPoint())
							+ (rayHit.getHitPoint().distance(rayHit.getRay().getOrigin())),
					transparencyHit.getShape().getMaterial().getEmissionAt(transparencyHit.getHitPoint()));

		}
		return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
	}

	protected boolean isInShadow(RayHit rayHit, Light light, List<Object3D> objects) {
		Vector3 hitPoint = rayHit.getHitPoint();
		Vector3 lightDirection = light.getAnchor().subtract(hitPoint).normalize();
		Ray shadowRay = new Ray(hitPoint.add(lightDirection.scale(1e-6)), lightDirection);

		RayHit hit = shadowRay.cast(objects);

		return hit != null && hit.getObject() instanceof Shape;
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
		return (double) Math.pow(specularFactor, 2)
				* rayHit.getShape().getMaterial().getReflectivityAt(rayHit.getHitPoint());
	}

	protected PixelData calculateReflection(RayHit rayHit, List<Light> lights, List<Object3D> objects,
			Material material, int depth) {
		if (depth > GlobalSettings.MAX_REFLECTION_DEPTH || material.getReflectivityAt(rayHit.getHitPoint()) <= 0) {
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
							reflectedHit.getShape().getMaterial(), depth + 1),
					reflectedRay.getOrigin().distance(reflectedHit.getHitPoint())
							+ (rayHit.getHitPoint().distance(rayHit.getRay().getOrigin())),
					reflectedHit.getShape().getMaterial().getEmissionAt(reflectedHit.getHitPoint()));
		}
		return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
	}
}
