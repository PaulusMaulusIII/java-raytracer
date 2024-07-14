package raytracer.lights;

import raytracer.utilities.Color;
import raytracer.utilities.Object3D;
import raytracer.utilities.math.Vector3;

public class Light extends Object3D {
    private Color color;
    private double intensity = 1;

    public Light(Vector3 anchor, Color color) {
        super(anchor);
        this.color = color;
    }

    public Light(Vector3 anchor) {
        this(anchor, Color.WHITE);
    }

    public Light(Vector3 anchor, Color color, double intensity) {
        this(anchor, color);
        this.intensity = intensity;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double getIntensity() {
        return intensity;
    }

    @Override
    public String toString() {
        return getClass().getName().replace("gameboy.lights.", "") + "@" + Integer.toHexString(hashCode());
    }
}
