package gameboy.rendering;

import gameboy.math.RayHit;
import gameboy.math.Vector3;
import javafx.scene.paint.Color;
import java.util.List;

public abstract class Material {
    protected Color color;
    protected double ambient = .5;
    protected double diffuse = .1;
    protected double specular = .1;
    protected double shininess = 32;

    public Material() {
        super();
        this.color = Color.BLACK;
    }

    public Material(Color color) {
        this.color = color;
    }

    public Color getColor(Vector3 point) {
        return this.color;
    }

    public abstract Color shade(RayHit rayHit, List<Light> lights);
}
