package gameboy.utilities.data;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import gameboy.core.enums.Token;
import gameboy.lights.Light;
import gameboy.materials.Material;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;

public class PixelData {
    private RayHit hit;
    private Ray ray;
    private Shape shape;
    private Material material;
    private Color color;
    private HashMap<Token, String> options;
    private LinkedList<Shape> objectsExcl;
    private List<Light> lights;

    public PixelData(RayHit hit, List<Light> lights, List<Shape> objects, HashMap<Token, String> options) {
        objectsExcl = new LinkedList<>(objects);
        objectsExcl.remove(hit.getObject());
        this.ray = hit.getRay();
        this.shape = hit.getObject();
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
        if (options.get(Token.SHADE) != null && options.get(Token.SHADE).equals("false"))
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
