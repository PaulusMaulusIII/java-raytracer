package raytracer.materials;

import raytracer.shaders.Shader;
import raytracer.utilities.Color;
import raytracer.utilities.Material;

public class GradientMaterial extends Material {

	public GradientMaterial(Shader shader, Color color1, Color color2) {
		super(shader, color1);
	}

}
