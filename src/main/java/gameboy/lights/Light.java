package gameboy.lights;

import gameboy.utilities.Color;
import gameboy.utilities.Object3D;
import gameboy.utilities.math.Vector3;

public class Light extends Object3D {
    private Color color;

    public Light(Vector3 anchor) {
        super(anchor);
        this.color = Color.WHITE;
    }

    public Light(Vector3 anchor, Color color) {
        super(anchor);
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return getClass().getName().replace("gameboy.lights.", "") + "@" + Integer.toHexString(hashCode());
    }
}
