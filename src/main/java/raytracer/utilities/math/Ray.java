package raytracer.utilities.math;

import java.util.List;

import raytracer.geometries.additional.Arrow;
import raytracer.utilities.Object3D;
import raytracer.utilities.Shape;

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

    public RayHit cast(List<Object3D> objects) {
        RayHit hit = null;
        for (Object3D object3d : objects) {
            if (object3d instanceof Shape && !(object3d instanceof Arrow)) {
                Vector3 hitPoint = ((Shape) object3d).getIntersectionPoint(this);
                if (hitPoint != null) {
                    RayHit hit2 = new RayHit(this, object3d, (Shape) object3d, hitPoint);
                    if (hit == null) {
                        hit = hit2;
                    }
                    else if (getOrigin().distance(hit.getHitPoint()) > getOrigin().distance(hitPoint)) {
                        hit = hit2;
                    }
                }
            }
        }
        return hit;
    }

    public RayHit castArrows(List<Object3D> objects) {
        RayHit hit = null;
        for (Object3D object3d : objects) {
            if (object3d instanceof Shape && object3d instanceof Arrow) {
                Vector3 hitPoint = ((Shape) object3d).getIntersectionPoint(this);
                if (hitPoint != null) {
                    RayHit hit2 = new RayHit(this, object3d, (Shape) object3d, hitPoint);
                    if (hit == null) {
                        hit = hit2;
                    }
                    else if (getOrigin().distance(hit.getHitPoint()) > getOrigin().distance(hitPoint)) {
                        hit = hit2;
                    }
                }
            }
        }
        return hit;
    }
}
