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

        // Calculate the denominator
        double denominator = rayDirection.dot(planeNormal);

        // If the denominator is 0, the ray is parallel to the plane and there is no
        // intersection
        if (Math.abs(denominator) < 1e-6) {
            return null;
        }

        // Calculate the numerator
        double numerator = planePoint.subtract(rayOrigin).dot(planeNormal);

        // Calculate the intersection distance t
        double t = numerator / denominator;

        // If t is negative, the intersection is behind the ray's origin
        if (t < 0) {
            return null;
        }

        // Calculate the intersection point
        return rayOrigin.add(rayDirection.scale(t));
    }

    public Vector3 getAxis() {
        return axis;
    }

    @Override
    public Vector3 getNormal(Vector3 hitPoint) {
        return axis;
    }
}
