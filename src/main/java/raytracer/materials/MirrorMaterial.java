package raytracer.materials;

import raytracer.core.interfaces.Shader;
import raytracer.utilities.Color;
import raytracer.utilities.Material;

public class MirrorMaterial extends Material {
	public MirrorMaterial(Shader shader) {
		super(shader, Color.BLACK);
		reflectivity = 1;
	}
}
