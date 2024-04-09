package gameboy.utilities.data;

import gameboy.lights.Light;
import gameboy.materials.Material;
import gameboy.utilities.Shape3D;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class PixelData {
    private RayHit hit;
    private Ray ray;
    private Shape3D shape;
    private Material material;
    private Color color;

    public PixelData(RayHit hit, List<Light> lights, List<Shape3D> objects) {
        this.hit = hit;
        LinkedList<Shape3D> objectsExcl = new LinkedList<>(objects);
        objectsExcl.remove(hit.getObject());
        if (hit != null) {
            this.ray = hit.getRay();
            this.shape = hit.getObject();
            this.material = shape.getMaterial();
            this.color = material.shade(hit, lights, objectsExcl);
        }
    }

    public RayHit getHit() {
        return hit;
    }

    public Color getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }

    public Ray getRay() {
        return ray;
    }

    public Shape3D getShape() {
        return shape;
    }
}
