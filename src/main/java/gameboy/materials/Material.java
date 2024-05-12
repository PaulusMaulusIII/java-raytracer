package gameboy.materials;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import gameboy.geometries.Cone;
import gameboy.lights.Light;
import gameboy.utilities.Shape3D;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public abstract class Material {
    protected Color color;
    protected double ambient = .1;
    protected double diffuse = .1;
    protected double specular = .1;
    protected double shininess = 32;

    public Material() {
        super();
        this.color = Color.BLACK;
    }

    public Material(Color color) {
        this.color = color;
    }

    public Color getColor(Vector3 point) {
        return this.color;
    }

    public Color shade(RayHit rayHit, List<Light> lights, List<Shape3D> objects) {
        // Vector3 normal = rayHit.getRay().getDirection().invert();
        Vector3 normal = getNormal(rayHit); // Get object surface normal vector
        Vector3 hitPoint = rayHit.getHitPoint(); // Get hitpoint
        Color baseColor = getColor(hitPoint); // Get the specified base color at point on shape

        LinkedList<Color> colors = new LinkedList<>();
        for (Light light : lights) {
            Ray shadowRay = new Ray(hitPoint, light.getAnchor().subtract(hitPoint));
            if (!inShadow(shadowRay, light, objects)) {
                Vector3 lightPosition = light.getAnchor();
                double brightnessFactor = normal.dot(lightPosition.subtract(hitPoint).normalize())
                        / hitPoint.distance(lightPosition) * hitPoint.distance(lightPosition);
                Color shadedColor = multiplyColors(baseColor, light.getColor());
                shadedColor = brighten(shadedColor, brightnessFactor);
                colors.add(brighten(shadedColor, ambient));
            }
            else {
                colors.add(brighten(new Color(1, 1, 1), ambient * 2));
            }
        }

        int red = 0;
        int green = 0;
        int blue = 0;
        double colorFactor = 1.0 / colors.size();
        for (Color color : colors) {
            red += (color.getRed() * colorFactor);
            green += (color.getGreen() * colorFactor);
            blue += (color.getBlue() * colorFactor);
        }

        return new Color(red, green, blue);
    }

    public boolean inShadow(Ray ray, Light light, List<Shape3D> objects) {
        for (Shape3D shape3d : objects) {
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
        int red = ((color1.getRed() / 255) * (color2.getRed() / 255) * 255) / 2;
        int green = ((color1.getGreen() / 255) * (color2.getGreen() / 255) * 255) / 2;
        int blue = ((color1.getBlue() / 255) * (color2.getBlue() / 255) * 255) / 2;

        return new Color(red, green, blue);
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

    public abstract Vector3 getNormal(RayHit hit);

    @Override
    public String toString() {
        return super.toString() + "\nColor: " + color + "\nDiffuse: " + diffuse + "\nSpecular: " + specular
                + "\nShininess: " + shininess;
    }
}
