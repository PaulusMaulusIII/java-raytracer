package com.paulusmaulus.raytracer.geometries.additional;

import java.util.List;
import com.paulusmaulus.raytracer.geometries.Plane;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.math.Ray;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Face {

	public static class Vertex {
		private final Vector3 position;
		private final Vector3 texCoord;
		private final Vector3 normal;

		public Vertex(Vector3 position, Vector3 texCoord, Vector3 normal) {
			this.position = position;
			this.texCoord = texCoord;
			this.normal = normal;
		}

		public Vector3 getPosition() {
			return position;
		}

		public Vector3 getTexCoord() {
			return texCoord;
		}

		public Vector3 getNormal() {
			return normal;
		}
	}

	private final List<Vertex> vertices;
	private final Material material;

	public Face(Material material, Vertex... vertices) {
		this(material, List.of(vertices));
	}

	public Face(Material material, List<Vertex> vertices) {
		this.material = material;
		this.vertices = vertices;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder("[");
		for (int i = 0; i < vertices.size() - 1; i++)
			string.append(vertices.get(i).getPosition().toString()).append(", ");
		string.append(vertices.get(vertices.size() - 1).getPosition().toString()).append("]");
		return string.toString();
	}

	public Vector3 getCenter() {
		Vector3 center = new Vector3(0, 0, 0);
		for (Vertex vertex : vertices) {
			center = center.add(vertex.getPosition());
		}
		center = center.scale(1.0 / vertices.size());
		return center;
	}

	public Vector3 getNormal() {
		if (vertices.size() < 3) {
			throw new IllegalStateException("A face must have at least 3 vertices to calculate a normal.");
		}
		Vector3 ab = vertices.get(1).getPosition().subtract(vertices.get(0).getPosition());
		Vector3 ac = vertices.get(2).getPosition().subtract(vertices.get(0).getPosition());
		return ab.cross(ac).normalize();
	}

	public boolean isPointInside(Vector3 point) {
		Vector3 normal = getNormal();
		Vector3 axis1, axis2;

		if (Math.abs(normal.x) > Math.abs(normal.y) && Math.abs(normal.x) > Math.abs(normal.z)) {
			axis1 = new Vector3(0, 1, 0);
			axis2 = new Vector3(0, 0, 1);
		}
		else if (Math.abs(normal.y) > Math.abs(normal.z)) {
			axis1 = new Vector3(1, 0, 0);
			axis2 = new Vector3(0, 0, 1);
		}
		else {
			axis1 = new Vector3(1, 0, 0);
			axis2 = new Vector3(0, 1, 0);
		}

		// Project the point onto the 2D plane
		double px = point.dot(axis1);
		double py = point.dot(axis2);

		// Project all vertices onto the 2D plane
		double[] xVertices = new double[vertices.size()];
		double[] yVertices = new double[vertices.size()];

		for (int i = 0; i < vertices.size(); i++) {
			Vector3 vertex = vertices.get(i).getPosition();
			xVertices[i] = vertex.dot(axis1);
			yVertices[i] = vertex.dot(axis2);
		}

		// Perform the point-in-polygon test
		boolean inside = false;
		int j = vertices.size() - 1;

		for (int i = 0; i < vertices.size(); i++) {
			if ((yVertices[i] > py) != (yVertices[j] > py)
					&& (px < (xVertices[j] - xVertices[i]) * (py - yVertices[i]) / (yVertices[j] - yVertices[i])
							+ xVertices[i])) {
				inside = !inside;
			}
			j = i;
		}

		return inside;
	}

	public Vector3 getIntersectionPoint(Ray ray) {
		Plane plane = new Plane(getCenter(), material, getNormal());
		Vector3 pointOnPlane = plane.getIntersectionPoint(ray);
		if (pointOnPlane == null) {
			return null;
		}

		if (isPointInside(pointOnPlane)) {
			return pointOnPlane;
		}

		return null;
	}

	public double distanceToEdge(Vector3 point) {
		double minDistance = Double.POSITIVE_INFINITY;
		for (int i = 0; i < vertices.size(); i++) {
			Vector3 a = vertices.get(i).getPosition();
			Vector3 b = vertices.get((i + 1) % vertices.size()).getPosition();
			double distance = pointToSegmentDistance(point, a, b);
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance;
	}

	private double pointToSegmentDistance(Vector3 p, Vector3 a, Vector3 b) {
		Vector3 ab = b.subtract(a);
		Vector3 ap = p.subtract(a);
		double t = ap.dot(ab) / ab.dot(ab);
		t = Math.max(0, Math.min(1, t));
		Vector3 projection = a.add(ab.scale(t));
		return p.subtract(projection).magnitude();
	}
}
