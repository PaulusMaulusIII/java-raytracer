package gameboy.utilities.data;

import gameboy.lights.Light;
import gameboy.materials.Material;
import gameboy.utilities.Shape3D;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import javafx.scene.paint.Color;
import java.util.List;

public class PixelData {
    private RayHit hit;
    private Ray ray;
    private Shape3D shape;
    private Material material;
    private Color color;

    public PixelData(RayHit hit, List<Light> lights) {
        this.hit = hit;
        if (hit != null) {
            this.ray = hit.getRay();
            this.shape = hit.getShape();
            this.material = shape.getMaterial();
            this.color = material.shade(hit, lights);
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
