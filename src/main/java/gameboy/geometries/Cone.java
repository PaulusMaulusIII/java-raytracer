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

	public Cone(Vector3 anchor, Vector3 axis, double angle) {
		super(anchor);
		setMaterial(new ConeMaterial(Color.WHITE));
		this.axis = axis;
		this.angle = angle;
	}

	@Override
	public List<Vector3> getPoints() {
		return new LinkedList<>(List.of(getAnchor(), getAnchor().add(axis)));
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		Vector3 anchor = getAnchor();
		// Parameter für den Kegel
		double cosTheta = Math.cos(angle); // Kosinus des Öffnungswinkels

		// Richtungsvektor des Strahls
		Vector3 rayDirection = ray.getDirection().normalize(); // Normalisiere den Richtungsvektor

		// Vektor vom Strahlursprung zum Mittelpunkt der Basis des Kegels
		Vector3 toAnchor = anchor.subtract(ray.getOrigin());

		// Projektion des Vektors "toAnchor" auf die Kegelachse
		double projection = toAnchor.dot(axis);

		// Vektor von der Basis des Kegels zur Projektion des Strahlursprungs auf die
		// Kegelachse
		Vector3 projectionPoint = anchor.add(axis.scale(projection));

		// Vektor vom Strahlursprung zur Projektion des Strahlursprungs auf die
		// Kegelachse
		Vector3 toProjection = projectionPoint.subtract(ray.getOrigin());

		// Abstand von der Projektion des Strahlursprungs zum Mittelpunkt der Basis des
		// Kegels
		double distanceToAxis = toProjection.magnitude();

		// Abstand von der Projektion des Strahlursprungs zur Basis des Kegels
		double distanceToBase = Math.sqrt((toAnchor.magnitude() * toAnchor.magnitude()) - projection * projection);

		// Winkel zwischen dem Vektor "toAnchor" und der Kegelachse
		double angleToAxis = Math.acos(projection / toAnchor.magnitude());

		// Überprüfung, ob der Strahl den Kegel schneidet
		if (angleToAxis <= angle && distanceToAxis <= distanceToBase * cosTheta) {
			// Berechne den Schnittpunkt
			// Entfernungsanteil entlang des Strahls, um den Schnittpunkt zu finden
			double t = projection / rayDirection.dot(axis);

			// Berechne den Schnittpunkt
			Vector3 intersectionPoint = ray.getOrigin().add(rayDirection.scale(t));

			return intersectionPoint;
		}

		// Wenn kein Schnittpunkt gefunden wurde
		return null;
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
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'getNormal'");
		}

	}

}
