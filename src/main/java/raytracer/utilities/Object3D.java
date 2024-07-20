package raytracer.utilities;

import java.util.List;
import java.util.LinkedList;

import raytracer.core.interfaces.PositionChangeListener;
import raytracer.utilities.math.Vector3;

public abstract class Object3D {

	Vector3 anchor;
	List<PositionChangeListener> listeners = new LinkedList<>();

	public Object3D() {
		super();
	}

	public Object3D(Vector3 anchorVector3) {
		super();
		this.anchor = anchorVector3;
	}

	/**
	 * Sets new {@code Vector3} Anchor property
	 * 
	 * @param anchor {@code Vector3} new Anchor
	 */
	public void setAnchor(Vector3 anchor) {
		for (PositionChangeListener changeListener : listeners)
			changeListener.posChanged(this.anchor, anchor);
		this.anchor = anchor;
	}

	/**
	 * Gets current {@code Vector3} anchor property
	 * 
	 * @return {@code Vector3} current anchor
	 */
	public Vector3 getAnchor() {
		return anchor;
	}

	public void addChangeListener(PositionChangeListener changeListener) {
		listeners.add(changeListener);
	}
}
