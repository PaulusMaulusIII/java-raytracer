package raytracer.materials;

import raytracer.shaders.Shader;
import raytracer.utilities.Color;
import raytracer.utilities.Material;

public class BasicMaterial extends Material {

	public BasicMaterial(Shader shader, Color color) {
		super(shader, color);
	}

}
