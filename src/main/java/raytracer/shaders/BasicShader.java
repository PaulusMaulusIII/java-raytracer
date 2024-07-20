package raytracer.shaders;

import java.util.List;

import raytracer.core.interfaces.Shader;
import raytracer.lights.Light;
import raytracer.utilities.Color;
import raytracer.utilities.Material;
import raytracer.utilities.Object3D;
import raytracer.utilities.math.RayHit;

public class BasicShader implements Shader {

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		return material.getColor(rayHit.getHitPoint());
	}

}
