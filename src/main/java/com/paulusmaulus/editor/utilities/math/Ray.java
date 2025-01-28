package com.paulusmaulus.editor.utilities.math;

import java.util.List;

import com.paulusmaulus.editor.geometries.additional.Arrow;
import com.paulusmaulus.editor.utilities.Object3D;
import com.paulusmaulus.editor.utilities.Shape;

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
        double distance = Double.POSITIVE_INFINITY;
        for (Object3D object3d : objects) {
            if (object3d instanceof Shape && !(object3d instanceof Arrow)) {
                Vector3 hitPoint = ((Shape) object3d).getIntersectionPoint(this);
                if (hitPoint != null && hitPoint.distance(getOrigin()) < distance) {
                    hit = new RayHit(this, object3d, (Shape) object3d, hitPoint);
                    distance = hitPoint.distance(getOrigin());
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
                    hit = new RayHit(this, object3d, (Shape) object3d, hitPoint);
                }
            }
        }
        return hit;
    }
}
