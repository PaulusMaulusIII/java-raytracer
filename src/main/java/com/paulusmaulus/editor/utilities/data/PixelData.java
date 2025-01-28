package com.paulusmaulus.editor.utilities.data;

import com.paulusmaulus.editor.utilities.Color;

public class PixelData {
    private Color color;
    private double distance;
    private double emission;

    public PixelData(Color color, double distance, double emission) {
        this.color = color;
        this.distance = distance;
        this.emission = emission;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }

    public double getEmission() {
        return emission;
    }

    @Override
    public String toString() {
        return "[" + color + ", " + distance + ", " + emission + "]";
    }
}
