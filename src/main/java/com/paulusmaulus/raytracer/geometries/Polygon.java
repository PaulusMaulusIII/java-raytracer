package com.paulusmaulus.raytracer.geometries;

import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

import com.paulusmaulus.raytracer.geometries.additional.Face;
import com.paulusmaulus.raytracer.geometries.additional.Face.BoundingBox;
import com.paulusmaulus.raytracer.geometries.additional.Face.Vertex;
import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.math.Ray;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Polygon extends Shape {

	private List<Vector3> vertices;
	private List<Face> faces;
	private BoundingBox boundingBox;

	public Polygon(Vector3 anchor, Material material, Face... faces) {
		this(anchor, material, List.of(faces));
	}

	public Polygon(Vector3 anchor, Material material, List<Face> faces) {
		super(anchor, material);
		this.faces = faces;
		this.vertices = extractUniqueVertices(faces);
		this.boundingBox = new Face.BoundingBox(faces);
	}

	public Polygon(Vector3 anchor, Material material, List<Vector3> points, List<Face> faces) {
		super(anchor, material);
		this.vertices = points;
		this.faces = faces;
		this.boundingBox = new Face.BoundingBox(faces);
	}

	private List<Vector3> extractUniqueVertices(List<Face> faces) {
		Set<Vector3> vertexSet = new LinkedHashSet<>();
		for (Face face : faces) {
			for (Face.Vertex vertex : face.getVertices()) {
				vertexSet.add(vertex.getPosition());
			}
		}
		return List.copyOf(vertexSet);
	}

	@Override
	public List<Vector3> getPoints() {
		return vertices;
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		if (!boundingBox.intersectsBoundingBox(ray))
			return null;
		for (Face face : faces) {
			Vector3 hit = face.getIntersectionPoint(ray);
			if (hit != null)
				return hit;
		}
		return null;
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		for (Face face : faces)
			if (face.isPointInside(hitPoint))
				return face.getNormal();
		return new Vector3(0, 0, 0); // Return null if no face contains the hitPoint
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

	@Override
	public void setAnchor(Vector3 anchor) {
		if (getAnchor() != null) {
			Vector3 difference = anchor.subtract(getAnchor());
			for (Face face : faces) {
				for (Vertex vertex : face.getVertices()) {
					vertex.setPosition(vertex.getPosition().add(difference));
				}
				face.setCenter(face.getCenter().add(difference));
				face.recalculateBoundingBox();
			}
			boundingBox = new BoundingBox(faces);
			super.setAnchor(getAnchor().add(difference));
		}
		else {
			super.setAnchor(anchor);
		}
	}

	public List<Face> getFaces() {
		return faces;
	}
}
