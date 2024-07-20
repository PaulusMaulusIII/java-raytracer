package raytracer.utilities.data;

import raytracer.utilities.math.Vector3;

public interface PositionChangeListener {
	void posChanged(Vector3 origPos, Vector3 newPos);
}
