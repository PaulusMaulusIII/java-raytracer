package gameboy.utilities;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public abstract class Material {

    protected final Color color;
    protected Shape shape;

    protected final double AMBIENT = .05;
    protected double reflectiveness = 0;

    public Material(Color color) {
        this.color = color;
    }

    public Color getColor(Vector3 point) {
        return this.color;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setReflectiveness(double reflectiveness) {
        this.reflectiveness = reflectiveness;
    }

    public Color shade(RayHit rayHit, List<Light> lights, List<Shape> objects) {
        Vector3 incidentDirection = rayHit.getRay().getDirection();
        Vector3 normal = rayHit.getShape().getNormal(rayHit); // Get object surface normal vector
        Vector3 hitPoint = rayHit.getHitPoint(); // Get hitpoint
        Color baseColor = getColor(hitPoint); // Get the specified base color at point on shape
        Color shadowColor = interpolate(Color.BLACK, baseColor, AMBIENT);

        LinkedList<Color> colors = new LinkedList<>();
        for (Light light : lights) {
            Ray shadowRay = new Ray(hitPoint, light.getAnchor().subtract(hitPoint));
            Vector3 reflectionDirection = incidentDirection.subtract(normal.scale(2 * incidentDirection.dot(normal)));
            if (!inShadow(shadowRay, light, objects)) {
                if (reflectiveness > 0) {
                    objects.add(shape);
                    colors.add(calculateReflection(new Ray(hitPoint, reflectionDirection), light, objects, 1000));
                }
                else
                    colors.add(calculateShadedColor(light, normal, baseColor, hitPoint, shadowColor));
            }
            else {
                if (reflectiveness > 0) {
                    objects.add(shape);
                    colors.add(interpolate(shadowColor,
                            calculateReflection(new Ray(hitPoint, reflectionDirection), light, objects, 1000),
                            1 - reflectiveness));
                }
                else
                    colors.add(shadowColor);
            }
        }

        Color finalColor = Color.BLACK;
        for (Color color : colors) {
            finalColor = add(finalColor, color);
        }

        return finalColor;
    }

    private Color calculateReflection(Ray ray, Light light, List<Shape> objects, int depth) {
        if (depth <= 0) {
            return Color.BLACK;
        }

        RayHit hit = ray.castRay(objects);

        if (hit != null) {
            Vector3 incidentDirection = ray.getDirection().normalize();
            Vector3 normal = hit.getShape().getNormal(hit);
            if (hit.getShape().getMaterial().reflectiveness > 0) {
                Vector3 reflectedDirection = incidentDirection.subtract(normal.scale(2 * incidentDirection.dot(normal)))
                        .normalize();
                Ray reflectedRay = new Ray(hit.getHitPoint(), reflectedDirection);
                Color reflectedColor = calculateReflection(reflectedRay, light, objects, depth - 1);
                return interpolate(hit.getShape().getMaterial().getColor(hit.getHitPoint()), reflectedColor,
                        hit.getShape().getMaterial().reflectiveness);
            }
            else {
                Vector3 hitPoint = hit.getHitPoint();
                Shape shapeHit = hit.getShape();
                Color baseColor = shapeHit.getMaterial().getColor(hitPoint);
                Color shadowColor = interpolate(Color.BLACK, baseColor, AMBIENT);
                Color shadedColor = calculateShadedColor(light, normal, baseColor, hitPoint, shadowColor);
                return interpolate(getColor(ray.getOrigin()), shadedColor, reflectiveness);
            }
        }
        else {
            return interpolate(getColor(ray.getOrigin()), new Color(25, 25, 25), reflectiveness);
        }
    }

    private Color calculateShadedColor(Light light, Vector3 normal, Color baseColor, Vector3 hitPoint,
            Color shadowColor) {
        Vector3 lightPosition = light.getAnchor();
        double brightnessFactor = normal.dot(lightPosition.subtract(hitPoint).normalize())
                / hitPoint.distance(lightPosition) * hitPoint.distance(lightPosition);
        Color shadedColor = interpolate(Color.BLACK, baseColor, AMBIENT);
        shadedColor = multiply(baseColor, light.getColor());
        shadedColor = brighten(shadedColor, brightnessFactor);
        if (shadedColor.getRGB() <= shadowColor.getRGB())
            return (shadowColor);
        else
            return (shadedColor);
    }

    private boolean inShadow(Ray ray, Light light, List<Shape> objects) {
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

    private Color multiply(Color color1, Color color2) {
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

    private Color interpolate(Color color1, Color color2, double ratio) {
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
}
