package gameboy.utilities.math;

import gameboy.utilities.Shape3D;

public class RayHit {
    private Ray ray;
    private Shape3D shape;
    private Vector3 hitPoint;

    public RayHit(Ray ray, Shape3D shape, Vector3 hitPoint) {
        this.ray = ray;
        this.shape = shape;
        this.hitPoint = hitPoint;
    }

    public Ray getRay() {
        return ray;
    }

    public Shape3D getObject() {
        return shape;
    }

    public Vector3 getHitPoint() {
        return hitPoint;
    }

    @Override
    public String toString() {
        return super.toString() + ":\n" + ray.toString() + "\n" + shape.toString() + "\n" + hitPoint.toString();
    }
}
