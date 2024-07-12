package gameboy.utilities.math;

import gameboy.utilities.Object3D;
import gameboy.utilities.Shape;

public class RayHit {
    private Ray ray;
    private Object3D object;
    private Shape shape;
    private Vector3 hitPoint;

    public RayHit(Ray ray, Object3D object, Shape shape, Vector3 hitPoint) {
        this.ray = ray;
        this.object = object;
        this.shape = shape;
        this.hitPoint = hitPoint;
    }

    public Ray getRay() {
        return ray;
    }

    public Object3D getObject() {
        return object;
    }

    public Vector3 getHitPoint() {
        return hitPoint;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return super.toString() + ":\n" + ray.toString() + "\n" + object.toString() + "\n" + hitPoint.toString();
    }
}
