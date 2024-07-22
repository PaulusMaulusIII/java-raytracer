package com.paulusmaulus.raytracer.geometries;

import java.util.List;

import com.paulusmaulus.raytracer.geometries.additional.Face;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.math.Ray;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Polygon extends Shape {

	public Polygon(Vector3 anchor, Material material, Face... faces) {
		super(anchor, material);
	}

	@Override
	public List<Vector3> getPoints() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getPoints'");
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getIntersectionPoint'");
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getNormal'");
	}

	@Override
	public double distanceToEdge(Vector3 point) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'distanceToEdge'");
	}

}
