package gameboy.materials;

import gameboy.utilities.Color;

import gameboy.utilities.Material;

public class MirrorMaterial extends Material {
	public MirrorMaterial() {
		super(Color.WHITE);
		reflectivity = 1;
	}
}
