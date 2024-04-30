package gameboy.geometries;

import java.util.LinkedList;
import java.util.List;

import gameboy.materials.Material;
import gameboy.utilities.Shape3D;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;
import javafx.scene.paint.Color;

public class Cone extends Shape3D {

	Vector3 axis;
	double angle;
	double height;

	public Cone(Vector3 anchor, Vector3 axis, double angle, double height, Color color) {
		super(anchor);
		setMaterial(new ConeMaterial(color));
		this.axis = axis;
		this.angle = angle;
		this.height = height;
	}

	@Override
	public List<Vector3> getPoints() {
		return new LinkedList<>(List.of(getAnchor(), getAnchor().add(axis)));
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		Vector3 co = ray.getOrigin().subtract(getAnchor());

		double a = ray.getDirection().dot(axis) * ray.getDirection().dot(axis) - Math.cos(angle) * Math.cos(angle);
		double b = 2. * (ray.getDirection().dot(axis) * co.dot(axis)
				- ray.getDirection().dot(co) * Math.cos(angle) * Math.cos(angle));
		double c = co.dot(axis) * co.dot(axis) - co.dot(co) * Math.cos(angle) * Math.cos(angle);

		double det = b * b - 4. * a * c;
		if (det < 0.)
			return null;

		det = Math.sqrt(det);
		double t1 = (-b - det) / (2. * a);
		double t2 = (-b + det) / (2. * a);

		// This is a bit messy; there ought to be a more elegant solution.
		double t = t1;
		if (t < 0. || t2 > 0. && t2 < t)
			t = t2;
		if (t < 0.)
			return null;

		Vector3 cp = ray.getOrigin().add(ray.getDirection().scale(t)).subtract(getAnchor());
		double h = cp.dot(axis);
		if (h < 0. || h > height)
			return null;

		return cp;
	}

	public class ConeMaterial extends Material {

		public ConeMaterial() {
			super();
		}

		public ConeMaterial(Color color) {
			super(color);
		}

		@Override
		public Vector3 getNormal(RayHit hit) {
			Vector3 cp = hit.getHitPoint();
			Vector3 n = cp.scale(axis.dot(cp) / cp.dot(cp)).subtract(axis).normalize();
			return n;
		}

	}

}
