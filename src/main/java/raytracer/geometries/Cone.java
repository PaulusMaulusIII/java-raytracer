package raytracer.geometries;

import java.util.LinkedList;
import java.util.List;

import raytracer.utilities.Material;
import raytracer.utilities.Shape;
import raytracer.utilities.math.Ray;
import raytracer.utilities.math.Vector3;

public class Cone extends Shape {

	Vector3 axis;
	double angle;
	double height;

	public Cone(Vector3 anchor, Material material, Vector3 axis, double angle, double height) {
		super(anchor, material);
		this.axis = axis.normalize();
		this.angle = angle;
		this.height = height;
	}

	@Override
	public List<Vector3> getPoints() {
		return new LinkedList<>(List.of(getAnchor(), getAnchor().add(axis)));
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		Vector3 rayOrigin = ray.getOrigin();
		Vector3 rayDirection = ray.getDirection();
		Vector3 co = rayOrigin.subtract(getAnchor());

		double a = rayDirection.dot(axis) * rayDirection.dot(axis) - Math.cos(angle) * Math.cos(angle);
		double b = 2
				* (rayDirection.dot(axis) * co.dot(axis) - rayDirection.dot(co) * Math.cos(angle) * Math.cos(angle));
		double c = co.dot(axis) * co.dot(axis) - co.dot(co) * Math.cos(angle) * Math.cos(angle);

		double det = b * b - 4 * a * c;

		if (det < 0)
			return null;
		det = Math.sqrt(det);

		double tMin = (-b - det) / (2. * a);
		double tMax = (-b + det) / (2. * a);

		double t = tMin;
		if (t < 0 || tMax > 0 && tMax < t)
			t = tMax;
		if (t < 0)
			return null;

		Vector3 hitPoint = rayOrigin.add(rayDirection.scale(t)).subtract(getAnchor());
		double heightAtPoint = hitPoint.dot(axis);
		if (heightAtPoint < 0 || heightAtPoint > height)
			return null;

		return hitPoint;
	}

	@Override
	public String toString() {
		return super.toString() + " | " + getMaterial().getColor(getAnchor()).toString();
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		Vector3 n = hitPoint.scale(axis.dot(hitPoint) / hitPoint.dot(hitPoint)).subtract(axis).normalize();
		return n;
	}

	@Override
	public double distanceToEdge(Vector3 point) {
		// Distance to the edge of the cone's base or the side surface
		Vector3 baseCenter = getAnchor().add(axis.scale(height));
		double distanceToAxis = point.subtract(getAnchor()).cross(axis).length();
		double distanceToBaseEdge = Math.abs(distanceToAxis - height * Math.tan(angle));
		double distanceToBase = Math.abs(point.subtract(baseCenter).dot(axis));
		return Math.min(distanceToBaseEdge, distanceToBase);
	}
}
