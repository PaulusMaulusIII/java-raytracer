package gameboy.materials;

import gameboy.shaders.Shader;
import gameboy.utilities.Color;
import gameboy.utilities.Material;

public class GradientMaterial extends Material {

	public GradientMaterial(Shader shader, Color color1, Color color2) {
		super(shader, color1);
	}

}
