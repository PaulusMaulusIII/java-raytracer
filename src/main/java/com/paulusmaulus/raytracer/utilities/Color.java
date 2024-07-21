package com.paulusmaulus.raytracer.utilities;

public class Color {

	int red;
	int green;
	int blue;
	double hue;
	double saturation;
	double value;

	public Color(int red, int green, int blue) {
		this.red = clamp(red);
		this.green = clamp(green);
		this.blue = clamp(blue);
		toHSV();
	}

	public Color(double red, double green, double blue) {
		this.red = clamp((int) red * 255);
		this.green = clamp((int) green * 255);
		this.blue = clamp((int) blue * 255);
		toHSV();
	}

	private Color(double hue, double saturation, double value, boolean isHSV) {
		this.hue = clampRad(hue);
		this.saturation = clamp(saturation);
		this.value = clamp(value);
		toRGB();
	}

	public static Color hsv(double hue, double saturation, double value) {
		return new Color(hue, saturation, value, true);
	}

	public static Color fromInt(int argb) {
		int b = (argb) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int r = (argb >> 16) & 0xFF;

		return new Color(r, g, b);
	}

	private Color(int hue, double saturation, double value, boolean isHSV) {
		this.hue = clampDeg(hue);
		this.saturation = clamp(saturation);
		this.value = clamp(value);
		toRGB();
	}

	public static Color hsv(int hue, double saturation, double value) {
		return new Color(hue, saturation, value, true);
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public double getHue() {
		return hue;
	}

	public void setHue(double hue) {
		this.hue = hue;
	}

	public double getSaturation() {
		return saturation;
	}

	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void toHSV() {
		double r = red / 255.0;
		double g = green / 255.0;
		double b = blue / 255.0;

		double max = Math.max(r, Math.max(g, b));
		double min = Math.min(r, Math.min(g, b));
		double delta = max - min;

		if (delta == 0) {
			hue = 0;
		}
		else if (max == r) {
			hue = 60 * (((g - b) / delta) % 6);
		}
		else if (max == g) {
			hue = 60 * (((b - r) / delta) + 2);
		}
		else {
			hue = 60 * (((r - g) / delta) + 4);
		}

		if (hue < 0) {
			hue += 360;
		}

		saturation = max == 0 ? 0 : (delta / max);
		value = max;
	}

	public void toRGB() {
		double c = value * saturation;
		double x = c * (1 - Math.abs((hue / 60) % 2 - 1));
		double m = value - c;

		double rPrime, gPrime, bPrime;

		if (0 <= hue && hue < 60) {
			rPrime = c;
			gPrime = x;
			bPrime = 0;
		}
		else if (60 <= hue && hue < 120) {
			rPrime = x;
			gPrime = c;
			bPrime = 0;
		}
		else if (120 <= hue && hue < 180) {
			rPrime = 0;
			gPrime = c;
			bPrime = x;
		}
		else if (180 <= hue && hue < 240) {
			rPrime = 0;
			gPrime = x;
			bPrime = c;
		}
		else if (240 <= hue && hue < 300) {
			rPrime = x;
			gPrime = 0;
			bPrime = c;
		}
		else {
			rPrime = c;
			gPrime = 0;
			bPrime = x;
		}

		red = clamp((int) ((rPrime + m) * 255));
		green = clamp((int) ((gPrime + m) * 255));
		blue = clamp((int) ((bPrime + m) * 255));
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

	public Color multiply(double factor) {
		int red = (int) (getRed() * (factor));
		int green = (int) (getGreen() * (factor));
		int blue = (int) (getBlue() * (factor));

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

	public Color add(double brightness) {
		return new Color(Math.min(1, red + brightness), Math.min(1, green + brightness),
				Math.min(1, blue + brightness));
	}

	private int clamp(int val) {
		return Math.min(Math.max(0, val), 255);
	}

	private double clamp(double val) {
		return Math.min(Math.max(0, val), 1);
	}

	private int clampDeg(int val) {
		return Math.min(Math.max(0, val), 360);
	}

	private double clampRad(double val) {
		return Math.min(Math.max(0, val), 2);
	}

	private static double lerp(double a, double b, double t) {
		return a + t * (b - a);
	}

	public static Color lerp(Color a, Color b, double t) {
		return new Color(lerp(a.getRed() / 255.0, b.getRed() / 255.0, t),
				lerp(a.getGreen() / 255.0, b.getGreen() / 255.0, t), lerp(a.getBlue() / 255.0, b.getBlue() / 255.0, t));
	}

	@Override
	public String toString() {
		return "[" + red + ", " + green + ", " + blue + "]";
	}

	public static final Color WHITE = new Color(1d, 1d, 1d);
	public static final Color LIGHT_GRAY = new Color(0.75, 0.75, 0.75);
	public static final Color GRAY = new Color(0.5, 0.5, 0.5);
	public static final Color DARK_GRAY = new Color(0.25, 0.25, 0.25);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color RED = new Color(1d, 0, 0);
	public static final Color GREEN = new Color(0, 1d, 0);
	public static final Color BLUE = new Color(0, 0, 1d);
	public static final Color YELLOW = new Color(1d, 1d, 0);
	public static final Color MAGENTA = new Color(1d, 0, 1d);
	public static final Color CYAN = new Color(0, 1d, 1d);
	public static final Color PINK = new Color(1d, 0.8, 0.8);

	public java.awt.Color toAWT() {
		return new java.awt.Color(red, green, blue);
	}
}
