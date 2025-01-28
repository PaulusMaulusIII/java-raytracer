package com.paulusmaulus.editor.core.interfaces;

import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.Scene;
import com.paulusmaulus.editor.utilities.math.RayHit;

public interface Shader {
	public abstract Color shade(RayHit rayHit, Scene scene, Material material, int depth);
}
