package gameboy.materials;

import java.awt.Color;

import gameboy.utilities.Material;

public class MirrorMaterial extends Material {
	public MirrorMaterial() {
		super(Color.WHITE);
		reflectiveness = 1;
	}
}
