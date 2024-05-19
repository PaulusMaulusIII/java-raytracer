package gameboy.utilities.data;

import gameboy.utilities.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import gameboy.core.enums.Properties;
import gameboy.lights.Light;
import gameboy.utilities.Material;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;

public class PixelData {
    private RayHit hit;
    private Ray ray;
    private Shape shape;
    private Material material;
    private Color color;
    private HashMap<Properties, String> options;
    private LinkedList<Shape> objectsExcl;
    private List<Light> lights;

    public PixelData(RayHit hit, List<Light> lights, List<Shape> objects, HashMap<Properties, String> options) {
        objectsExcl = new LinkedList<>(objects);
        objectsExcl.remove(hit.getShape());
        this.ray = hit.getRay();
        this.shape = hit.getShape();
        this.material = shape.getMaterial();
        this.color = material.getColor(hit.getHitPoint());
        this.hit = hit;
        this.lights = lights;
        this.options = options;
    }

    public RayHit getHit() {
        return hit;
    }

    public Color getColor() {
        if (options.get(Properties.SHADE) != null && options.get(Properties.SHADE).equals("false"))
            return color;

        return material.shade(hit, lights, objectsExcl);
    }

    public Material getMaterial() {
        return material;
    }

    public Ray getRay() {
        return ray;
    }

    public Shape getShape() {
        return shape;
    }
}
