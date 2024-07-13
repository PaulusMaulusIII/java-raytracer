package gameboy.geometries;

import java.util.LinkedList;
import java.util.List;

import gameboy.utilities.Material;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.Vector3;

public class Plane extends Shape {

    Vector3 axis;

    public Plane(Vector3 anchor, Material material, Vector3 axis) {
        super(anchor, material);
        this.axis = axis;
    }

    @Override
    public List<Vector3> getPoints() {
        return new LinkedList<>(List.of(getAnchor()));
    }

    @Override
    public Vector3 getIntersectionPoint(Ray ray) {
        Vector3 rayOrigin = ray.getOrigin();
        Vector3 rayDirection = ray.getDirection();
        Vector3 planePoint = getAnchor();
        Vector3 planeNormal = getAxis();

        double denominator = rayDirection.dot(planeNormal);
        if (Math.abs(denominator) < 1e-6)
            return null;

        double numerator = planePoint.subtract(rayOrigin).dot(planeNormal);
        double t = numerator / denominator;

        if (t < 0)
            return null;
        return rayOrigin.add(rayDirection.scale(t));
    }

    public Vector3 getAxis() {
        return axis;
    }

    @Override
    public Vector3 getNormal(Vector3 hitPoint) {
        return axis;
    }

    public void setAxis(Vector3 axis) {
        this.axis = axis;
    }

    @Override
    public double distanceToEdge(Vector3 point) {
        // Lines do not have edges
        return Double.POSITIVE_INFINITY;
    }
}
