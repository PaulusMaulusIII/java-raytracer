package gameboy.materials;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public abstract class Material {

    protected Color color;
    protected double ambient = .05;
    protected double diffuse = .1;
    protected double specular = .1;
    protected double shininess = 32;
    protected Shape shape;

    public Material() {}

    public Material(Color color) {
        this.color = color;
    }

    public Color getColor(Vector3 point) {
        return this.color;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Color shade(RayHit rayHit, List<Light> lights, List<Shape> objects) {
        Vector3 normal = rayHit.getObject().getNormal(rayHit); // Get object surface normal vector
        Vector3 hitPoint = rayHit.getHitPoint(); // Get hitpoint
        Color baseColor = getColor(hitPoint); // Get the specified base color at point on shape
        Color shadowColor = interpolateColors(Color.BLACK, baseColor, ambient);

        LinkedList<Color> colors = new LinkedList<>();
        for (Light light : lights) {
            Ray shadowRay = new Ray(hitPoint, light.getAnchor().subtract(hitPoint));
            if (!inShadow(shadowRay, light, objects)) {
                Vector3 lightPosition = light.getAnchor();
                double brightnessFactor = normal.dot(lightPosition.subtract(hitPoint).normalize())
                        / hitPoint.distance(lightPosition) * hitPoint.distance(lightPosition);
                Color shadedColor = interpolateColors(Color.BLACK, baseColor, ambient);
                shadedColor = multiplyColors(baseColor, light.getColor());
                shadedColor = brighten(shadedColor, brightnessFactor);
                if (shadedColor.getRGB() <= shadowColor.getRGB())
                    colors.add(shadowColor);
                else
                    colors.add(shadedColor);
            }
            else {
                colors.add(shadowColor);
            }
        }

        Color finalColor = Color.BLACK;
        for (Color color : colors) {
            finalColor = add(finalColor, color);
        }

        return finalColor;
    }

    public boolean inShadow(Ray ray, Light light, List<Shape> objects) {
        for (Shape shape3d : objects) {
            Vector3 intersectionPoint = shape3d.getIntersectionPoint(ray);
            if (intersectionPoint != null) {
                double distanceToIntersection = intersectionPoint.subtract(ray.getOrigin()).magnitude();
                double distanceToLight = light.getAnchor().subtract(ray.getOrigin()).magnitude();
                if (distanceToIntersection < distanceToLight) {
                    return true;
                }
            }
        }
        return false;

    }

    private Color multiplyColors(Color color1, Color color2) {
        float red = ((color1.getRed() / 255f) * (color2.getRed() / 255f) * 255) / 2;
        float green = ((color1.getGreen() / 255f) * (color2.getGreen() / 255f) * 255) / 2;
        float blue = ((color1.getBlue() / 255f) * (color2.getBlue() / 255f) * 255) / 2;

        return new Color((int) red, (int) green, (int) blue);
    }

    private Color brighten(Color color, double factor) {
        int red = (int) (color.getRed() * (factor + 1));
        int green = (int) (color.getGreen() * (factor + 1));
        int blue = (int) (color.getBlue() * (factor + 1));

        red = (red > 255) ? 255 : red;
        green = (green > 255) ? 255 : green;
        blue = (blue > 255) ? 255 : blue;

        return new Color(red, green, blue);
    }

    private Color interpolateColors(Color color1, Color color2, double ratio) {
        int red = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
        int green = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
        int blue = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);

        red = (red > 255) ? 255 : red;
        green = (green > 255) ? 255 : green;
        blue = (blue > 255) ? 255 : blue;

        return new Color(red, green, blue);
    }

    private Color add(Color color1, Color color2) {

        int red = color1.getRed() + color2.getRed();
        int green = color1.getGreen() + color2.getGreen();
        int blue = color1.getBlue() + color2.getBlue();

        red = (red > 255) ? 255 : red;
        green = (green > 255) ? 255 : green;
        blue = (blue > 255) ? 255 : blue;

        return new Color(red, green, blue);
    }

    @Override
    public String toString() {
        return super.toString() + "\nColor: " + color + "\nDiffuse: " + diffuse + "\nSpecular: " + specular
                + "\nShininess: " + shininess;
    }
}
