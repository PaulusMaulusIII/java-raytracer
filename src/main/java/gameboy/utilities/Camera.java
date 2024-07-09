package gameboy.utilities;

import gameboy.utilities.math.Vector3;

public class Camera extends Object3D {
    private double pitch = 0;
    private double yaw = 0;
    private double fov = 0;

    public Camera(Vector3 position, double fov) {
        super(position);
        this.fov = (double) fov;
    }

    public Camera(Vector3 position, double fov, double pitch, double yaw) {
        this(position, fov);
        setPitch(pitch);
        setYaw(yaw);
    }

    /**
     * Gets {@code double} yaw property in {@code Radians}
     * 
     * @return {@code double} yaw in {@code Radians}
     */

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public void setFOV(double fov) {
        this.fov = fov;
    }

    public double getFOV() {
        return fov;
    }

    public void translate(Vector3 vec) {
        this.anchor = this.anchor.add(vec);
    }

    @Override
    public String toString() {
        return getClass().getName().replace("gameboy.utilities.", "") + "@" + Integer.toHexString(hashCode());
    }
}
