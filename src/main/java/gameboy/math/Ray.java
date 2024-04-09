package gameboy.math;

public class Ray {
    private Vector3 origin;
    private Vector3 direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public Vector3 getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return origin.toString() + " | " + direction.toString();
    }

    public Vector3 intersection(Ray other) {
        Vector3 crossDir = this.direction.cross(other.direction);
        Vector3 dist = other.origin.subtract(this.origin);

        if (crossDir.magnitude() < 1e-6) {
            return null;
        }

        double t = dist.dot(crossDir) / crossDir.dot(crossDir);
        double u = dist.dot(crossDir) / crossDir.dot(crossDir);

        if (t >= 0 && u >= 0 && u <= 1)
            return this.origin.add(this.direction.scale(t));
        return null;
    }

}
