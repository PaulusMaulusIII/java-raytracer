package gameboy.utilities;

import gameboy.shaders.Shader;
import gameboy.utilities.math.Vector3;

public abstract class Material {

    protected Shader shader;
    protected Color color;
    protected Shape shape;

    protected double emission = 1;
    protected double shininess = 128;
    protected double reflectivity = 0;

    public Material(Shader shader, Color color) {
        this.shader = shader;
        this.color = color;
    }

    public Color getColor(Vector3 point) {
        return this.color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Shape getShape() {
        return shape;
    }

    public Vector3 getNormal(Vector3 hitPoint) {
        return shape.getNormal(hitPoint);
    }

    public void setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }

    public double getEmission() {
        return emission;
    }
}
