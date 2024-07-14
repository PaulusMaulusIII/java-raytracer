package raytracer.geometries;

import java.util.LinkedList;
import java.util.List;

import raytracer.utilities.Material;
import raytracer.utilities.Shape;
import raytracer.utilities.math.Ray;
import raytracer.utilities.math.Vector3;

public class Line extends Shape {

    private final Vector3[] points = new Vector3[2];
    private Vector3 anchor2;
    private Vector3 direction;

    public Line(Vector3 anchor, Vector3 anchor2, Material material) {
        super(anchor, material);
        points[0] = anchor;
        points[1] = anchor2;
        this.anchor2 = anchor2;
        this.direction = anchor2.subtract(anchor).normalize();
    }

    @Override
    public List<Vector3> getPoints() {
        return new LinkedList<>(List.of(points));
    }

    public Vector3 getDirection() {
        return direction;
    }

    public Vector3 getAnchor2() {
        return anchor2;
    }

    @Override
    public Vector3 getIntersectionPoint(Ray ray) {
        return ray.intersection(toRay());
    }

    public Ray toRay() {
        return new Ray(getAnchor(), direction);
    }

    @Override
    public Vector3 getNormal(Vector3 hitPoint) {
        return hitPoint.invert();
    }

    @Override
    public double distanceToEdge(Vector3 point) {
        // Lines do not have edges
        return Double.POSITIVE_INFINITY;
    }
}
