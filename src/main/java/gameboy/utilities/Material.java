package gameboy.utilities;

import java.util.List;

import gameboy.lights.Light;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public abstract class Material {

    protected Color color;
    protected Shape shape;

    protected double reflectivity = 0;
    protected double emission = 0;
    protected double shininess = 5;

    public Material(Color color) {
        this.color = color;
    }

    public Color getColor(Vector3 point) {
        return this.color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }

    public double getEmission() {
        return emission;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public Shape getShape() {
        return shape;
    }

    public Vector3 getNormal(Vector3 hitPoint) {
        return shape.getNormal(hitPoint);
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    public double getShininess() {
        return shininess;
    }

    public Color shade(RayHit rayHit, List<Light> lights, List<Shape> objects) {
        Color baseColor = rayHit.getShape().getMaterial().getColor(rayHit.getHitPoint());

        Color ambientComponent = baseColor.multiply(GlobalSettings.AMBIENT_BRIGHTNESS);

        Color diffuseComponent = new Color(0, 0, 0);
        Color specularComponent = new Color(0, 0, 0);

        for (Light light : lights) {
            if (!isInShadow(rayHit, light, objects)) {
                diffuseComponent = diffuseComponent.add(baseColor.multiply(calculateDiffuseLighting(rayHit, light)));
                specularComponent = specularComponent
                        .add(light.getColor().multiply(calculateSpecularLighting(rayHit, light)));
            }
        }

        Color reflectionComponent = calculateReflection(rayHit, lights, objects);

        Color finalColor = ambientComponent.add(diffuseComponent).add(specularComponent).add(reflectionComponent);
        return finalColor;
    }

    private boolean isInShadow(RayHit rayHit, Light light, List<Shape> objects) {
        Vector3 hitPoint = rayHit.getHitPoint();
        Vector3 lightDirection = light.getAnchor().subtract(hitPoint).normalize();
        Ray shadowRay = new Ray(hitPoint.add(lightDirection.scale(1e-4)), lightDirection);

        RayHit hit = shadowRay.cast(objects);

        if (hit != null)
            return true;
        return false;
    }

    private double calculateDiffuseLighting(RayHit rayHit, Light light) {
        Vector3 normal = rayHit.getShape().getNormal(rayHit.getHitPoint()).normalize();
        Vector3 lightDirection = light.getAnchor().subtract(rayHit.getHitPoint()).normalize();
        return Math.max(0, normal.dot(lightDirection));
    }

    private double calculateSpecularLighting(RayHit rayHit, Light light) {
        Vector3 viewDirection = rayHit.getRay().getDirection().normalize();
        Vector3 lightDirection = light.getAnchor().subtract(rayHit.getHitPoint()).normalize();
        Vector3 normal = rayHit.getShape().getNormal(rayHit.getHitPoint()).normalize();
        Vector3 reflectDirection = lightDirection.subtract(normal.scale(2 * lightDirection.dot(normal))).normalize();

        double specularStrength = GlobalSettings.SPECULAR_STRENGTH;
        double shininess = getShininess();
        double specularFactor = Math.pow(Math.max(0, viewDirection.dot(reflectDirection)), shininess);

        return specularStrength * specularFactor;
    }

    private Color calculateReflection(RayHit rayHit, List<Light> lights, List<Shape> objects) {
        if (reflectivity <= 0) {
            return GlobalSettings.SKY_BOX_COLOR;
        }

        Vector3 hitPoint = rayHit.getHitPoint();
        Vector3 normal = getNormal(hitPoint);
        Vector3 incident = rayHit.getRay().getDirection();
        Vector3 reflectedDirection = incident.subtract(normal.scale(2 * incident.dot(normal))).normalize();
        Ray reflectedRay = new Ray(hitPoint.add(reflectedDirection.scale(1e-4)), reflectedDirection);

        RayHit reflectedHit = reflectedRay.cast(objects);
        if (reflectedHit != null) {
            Material reflectedMaterial = reflectedHit.getShape().getMaterial();
            Color reflectedColor = reflectedMaterial.shade(reflectedHit, lights, objects);
            return getColor(hitPoint).interpolate(reflectedColor, reflectivity);
        }

        return GlobalSettings.SKY_BOX_COLOR;
    }
}
