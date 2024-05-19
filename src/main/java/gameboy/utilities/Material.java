package gameboy.utilities;

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
        Color shadowColor = Color.BLACK.interpolate(baseColor, AMBIENT);

        LinkedList<Color> colors = new LinkedList<>();
        for (Light light : lights) {
            Ray shadowRay = new Ray(hitPoint, light.getAnchor().subtract(hitPoint));
            Vector3 reflectionDirection = incidentDirection.subtract(normal.scale(2 * incidentDirection.dot(normal)));
            if (!inShadow(shadowRay, light, objects)) {
                if (reflectiveness > 0) {
                    objects.add(shape);
                    baseColor = calculateReflection(new Ray(hitPoint, reflectionDirection), light, objects, 10);
                }
                colors.add(calculateShadedColor(light, normal, baseColor, hitPoint, shadowColor));
            }
            else {
                if (reflectiveness > 0) {
                    objects.add(shape);
                    colors.add(shadowColor.interpolate(
                            calculateReflection(new Ray(hitPoint, reflectionDirection), light, objects, 10),
                            1 - reflectiveness));
                }
                else
                    colors.add(shadowColor);
            }
        }

        Color finalColor = Color.BLACK;
        for (Color color : colors) {
            finalColor = finalColor.add(color);
        }

        return finalColor;
    }

    private Color calculateReflection(Ray ray, Light light, List<Shape> objects, int depth) {
        if (depth <= 0) {
            return Color.BLACK;
        }

        RayHit hit = ray.castRay(objects);

        if (hit != null) { // TODO WAAAAAAY TO HEAVY
            Vector3 hitPoint = hit.getHitPoint();
            Material hitMaterial = hit.getShape().getMaterial();
            Color colorAtHit = hitMaterial.getColor(hitPoint);
            Color shadowColorAtHit = Color.BLACK.interpolate(colorAtHit, AMBIENT);
            Vector3 incidentDirection = ray.getDirection().normalize();
            Vector3 normal = hit.getShape().getNormal(hit);
            Ray shadowRay = new Ray(hitPoint.add(new Vector3(1e-6, 1e-6, 1e-6)), light.getAnchor().subtract(hitPoint));
            Vector3 reflectedDirection = incidentDirection.subtract(normal.scale(2 * incidentDirection.dot(normal)))
                    .normalize();
            Ray reflectedRay = new Ray(hitPoint, reflectedDirection);
            Color reflectedColor = calculateReflection(reflectedRay, light, objects, depth - 1);
            Color shadedColor = calculateShadedColor(light, normal, colorAtHit, hitPoint, shadowColorAtHit);

            if (!inShadow(shadowRay, light, objects)) {
                if (hitMaterial.reflectiveness > 0)
                    return colorAtHit.interpolate(reflectedColor, hitMaterial.reflectiveness);
                else
                    return getColor(ray.getOrigin()).interpolate(shadedColor, reflectiveness);
            }
            else {
                objects.add(shape);
                if (hitMaterial.reflectiveness > 0)
                    return shadowColorAtHit.interpolate(calculateReflection(reflectedRay, light, objects, depth - 1),
                            1 - reflectiveness);
                else
                    return shadowColorAtHit;

            }
        }
        else {
            return getColor(ray.getOrigin()).interpolate(new Color(25, 25, 25), reflectiveness);
        }
    }

    private Color calculateShadedColor(Light light, Vector3 normal, Color baseColor, Vector3 hitPoint,
            Color shadowColor) {
        Vector3 lightPosition = light.getAnchor();
        double brightnessFactor = normal.dot(lightPosition.subtract(hitPoint).normalize())
                / hitPoint.distance(lightPosition) * hitPoint.distance(lightPosition);
        Color shadedColor = Color.BLACK.interpolate(baseColor, AMBIENT);
        shadedColor = baseColor.multiply(light.getColor());
        shadedColor = shadedColor.brighten(brightnessFactor);
        if (shadedColor.toAWT().getRGB() <= shadowColor.toAWT().getRGB())
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
}
