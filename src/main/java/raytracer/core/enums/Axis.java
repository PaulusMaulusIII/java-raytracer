package raytracer.core.enums;

import raytracer.utilities.math.Vector3;

public enum Axis {

    X(new Vector3(1, 0, 0)), Y(new Vector3(0, 1, 0)), Z(new Vector3(0, 0, 1));

    private final Vector3 vector;

    private Axis(Vector3 vector) {
        this.vector = vector;
    }

    public Vector3 getVector() {
        return vector;
    }
}
