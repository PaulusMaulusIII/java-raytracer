package gameboy.shaders;

import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Color;
import gameboy.utilities.Material;
import gameboy.utilities.Object3D;
import gameboy.utilities.math.RayHit;

public interface Shader {
	public abstract Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material);
}
