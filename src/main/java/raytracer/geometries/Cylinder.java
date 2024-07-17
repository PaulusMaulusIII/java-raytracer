package raytracer.geometries;

import java.util.LinkedList;
import java.util.List;
import raytracer.utilities.Material;
import raytracer.utilities.Shape;
import raytracer.utilities.math.Ray;
import raytracer.utilities.math.Vector3;

public class Cylinder extends Shape {

	private Vector3 axis;
	private double radius;
	private double height;

	public Cylinder(Vector3 anchor, Material material, Vector3 axis, double radius, double height) {
		super(anchor.subtract(axis.scale(2)), material);
		this.axis = axis.normalize();
		this.radius = radius;
		this.height = height + 2;
	}

	@Override
	public List<Vector3> getPoints() {
		Vector3 endPoint = getAnchor().add(axis.scale(height));
		return List.of(getAnchor(), endPoint);
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		Vector3 position = ray.getOrigin();
		Vector3 direction = ray.getDirection();
		Vector3 anchor = getAnchor();

		double a = direction.dot(direction) - Math.pow(direction.dot(axis), 2);
		double b = 2 * ((position.subtract(anchor)).dot(direction)
				- ((position.subtract(anchor)).dot(axis) * direction.dot(axis)));
		double c = (position.subtract(anchor)).dot(position.subtract(anchor))
				- Math.pow((position.subtract(anchor)).dot(axis), 2) - Math.pow(radius, 2);

		double discriminant = Math.pow(b, 2) - 4 * a * c;
		if (discriminant < 0) {
			return null;
		}

		List<Vector3> capIntersections = new LinkedList<>();
		Plane bottomCap = new Plane(anchor, getMaterial(), axis);
		Vector3 bottomCapIntersection = bottomCap.getIntersectionPoint(ray);
		if (bottomCapIntersection != null && anchor.distance(bottomCapIntersection) <= radius)
			capIntersections.add(bottomCapIntersection);

		Plane topCap = new Plane(anchor.add(axis.scale(height)), getMaterial(), axis);
		Vector3 topCapIntersection = topCap.getIntersectionPoint(ray);
		if (topCapIntersection != null && anchor.add(axis.scale(height)).distance(topCapIntersection) <= radius)
			capIntersections.add(topCapIntersection);

		double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
		double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);

		double t = (t1 < t2) ? t1 : t2;
		if (t >= 0) {
			Vector3 intersectionPoint = position.add(direction.scale(t));
			Vector3 intersectionToAnchor = intersectionPoint.subtract(anchor);
			double projection = intersectionToAnchor.dot(axis);

			if (projection > 0 && projection < height)
				return intersectionPoint;
		}

		if (!capIntersections.isEmpty()) {
			Vector3 finalIntersection = capIntersections.get(0);
			if (capIntersections.size() > 1) {
				if (capIntersections.get(1).distance(position) < finalIntersection.distance(position)) {
					finalIntersection = capIntersections.get(1);
				}
			}
			return finalIntersection;
		}

		return null;
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		Vector3 anchor = getAnchor();

		Vector3 anchorToPoint = hitPoint.subtract(anchor);
		double projectionLength = anchorToPoint.dot(axis);
		Vector3 projection = axis.scale(projectionLength);
		Vector3 radial = anchorToPoint.subtract(projection);
		Vector3 normalCylinder = radial.normalize();

		if (projectionLength < 1e-6 && projectionLength > -1e-6)
			return axis.invert();

		if (projectionLength > height - 1e-6 && projectionLength < height + 1e-6)
			return axis;

		return normalCylinder;
	}

	@Override
	public double distanceToEdge(Vector3 point) {
		Vector3 anchorToPoint = point.subtract(getAnchor());
		double projectionLength = anchorToPoint.dot(axis);
		Vector3 projection = axis.scale(projectionLength);
		Vector3 radial = anchorToPoint.subtract(projection);
		double radialDistance = radial.length();
		if (projectionLength >= 0 && projectionLength <= height) {
			return Math.abs(radialDistance - radius);
		}
		else if (projectionLength < 0) {
			return point.distance(getAnchor()) - radius;
		}
		else {
			Vector3 endPoint = getAnchor().add(axis.scale(height));
			return point.distance(endPoint) - radius;
		}
	}

	public Vector3 getAxis() {
		return axis;
	}

	public double getHeight() {
		return height;
	}

	public double getRadius() {
		return radius;
	}

	public void setAxis(Vector3 axis) {
		this.axis = axis;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
