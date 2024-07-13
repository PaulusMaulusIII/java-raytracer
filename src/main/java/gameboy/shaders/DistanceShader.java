package gameboy.shaders;

import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Color;
import gameboy.utilities.Material;
import gameboy.utilities.Object3D;
import gameboy.utilities.math.RayHit;

public class DistanceShader implements Shader {
	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material) {
		return (rayHit.getRay().getOrigin().distance(rayHit.getHitPoint()) * 10 > 360) ? Color.BLACK
				: Color.hsv((int) rayHit.getRay().getOrigin().distance(rayHit.getHitPoint()) * 10, 1, 1);
	}
}
