package com.paulusmaulus.raytracer.geometries;

import java.util.LinkedList;
import java.util.List;

import com.paulusmaulus.raytracer.utilities.Material;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.math.Ray;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class Cube extends Shape {

    private final Vector3[] points = new Vector3[8];

    double sideLength;

    public Cube(Vector3 anchor, Material material, double sideLength) {
        super(anchor, material);
        setMaterial(material);
        this.sideLength = sideLength;

        double radius = sideLength / 2.0;

        for (int i = 0; i < 8; i++) {
            double xOffset = (i & 1) == 0 ? -radius : radius;
            double yOffset = (i & 2) == 0 ? -radius : radius;
            double zOffset = (i & 4) == 0 ? -radius : radius;

            points[i] = new Vector3(anchor.x + xOffset, anchor.y + yOffset, anchor.z + zOffset);
        }
        setName(toString());
    }

    @Override
    public List<Vector3> getPoints() {
        return new LinkedList<>(List.of(points));
    }

    @Override
    public Vector3 getIntersectionPoint(Ray ray) {
        double tMin = 0;
        double tMax = Double.POSITIVE_INFINITY;
        double halfSideLength = sideLength / 2d;

        boolean hit = false;

        for (int i = 0; i < 3; i++) {
            if (Math.abs(ray.getDirection().toArray()[i]) < 1e-6) {
                if (ray.getOrigin().toArray()[i] < getAnchor().toArray()[i] - halfSideLength
                        || ray.getOrigin().toArray()[i] > getAnchor().toArray()[i] + halfSideLength)
                    return null;
            }
            else {
                double t1 = (getAnchor().toArray()[i] - halfSideLength - ray.getOrigin().toArray()[i])
                        / ray.getDirection().toArray()[i];
                double t2 = (getAnchor().toArray()[i] + halfSideLength - ray.getOrigin().toArray()[i])
                        / ray.getDirection().toArray()[i];

                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tMin = Math.max(tMin, t1);
                tMax = Math.min(tMax, t2);

                if (tMin > tMax)
                    return null;

            }

            if (tMin > 0)
                hit = true;
        }

        if (hit)
            return ray.getOrigin().add(ray.getDirection().scale(tMin));
        return null;
    }

    public int determineCubeSide(Vector3 point) {
        double[] pointArray = point.toArray();
        double[] anchorArray = getAnchor().toArray();

        double maxDistance = 0;
        int maxAxis = -1;
        for (int i = 0; i < 3; i++) {
            double distance = Math.abs(pointArray[i] - anchorArray[i]);
            if (distance > maxDistance) {
                maxDistance = distance;
                maxAxis = i;
            }
        }

        if (maxAxis == 0) {
            return pointArray[0] > anchorArray[0] ? 0 : 1;
        }
        else if (maxAxis == 1) {
            return pointArray[1] > anchorArray[1] ? 2 : 3;
        }
        else {
            return pointArray[2] > anchorArray[2] ? 4 : 5;
        }
    }

    @Override
    public Vector3 getNormal(Vector3 hitPoint) {
        switch (determineCubeSide(hitPoint)) {
        case 0:
            return new Vector3(1, 0, 0);
        case 1:
            return new Vector3(-1, 0, 0);
        case 2:
            return new Vector3(0, 1, 0);
        case 3:
            return new Vector3(0, -1, 0);
        case 4:
            return new Vector3(0, 0, 1);
        case 5:
            return new Vector3(0, 0, -1);

        default:
            return null;
        }
    }

    public double getSideLength() {
        return sideLength;
    }

    public void setSideLength(double value) {
        this.sideLength = value;
    }

    @Override
    public double distanceToEdge(Vector3 point) {
        // Calculate the distance from the point to the nearest edge of the cube
        double halfSide = sideLength / 2;
        Vector3 anchor = getAnchor();
        double dx = Math.max(Math.abs(point.x - anchor.x) - halfSide, 0);
        double dy = Math.max(Math.abs(point.y - anchor.y) - halfSide, 0);
        double dz = Math.max(Math.abs(point.z - anchor.z) - halfSide, 0);
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
