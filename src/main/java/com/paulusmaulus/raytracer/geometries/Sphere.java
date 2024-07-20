package com.paulusmaulus.raytracer.geometries;

import java.util.LinkedList;
import java.util.List;

import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.math.Ray;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Sphere extends Shape {

    double radius;

    public Sphere(Vector3 anchor, Material material, double radius) {
        super(anchor, material);
        this.radius = radius;
    }

    @Override
    public List<Vector3> getPoints() {
        return new LinkedList<Vector3>(List.of(getAnchor()));
    }

    @Override
    public Vector3 getIntersectionPoint(Ray ray) {
        Vector3 oc = ray.getOrigin().subtract(getAnchor());
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2.0 * oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius * radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return null;
        }
        double t = (-b - Math.sqrt(discriminant)) / (2.0 * a);
        if (t >= 0)
            return ray.getOrigin().add(ray.getDirection().scale(t));
        return null;
    }

    @Override
    public Vector3 getNormal(Vector3 hitPoint) {
        return hitPoint.subtract(getAnchor()).normalize();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public double distanceToEdge(Vector3 point) {
        // Calculate the distance from the point to the edge of the sphere
        return Math.abs(point.subtract(getAnchor()).length() - radius);
    }
}
