package com.paulusmaulus.raytracer.core.interfaces;

import com.paulusmaulus.raytracer.utilities.math.Vector3;

public interface PositionChangeListener {
	void posChanged(Vector3 origPos, Vector3 newPos);
}
