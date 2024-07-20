package com.paulusmaulus.raytracer.core.interfaces;

import java.util.List;

import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.math.RayHit;

public interface Shader {
	public abstract Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material,
			int depth);
}
