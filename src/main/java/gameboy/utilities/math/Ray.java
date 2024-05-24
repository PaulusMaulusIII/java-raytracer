package gameboy.utilities.math;

import java.util.List;

import gameboy.utilities.Shape;

public class Ray {
    private Vector3 origin;
    private Vector3 direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public Vector3 getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return origin.toString() + " | " + direction.toString();
    }

    public Vector3 intersection(Ray other) {
        Vector3 crossDir = this.direction.cross(other.direction);
        Vector3 dist = other.origin.subtract(this.origin);

        if (crossDir.magnitude() < 1e-6) {
            return null;
        }

        double t = dist.dot(crossDir) / crossDir.dot(crossDir);
        double u = dist.dot(crossDir) / crossDir.dot(crossDir);

        if (t >= 0 && u >= 0 && u <= 1)
            return this.origin.add(this.direction.scale(t));
        return null;
    }

    public Vector3 intersectionPoint(Vector3 point) {
        double x_p = point.x;
        double y_p = point.y;
        double z_p = point.z;

        double x_v = getOrigin().x;
        double y_v = getOrigin().y;
        double z_v = getOrigin().z;

        double d_x = getDirection().x;
        double d_y = getDirection().y;
        double d_z = getDirection().z;

        double t;

        if (d_x != 0) {
            t = (x_p - x_v) / d_x;
        }
        else if (d_y != 0) {
            t = (y_p - y_v) / d_y;
        }
        else if (d_z != 0) {
            t = (z_p - z_v) / d_z;
        }
        else {
            return null;
        }

        if ((x_v + t * d_x) == x_p && (y_v + t * d_y) == y_p && (z_v + t * d_z) == z_p) {
            return getOrigin().add(getDirection().scale(t));
        }
        else {
            return null;
        }
    }

    public RayHit cast(List<Shape> objects) {
        RayHit hit = null;
        for (Shape shape : objects) {
            Vector3 hitPoint = shape.getIntersectionPoint(this);
            if (hitPoint != null) {
                RayHit hit2 = new RayHit(this, shape, hitPoint);
                if (hit == null) {
                    hit = hit2;
                }
                else if (getOrigin().distance(hit.getHitPoint()) > getOrigin().distance(hitPoint)) {
                    hit = hit2;
                }
            }
        }
        return hit;
    }
}
