package com.paulusmaulus.editor.utilities;

import com.paulusmaulus.editor.core.interfaces.Shader;
import com.paulusmaulus.editor.utilities.math.Vector3;

public abstract class Material {

    protected Shader shader;
    protected Color color;
    protected Shape shape;

    protected double emission = 1;
    protected double reflectivity = 0.4;
    protected double transparency = 0;

    public Material(Shader shader, Color color) {
        this.shader = shader;
        this.color = color;
    }

    public Material(Shader shader, Color color, double emission, double reflectivity, double transparency) {
        this.shader = shader;
        this.color = color;
        this.emission = emission;
        this.reflectivity = reflectivity;
        this.transparency = transparency;
    }

    public double getEmission() {
        return emission;
    }

    public double getEmissionAt(Vector3 point) {
        return emission;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public double getReflectivityAt(Vector3 point) {
        return reflectivity;
    }

    public double getTransparency() {
        return transparency;
    }

    public double getTransparencyAt(Vector3 point) {
        return transparency;
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

    public void setTransparency(double transparency) {
        this.transparency = transparency;
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

    protected abstract String getName();
}
