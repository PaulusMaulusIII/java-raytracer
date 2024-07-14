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

public class BasicShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		for (Light light : lights) {
			if (isInShadow(rayHit, light, objects)) {
				return Color.BLACK;
			}
			if (material.getReflectivity() > 0) {
				return material.getColor(rayHit.getHitPoint()).interpolate(
						calculateReflection(rayHit, lights, objects, material, depth).getColor(),
						material.getReflectivity());
			}
		}
		return material.getColor(rayHit.getHitPoint());
	}

	private boolean isInShadow(RayHit rayHit, Light light, List<Object3D> objects) {
		Vector3 hitPoint = rayHit.getHitPoint();
		Vector3 lightDirection = light.getAnchor().subtract(hitPoint).normalize();
		Ray shadowRay = new Ray(hitPoint.add(lightDirection.scale(1e-6)), lightDirection);

		RayHit hit = shadowRay.cast(objects);

		if (hit != null && hit.getObject() instanceof Shape)
			return true;
		return false;
	}

	protected PixelData calculateReflection(RayHit rayHit, List<Light> lights, List<Object3D> objects,
			Material material, int depth) {
		if (depth > GlobalSettings.MAX_REFLECTION_DEPTH)
			return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);

		if (material.getReflectivity() <= 0)
			return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);

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
					reflectedRay.getOrigin().distance(reflectedHit.getHitPoint()),
					reflectedHit.getShape().getMaterial().getEmission());
		}

		return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
	}
}
