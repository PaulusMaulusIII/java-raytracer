package com.paulusmaulus.editor.geometries.additional;

import java.util.List;
import com.paulusmaulus.editor.geometries.Plane;
import com.paulusmaulus.editor.materials.BasicMaterial;
import com.paulusmaulus.editor.shaders.BasicShader;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Material;
import com.paulusmaulus.editor.utilities.math.Ray;
import com.paulusmaulus.editor.utilities.math.Vector3;

public class Face {

	public static class Vertex {
		private Vector3 position;
		private Vector3 texCoord;
		private Vector3 normal;

		public Vertex(Vector3 position, Vector3 texCoord, Vector3 normal) {
			this.position = position;
			this.texCoord = texCoord;
			this.normal = normal;
		}

		public void setPosition(Vector3 position) {
			this.position = position;
		}

		public Vector3 getPosition() {
			return position;
		}

		public void setTexCoord(Vector3 texCoord) {
			this.texCoord = texCoord;
		}

		public Vector3 getTexCoord() {
			return texCoord;
		}

		public void setNormal(Vector3 normal) {
			this.normal = normal;
		}

		public Vector3 getNormal() {
			return normal;
		}
	}

	public static class BoundingBox {
		final Vector3 min;
		final Vector3 max;

		public BoundingBox(List<Face> faces) {
			min = calculateMin(faces);
			max = calculateMax(faces);
		}

		public Vector3 getMin() {
			return min;
		}

		public Vector3 getMax() {
			return max;
		}

		private Vector3 calculateMin(List<Face> faces) {
			double minX = Double.POSITIVE_INFINITY;
			double minY = Double.POSITIVE_INFINITY;
			double minZ = Double.POSITIVE_INFINITY;

			for (Face face : faces) {
				for (Vertex vertex : face.getVertices()) {
					Vector3 vertexPos = vertex.getPosition();
					if (vertexPos.x < minX)
						minX = vertexPos.x;
					if (vertexPos.y < minY)
						minY = vertexPos.y;
					if (vertexPos.z < minZ)
						minZ = vertexPos.z;
				}
			}

			return new Vector3(minX, minY, minZ);
		}

		private Vector3 calculateMax(List<Face> faces) {
			double maxX = Double.NEGATIVE_INFINITY;
			double maxY = Double.NEGATIVE_INFINITY;
			double maxZ = Double.NEGATIVE_INFINITY;

			for (Face face : faces) {
				for (Vertex vertex : face.getVertices()) {
					Vector3 vertexPos = vertex.getPosition();
					if (vertexPos.x > maxX)
						maxX = vertexPos.x;
					if (vertexPos.y > maxY)
						maxY = vertexPos.y;
					if (vertexPos.z > maxZ)
						maxZ = vertexPos.z;
				}
			}

			return new Vector3(maxX, maxY, maxZ);
		}

		public boolean isInsideBoundingBox(Vector3 point) {
			double[] pointXYZ = point.toArray();
			double[] minXYZ = min.toArray();
			double[] maxXYZ = max.toArray();
			for (int i = 0; i < point.toArray().length; i++)
				if (pointXYZ[i] < minXYZ[i] || pointXYZ[i] > maxXYZ[i])
					return false;
			return true;
		}

		public boolean intersectsBoundingBox(Ray ray) {
			Vector3 invDir = new Vector3(1.0 / ray.getDirection().x, 1.0 / ray.getDirection().y,
					1.0 / ray.getDirection().z);

			double t1 = (min.x - ray.getOrigin().x) * invDir.x;
			double t2 = (max.x - ray.getOrigin().x) * invDir.x;
			double t3 = (min.y - ray.getOrigin().y) * invDir.y;
			double t4 = (max.y - ray.getOrigin().y) * invDir.y;
			double t5 = (min.z - ray.getOrigin().z) * invDir.z;
			double t6 = (max.z - ray.getOrigin().z) * invDir.z;

			double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
			double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

			if (tmax < 0)
				return false;
			if (tmin > tmax)
				return false;
			return true;
		}

	}

	private List<Vertex> vertices;
	private Material material;
	private Vector3 normal;
	private Vector3 center;
	private BoundingBox boundingBox;

	public Face(Material material, Vertex... vertices) {
		this(material, List.of(vertices));
	}

	public Face(Material material, List<Vertex> vertices) {
		if (vertices.size() < 3) {
			throw new IllegalArgumentException("A face must have at least 3 vertices.");
		}

		this.material = material;
		this.vertices = vertices;
		this.normal = computeNormal();
		this.center = computeCenter();
		this.boundingBox = new BoundingBox(List.of(this));
	}

	public void recalculateBoundingBox() {
		boundingBox = new BoundingBox(List.of(this));
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < vertices.size() - 1; i++) {
			sb.append(vertices.get(i).getPosition().toString()).append(", ");
		}
		sb.append(vertices.get(vertices.size() - 1).getPosition().toString()).append("]");
		return sb.toString();
	}

	private Vector3 computeCenter() {
		Vector3 sum = new Vector3(0, 0, 0);
		for (Vertex vertex : vertices) {
			sum = sum.add(vertex.getPosition());
		}
		return sum.scale(1.0 / vertices.size());
	}

	public Vector3 getCenter() {
		return center;
	}

	public void setCenter(Vector3 center) {
		this.center = center;
	}

	private Vector3 computeNormal() {
		if (vertices.get(2) != null && vertices.get(1) != null) {
			Vector3 ab = vertices.get(1).getPosition().subtract(vertices.get(0).getPosition());
			Vector3 ac = vertices.get(2).getPosition().subtract(vertices.get(0).getPosition());
			return ab.cross(ac).normalize();
		}
		return new Vector3(0, 0, 0);
	}

	public Vector3 getNormal() {
		Vector3 vertexAvgNormal = new Vector3(0, 0, 0);
		int counter = 0;
		for (Vertex vertex : vertices) {
			if (vertex.getNormal() != null) {
				vertexAvgNormal.add(vertex.getNormal());
				counter++;
			}
		}
		if (counter > 0) {
			vertexAvgNormal = vertexAvgNormal.scale(1 / counter);
			normal = vertexAvgNormal;
		}
		if (normal != null)
			normal = computeNormal();
		if (normal != null)
			return normal;
		return new Vector3(0, 0, 0);
	}

	public boolean isPointInside(Vector3 point) {
		if (!boundingBox.isInsideBoundingBox(point))
			return false;

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
		double px = point.dot(axis1);
		double py = point.dot(axis2);
		double[] xVertices = new double[vertices.size()];
		double[] yVertices = new double[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			Vector3 vertex = vertices.get(i).getPosition();
			xVertices[i] = vertex.dot(axis1);
			yVertices[i] = vertex.dot(axis2);
		}
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
		if (normal.dot(ray.getDirection()) >= 0)
			return null;

		Plane plane = new Plane(center, new BasicMaterial(new BasicShader(), Color.BLACK), normal);
		Vector3 pointOnPlane = plane.getIntersectionPoint(ray);
		if (pointOnPlane == null)
			return null;
		return isPointInside(pointOnPlane) ? pointOnPlane : null;
	}

	public double distanceToEdge(Vector3 point) {
		double minDistance = Double.POSITIVE_INFINITY;
		for (int i = 0; i < vertices.size(); i++) {
			Vector3 a = vertices.get(i).getPosition();
			Vector3 b = vertices.get((i + 1) % vertices.size()).getPosition();
			minDistance = Math.min(minDistance, pointToSegmentDistance(point, a, b));
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

	public void setVertices(List<Vertex> updatedVertices) {
		vertices = updatedVertices;
	}
}
