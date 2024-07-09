package gameboy.utilities;

import gameboy.utilities.math.Vector3;

public class Object3D {

	Vector3 anchor;

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
}
