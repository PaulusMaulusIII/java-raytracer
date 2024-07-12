package gameboy.shaders;

import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Color;
import gameboy.utilities.Material;
import gameboy.utilities.Object3D;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class BasicShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material) {
		for (Light light : lights) {
			if (isInShadow(rayHit, light, objects)) {
				return Color.BLACK;
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

}
