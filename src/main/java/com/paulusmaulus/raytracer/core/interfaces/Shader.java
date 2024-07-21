package com.paulusmaulus.raytracer.core.interfaces;

import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Scene;
import com.paulusmaulus.raytracer.utilities.math.RayHit;

public interface Shader {
	public abstract Color shade(RayHit rayHit, Scene scene, Material material, int depth);
}
