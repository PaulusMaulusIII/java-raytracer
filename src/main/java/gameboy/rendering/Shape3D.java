package gameboy.rendering;

import java.util.List;

import gameboy.math.Ray;
import gameboy.math.Vector3;

public abstract class Shape3D {

    Vector3 anchor;
    Material material;

    public Shape3D(Vector3 anchor) {
        setAnchor(anchor);
    }

    public Shape3D(Material material) {
        setMaterial(material);
    }

    public Shape3D(Vector3 anchor, Material material) {
        setAnchor(anchor);
        setMaterial(material);
    }

    public void setAnchor(Vector3 anchor) {
        this.anchor = anchor;
    }

    public Vector3 getAnchor() {
        return anchor;
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

    public abstract Vector3 getNormal(Vector3 point);
}
