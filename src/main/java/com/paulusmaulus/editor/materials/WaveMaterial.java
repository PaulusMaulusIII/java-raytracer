package com.paulusmaulus.editor.materials;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.math.Vector3;

public class WaveMaterial extends Material {

	private long time;
	private Vector3 normal = new Vector3(1, 2, 0);

	public WaveMaterial(Shader shader) {
		super(shader, Color.WHITE);
		time = System.currentTimeMillis();
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		if (System.currentTimeMillis() - time > 1) {
			normal = normal.rotate(0, Math.toRadians(1), 0); // TODO specular !!!!!!!!!!
			time = System.currentTimeMillis();
		}
		return normal;
	}

	@Override
	protected String getName() {
		return "Wave";
	}
}
