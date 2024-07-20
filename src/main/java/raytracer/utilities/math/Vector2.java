package raytracer.utilities.math;

public class Vector2 {

	public double x, y;

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2 rotate(double roll) {
		// Rotate around the Z axis (roll)
		double cosRoll = Math.cos(roll);
		double sinRoll = Math.sin(roll);
		double rotatedX = x * cosRoll - y * sinRoll;
		double rotatedY = y * cosRoll + x * sinRoll;

		return new Vector2(rotatedX, rotatedY);
	}

	public double distance(Vector2 other) {
		Vector2 difference = this.subtract(other);
		return (double) Math.sqrt(difference.x * difference.x + difference.y * difference.y);
	}

	public Vector2 add(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}

	public Vector2 subtract(Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}

	public Vector2 scale(double scalar) {
		return new Vector2(x * scalar, y * scalar);
	}

	public double magnitude() {
		return (double) Math.sqrt(x * x + y * y);
	}

	public double length() {
		return x + y;
	}

	public Vector2 normalize() {
		double magnitude = magnitude();
		return new Vector2(x / magnitude, y / magnitude);
	}

	public Vector2 cross(Vector2 other) {
		Vector3 cross = new Vector3(x, y, 0).cross(new Vector3(other.x, other.y, 0));
		return new Vector2(cross.x, cross.y);
	}

	public double dot(Vector2 other) {
		return this.x * other.x + this.y * other.y;
	}

	public double[] toArray() {
		return new double[] {
				x, y
		};
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	public boolean isParallel(Vector2 other) {
		return Math.abs(this.dot(other) - 1) < 1e-6 || Math.abs(this.dot(other) + 1) < 1e-6;
	}

	public Vector2 invert() {
		return new Vector2(-x, -y);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
}