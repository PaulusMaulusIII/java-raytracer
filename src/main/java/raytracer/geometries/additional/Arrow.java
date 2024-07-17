package raytracer.geometries.additional;

import java.util.List;

import raytracer.geometries.Cylinder;
import raytracer.geometries.Sphere;
import raytracer.utilities.Material;
import raytracer.utilities.Shape;
import raytracer.utilities.math.Ray;
import raytracer.utilities.math.Vector3;

public class Arrow extends Shape {

	private Vector3 axis;
	private double length;

	public Arrow(Vector3 anchor, Material material) {
		this(anchor, material, new Vector3(0, 1, 0), 2);
	}

	public Arrow(Vector3 anchor, Material material, Vector3 axis) {
		this(anchor, material, axis, 2);
	}

	public Arrow(Vector3 anchor, Material material, Vector3 axis, double length) {
		super(anchor, material);
		this.axis = axis.normalize();
		this.length = length;
	}

	@Override
	public List<Vector3> getPoints() {
		return List.of(getAnchor(), getAnchor().add(axis.scale(length)));
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		Vector3 sphereIntersection = new Sphere(getPoints().get(1), getMaterial(), .2).getIntersectionPoint(ray);
		Vector3 cylinderIntersection = new Cylinder(getAnchor(), getMaterial(), axis, .1, length)
				.getIntersectionPoint(ray);
		if (sphereIntersection != null)
			return sphereIntersection;
		return cylinderIntersection;
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		if (hitPoint.distance(getPoints().get(1)) <= .2)
			return new Sphere(getPoints().get(1), getMaterial(), .2).getNormal(hitPoint);
		else
			return new Cylinder(getAnchor(), getMaterial(), axis, .1, length).getNormal(hitPoint);
	}

	@Override
	public double distanceToEdge(Vector3 point) {
		// Distance from a point to a line in 3D
		Vector3 anchorToPoint = point.subtract(getAnchor());
		Vector3 projection = axis.scale(anchorToPoint.dot(axis));
		Vector3 rejection = anchorToPoint.subtract(projection);
		return rejection.length();
	}
}