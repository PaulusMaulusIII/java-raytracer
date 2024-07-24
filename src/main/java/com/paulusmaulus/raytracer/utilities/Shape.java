package com.paulusmaulus.raytracer.utilities;

import java.util.List;

import com.paulusmaulus.raytracer.utilities.math.Ray;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public abstract class Shape extends Object3D {

    private Material material;

    public Shape(Vector3 anchor, Material material) {
        setAnchor(anchor);
        setMaterial(material);
        this.material.setShape(this);
        setName(toString());
    }

    public boolean hasMaterial() {
        return material != null;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public abstract List<Vector3> getPoints();

    public abstract Vector3 getIntersectionPoint(Ray ray);

    public abstract Vector3 getNormal(Vector3 hitPoint);

    public abstract double distanceToEdge(Vector3 point);

    @Override
    public String toString() {
        return getClass().getName().replace("raytracer.geometries.", "") + "@" + Integer.toHexString(hashCode());
    }
}