package gameboy.lights;

import java.awt.Color;

import gameboy.utilities.math.Vector3;

public class Light {
    private Vector3 anchor;
    private Color color;

    public Light(Vector3 anchor) {
        this.anchor = anchor;
        this.color = Color.WHITE;
    }

    public void setAnchor(Vector3 anchor) {
        this.anchor = anchor;
    }

    public Light(Vector3 anchor, Color color) {
        this.anchor = anchor;
        this.color = color;
    }

    public Vector3 getAnchor() {
        return anchor;
    }

    public Color getColor() {
        return color;
    }
}
