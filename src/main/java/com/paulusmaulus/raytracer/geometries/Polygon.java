package com.paulusmaulus.raytracer.geometries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedList;

import com.paulusmaulus.raytracer.geometries.additional.Face;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.math.Ray;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Polygon extends Shape {

	private List<Vector3> vertices = new LinkedList<>();
	private List<Face> faces = new LinkedList<>();
	private List<Vector3> surfaceNormals = new LinkedList<>();

	public Polygon(Vector3 anchor, Material material, Face... faces) {
		this(anchor, material, List.of(faces));
	}

	public Polygon(Vector3 anchor, Material material, List<Face> faces) {
		super(anchor, material);
		Set<Vector3> vertexSet = new HashSet<>();
		for (Face face : faces) {
			for (Face.Vertex vertex : face.getVertices()) {
				vertexSet.add(vertex.getPosition());
			}
		}
		this.vertices = new LinkedList<>(vertexSet);
		this.faces = faces;
		for (Face face : faces) {
			surfaceNormals.add(face.getNormal());
		}
	}

	public Polygon(Vector3 anchor, Material material, List<Vector3> points, List<Face> faces) {
		super(anchor, material);
		this.vertices = points;
		this.faces = faces;
		for (Face face : faces) {
			surfaceNormals.add(face.getNormal());
		}
	}

	public Polygon(Vector3 anchor, Material material, List<Vector3> points, List<Face> faces, List<Vector3> normals) {
		super(anchor, material);
		this.vertices = points;
		this.faces = faces;
		this.surfaceNormals = normals;
	}

	@Override
	public List<Vector3> getPoints() {
		return vertices;
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		for (Face face : faces) {
			Vector3 hit = face.getIntersectionPoint(ray);
			if (hit != null) {
				return hit;
			}
		}
		return null;
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		// Find the face containing the hit point
		for (Face face : faces) {
			if (face.isPointInside(hitPoint)) {
				return face.getNormal();
			}
		}
		return new Vector3(0, 0, 0); // Return a default normal if no face contains the hit point
	}

	@Override
	public double distanceToEdge(Vector3 point) {
		double minDistance = Double.POSITIVE_INFINITY;
		for (Face face : faces) {
			double distance = face.distanceToEdge(point);
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance;
	}
}
