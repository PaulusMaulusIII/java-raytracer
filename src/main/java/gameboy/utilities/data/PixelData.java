package gameboy.utilities.data;

import gameboy.utilities.Color;
import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Material;
import gameboy.utilities.Object3D;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;

public class PixelData {
    private RayHit hit;
    private Ray ray;
    private Object3D object;
    private Shape shape;
    private Material material;
    private List<Object3D> objects;
    private List<Light> lights;

    public PixelData(RayHit hit, List<Light> lights, List<Object3D> objects) {
        this.objects = objects;
        this.ray = hit.getRay();
        this.object = hit.getObject();
        this.shape = hit.getShape();
        this.material = shape.getMaterial();
        this.hit = hit;
        this.lights = lights;
    }

    public RayHit getHit() {
        return hit;
    }

    public Color getColor() {
        return material.getShader().shade(hit, lights, objects, material);
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

    public Object3D getObject() {
        return object;
    }
}
