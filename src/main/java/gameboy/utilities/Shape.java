package gameboy.utilities;

import java.util.List;

import gameboy.utilities.math.Ray;
import gameboy.utilities.math.Vector3;

public abstract class Shape extends Object3D {

    Material material;

    public Shape(Vector3 anchor, Material material) {
        setAnchor(anchor);
        setMaterial(material);
        this.material.setShape(this);
    }

    /**
     * Returns {@code true} if {@code Material} property is not {@code null}
     * 
     * @return {@code true} if {@code Material != null}
     */
    public boolean hasMaterial() {
        return material != null;
    }

    /**
     * Sets new {@code Material} material property
     * 
     * @param material {@code Material} new material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Gets current {@code Material} material property
     * 
     * @return current {@code Material} material property
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Gets {@code List<Vector3>} of {@code Vector3} significant points
     * 
     * @return {@code List<Vector3>} of {@code Vector3} significant points
     */
    public abstract List<Vector3> getPoints();

    /**
     * Gets {@code Vector3} intersection point of this {@code Shape3D} with
     * {@code Ray} ray, else returns {@code null}
     * 
     * @param ray {@code Ray} to be checked if intersecting with {@code Shape3D}
     * @return {@code null} if {@code Ray} does not intersect {@code Shape3D}, else
     *         {@code Vector3} intersection point with {@code Shape3D}
     */
    public abstract Vector3 getIntersectionPoint(Ray ray);

    /**
     * Calculates {@code Vector3} surface normal vector at {@code Vector3} hitPoint
     * 
     * @param hitPoint {@code Vector3}
     * @return {@code Vector3} surface normal vector at {@code Vector3} hitPoint
     */
    public abstract Vector3 getNormal(Vector3 hitPoint);

    public abstract double distanceToEdge(Vector3 point);

    @Override
    public String toString() {
        return getClass().getName().replace("gameboy.geometries.", "") + "@" + Integer.toHexString(hashCode());
    }
}
