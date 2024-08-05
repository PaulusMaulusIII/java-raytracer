package com.paulusmaulus.raytracer.geometries;

import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.math.Ray;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Circle extends Plane {

	double radius;

	public Circle(Vector3 anchor, Material material, Vector3 axis, double radius) {
		super(anchor, material, axis);
		this.radius = radius;
		setName(toString());
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		Vector3 hitPoint = super.getIntersectionPoint(ray);
		if (hitPoint == null || hitPoint.distance(getAnchor()) > radius)
			return null;
		return hitPoint;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}
}
