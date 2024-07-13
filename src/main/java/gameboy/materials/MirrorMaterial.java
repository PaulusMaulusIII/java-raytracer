package gameboy.materials;

import gameboy.shaders.Shader;
import gameboy.utilities.Color;

import gameboy.utilities.Material;

public class MirrorMaterial extends Material {
	public MirrorMaterial(Shader shader) {
		super(shader, Color.BLACK);
		reflectivity = 1;
	}
}
