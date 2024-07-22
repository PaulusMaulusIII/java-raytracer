package com.paulusmaulus.raytracer.geometries.additional;

import java.util.List;

import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Face {

	List<Vector3> vertices;

	public Face(Vector3... vertices) {
		this(List.of(vertices));
	}

	public Face(List<Vector3> vertices) {
		this.vertices = vertices;
	}

	public List<Vector3> getVertices() {
		return vertices;
	}
}
