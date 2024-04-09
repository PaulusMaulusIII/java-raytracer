package gameboy.lights;

import java.util.LinkedList;
import java.util.List;

import gameboy.utilities.Shape3D;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.Vector3;
import javafx.scene.paint.Color;

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
