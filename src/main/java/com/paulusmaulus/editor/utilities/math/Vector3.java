package com.paulusmaulus.editor.utilities.math;

public class Vector3 {
    public double x, y, z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 rotate(double pitch, double yaw, double roll) {
        // Rotate around the Z axis (roll)
        double cosRoll = Math.cos(roll);
        double sinRoll = Math.sin(roll);
        double rotatedX = x * cosRoll - y * sinRoll;
        double rotatedY = x * sinRoll + y * cosRoll;
        double rotatedZ = z;

        // Rotate around the X axis (pitch)
        double cosPitch = Math.cos(pitch);
        double sinPitch = Math.sin(pitch);
        double tempY = rotatedY * cosPitch - rotatedZ * sinPitch;
        rotatedZ = rotatedY * sinPitch + rotatedZ * cosPitch;
        rotatedY = tempY;

        // Rotate around the Y axis (yaw)
        double cosYaw = Math.cos(yaw);
        double sinYaw = Math.sin(yaw);
        double tempX = rotatedX * cosYaw + rotatedZ * sinYaw;
        rotatedZ = -rotatedX * sinYaw + rotatedZ * cosYaw;
        rotatedX = tempX;

        return new Vector3(rotatedX, rotatedY, rotatedZ);
    }

    public double distance(Vector3 other) {
        Vector3 difference = this.subtract(other);
        return (double) Math.sqrt(Math.pow(difference.x, 2) + Math.pow(difference.y, 2) + Math.pow(difference.z, 2));
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public Vector3 scale(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    public double magnitude() {
        return (double) Math.sqrt(x * x + y * y + z * z);
    }

    public double length() {
        return x + y + z;
    }

    public Vector3 normalize() {
        double magnitude = magnitude();
        return new Vector3(x / magnitude, y / magnitude, z / magnitude);
    }

    public Vector3 cross(Vector3 other) {
        double newX = this.y * other.z - this.z * other.y;
        double newY = this.z * other.x - this.x * other.z;
        double newZ = this.x * other.y - this.y * other.x;

        return new Vector3(newX, newY, newZ);
    }

    public double dot(Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public double[] toArray() {
        return new double[] {
                x, y, z
        };
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public boolean isParallel(Vector3 other) {
        return Math.abs(this.dot(other) - 1) < 1e-6 || Math.abs(this.dot(other) + 1) < 1e-6;
    }

    public Vector3 invert() {
        return new Vector3(-x, -y, -z);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3)
            if (((Vector3) obj).x == x && ((Vector3) obj).y == y && ((Vector3) obj).z == z)
                return true;
        return false;
    }

    public Vector3 min(Vector3 pos) {
        if (pos.length() < length()) {
            return pos;
        }
        return this;
    }

    public Vector3 max(Vector3 pos) {
        if (pos.length() > length()) {
            return pos;
        }
        return this;
    }
}