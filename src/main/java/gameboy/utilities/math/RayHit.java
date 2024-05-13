package gameboy.utilities.math;

import gameboy.utilities.Shape;

public class RayHit {
    private Ray ray;
    private Shape shape;
    private Vector3 hitPoint;

    public RayHit(Ray ray, Shape shape, Vector3 hitPoint) {
        this.ray = ray;
        this.shape = shape;
        this.hitPoint = hitPoint;
    }

    public Ray getRay() {
        return ray;
    }

    public Shape getShape() {
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
