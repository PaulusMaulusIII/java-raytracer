package raytracer.shaders;

import java.util.List;

import raytracer.core.interfaces.Shader;
import raytracer.lights.Light;
import raytracer.utilities.Color;
import raytracer.utilities.Material;
import raytracer.utilities.Object3D;
import raytracer.utilities.math.RayHit;

public class DistanceShader implements Shader {
	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		return (rayHit.getRay().getOrigin().distance(rayHit.getHitPoint()) * 10 > 360) ? Color.BLACK
				: Color.hsv((int) rayHit.getRay().getOrigin().distance(rayHit.getHitPoint()) * 10, 1, 1);
	}
}
