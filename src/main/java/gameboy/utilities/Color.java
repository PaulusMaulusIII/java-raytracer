package gameboy.utilities;

public class Color {

	int red;
	int green;
	int blue;

	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color(double red, double green, double blue) {
		this.red = clamp((int) red * 255);
		this.green = clamp((int) green * 255);
		this.blue = clamp((int) blue * 255);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public Color multiply(Color other) {
		float red = ((getRed() / 255f) * (other.getRed() / 255f) * 255) / 2;
		float green = ((getGreen() / 255f) * (other.getGreen() / 255f) * 255) / 2;
		float blue = ((getBlue() / 255f) * (other.getBlue() / 255f) * 255) / 2;

		return new Color((int) red, (int) green, (int) blue);
	}

	public Color brighten(double factor) {
		int red = (int) (getRed() * (factor + 1));
		int green = (int) (getGreen() * (factor + 1));
		int blue = (int) (getBlue() * (factor + 1));

		return new Color(clamp(red), clamp(green), clamp(blue));
	}

	public Color interpolate(Color other, double ratio) {
		int red = (int) (getRed() * (1 - ratio) + other.getRed() * ratio);
		int green = (int) (getGreen() * (1 - ratio) + other.getGreen() * ratio);
		int blue = (int) (getBlue() * (1 - ratio) + other.getBlue() * ratio);

		return new Color(clamp(red), clamp(green), clamp(blue));
	}

	public Color add(Color other) {

		int red = getRed() + other.getRed();
		int green = getGreen() + other.getGreen();
		int blue = getBlue() + other.getBlue();

		return new Color(clamp(red), clamp(green), clamp(blue));
	}

	private int clamp(int val) {
		return Math.min(Math.max(0, val), 255);
	}

	public java.awt.Color toAWT() {
		return new java.awt.Color(red, green, blue);
	}

	@Override
	public String toString() {
		return "[" + red + ", " + green + ", " + blue + "]";
	}

	public static final Color WHITE = new Color(1d, 1d, 1d);
	public static final Color LIGHT_GRAY = new Color(.75, .75, .75);
	public static final Color GRAY = new Color(.5, .5, .5);
	public static final Color DARK_GRAY = new Color(.25, .25, .25);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color RED = new Color(1d, 0, 0);
	public static final Color GREEN = new Color(0, 1d, 0);
	public static final Color BLUE = new Color(0, 0, 1d);
	public static final Color YELLOW = new Color(0, 1d, 1d);
	public static final Color MAGENTA = new Color(1d, 0, 1d);
	public static final Color CYAN = new Color(0, 1d, 1d);
	public static final Color PINK = new Color(1d, .8, .8);
}
