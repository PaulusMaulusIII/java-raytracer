package gameboy.utilities.math;

public class Vector3 {
    public double x, y, z;
    double[] axis = { x, y, z };

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 rotate(double pitch, double yaw) {

        double rotatedY = (double) (y * Math.cos(pitch) - z * Math.sin(pitch));
        double rotatedZ = (double) (y * Math.sin(pitch) + z * Math.cos(pitch));
        double rotatedX = (double) (x * Math.cos(yaw) + rotatedZ * Math.sin(yaw));

        rotatedZ = (double) (-x * Math.sin(yaw) + rotatedZ * Math.cos(yaw));

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
        return new double[] { x, y, z };
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public double[] getAxis() {
        return axis;
    }

    public boolean isParallel(Vector3 other) {
        return Math.abs(this.dot(other) - 1) < 1e-6 || Math.abs(this.dot(other) + 1) < 1e-6;
    }
}