package raytracer.shaders;

import java.util.List;

import raytracer.lights.Light;
import raytracer.utilities.Color;
import raytracer.utilities.Material;
import raytracer.utilities.Object3D;
import raytracer.utilities.math.RayHit;

public class RainbowShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		double closestDistance = Double.MAX_VALUE;

		for (Light light : lights) {
			double distance = rayHit.getHitPoint().distance(light.getAnchor()) * 2;
			if (distance < closestDistance) {
				closestDistance = distance * 2;
			}
		}

		if (closestDistance > 360) {
			return Color.BLACK;
		}
		return Color.hsv((int) closestDistance, 1, 1);
	}
}
