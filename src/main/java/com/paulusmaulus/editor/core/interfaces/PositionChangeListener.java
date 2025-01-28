package com.paulusmaulus.editor.core.interfaces;

import com.paulusmaulus.editor.utilities.math.Vector3;

public interface PositionChangeListener {
	void posChanged(Vector3 origPos, Vector3 newPos);
}
