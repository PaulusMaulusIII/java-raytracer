package gameboy.geometries;

import java.util.ArrayList;
import java.util.List;

import gameboy.utilities.Material;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.Vector3;

/**
 * Represents a polygonal shape in 3D space.
 */
public class Polygon extends Shape {
	private List<Vector3> vertices;

	/**
	 * Constructs a polygon with the given anchor, material, and vertices.
	 * 
	 * @param anchor   the anchor point of the polygon.
	 * @param material the material of the polygon.
	 * @param vertices the list of vertices defining the polygon.
	 */
	public Polygon(Vector3 anchor, Material material, List<Vector3> vertices) {
		super(anchor, material);
		this.vertices = new ArrayList<>(vertices);
	}

	@Override
	public List<Vector3> getPoints() {
		return vertices;
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		if (vertices.size() < 3) {
			return null;
		}

		for (Vector3 v0 : vertices) {
			for (Vector3 v1 : vertices) {
				if (v1 != v0) {
					for (Vector3 v2 : vertices) {
						if (v2 != v1 && v2 != v0) {
							Vector3 hit = intersectTriangle(ray, v0, v1, v2);
							if (hit != null) {
								return hit;
							}
						}
					}
				}
			}
		}

		return null;
	}

	private Vector3 intersectTriangle(Ray ray, Vector3 v0, Vector3 v1, Vector3 v2) {
		Vector3 edge1 = v1.subtract(v0);
		Vector3 edge2 = v2.subtract(v0);
		Vector3 h = ray.getDirection().cross(edge2);
		double a = edge1.dot(h);
		if (a > -1e-8 && a < 1e-8) {
			return null;
		}

		double f = 1.0 / a;
		Vector3 s = ray.getOrigin().subtract(v0);
		double u = f * s.dot(h);
		if (u < 0.0 || u > 1.0) {
			return null;
		}

		Vector3 q = s.cross(edge1);
		double v = f * ray.getDirection().dot(q);
		if (v < 0.0 || u + v > 1.0) {
			return null;
		}

		double t = f * edge2.dot(q);
		if (t > 1e-8) {
			return ray.getOrigin().add(ray.getDirection().scale(t));
		}
		else {
			return null;
		}
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		if (vertices.size() < 3) {
			return null;
		}

		Vector3 v0 = vertices.get(0);
		Vector3 v1 = vertices.get(1);
		Vector3 v2 = vertices.get(2);

		Vector3 edge1 = v1.subtract(v0);
		Vector3 edge2 = v2.subtract(v0);
		return edge1.cross(edge2).normalize();
	}

	@Override
	public String toString() {
		return "Polygon@" + Integer.toHexString(hashCode());
	}

	@Override
	public double distanceToEdge(Vector3 point) {
		// Calculate the distance from the point to the nearest edge of the polygon
		double minDistance = Double.POSITIVE_INFINITY;
		int numVertices = vertices.size();
		for (int i = 0; i < numVertices; i++) {
			Vector3 v1 = vertices.get(i);
			Vector3 v2 = vertices.get((i + 1) % numVertices);
			Vector3 edge = v2.subtract(v1);
			Vector3 v1ToPoint = point.subtract(v1);
			double projectionLength = v1ToPoint.dot(edge) / edge.dot(edge);
			Vector3 projectionPoint;
			if (projectionLength < 0) {
				projectionPoint = v1;
			}
			else if (projectionLength > 1) {
				projectionPoint = v2;
			}
			else {
				projectionPoint = v1.add(edge.scale(projectionLength));
			}
			double distance = point.subtract(projectionPoint).length();
			minDistance = Math.min(minDistance, distance);
		}
		return minDistance;
	}
}
