package gameboy.materials;

import gameboy.lights.Light;
import gameboy.utilities.Shape3D;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;
import javafx.scene.paint.Color;
import java.util.List;

public abstract class Material {
    protected Color color;
    protected double ambient = .5;
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
        Vector3 hitPoint = rayHit.getHitPoint();
        Color baseColor = getColor(hitPoint);
        Vector3 normal = getNormal(hitPoint);
        Color finalColor = Color.BLACK;

        for (Light light : lights) {
            if (!inShadow(new Ray(hitPoint, light.getAnchor().subtract(hitPoint).normalize()), light, objects)) {
                Vector3 lightPosition = light.getAnchor();
                double brightnessFactor = normal.dot(lightPosition.subtract(hitPoint).normalize())
                        / hitPoint.distance(lightPosition) * hitPoint.distance(lightPosition);
                Color shadedColor = baseColor.deriveColor(0, 1, brightnessFactor, 1);
                finalColor = finalColor.interpolate(shadedColor, 1);
            } else {
                finalColor = finalColor.interpolate(Color.BLACK, 1);
            }
        }

        return finalColor;
    }

    public boolean inShadow(Ray ray, Light light, List<Shape3D> objects) {
        boolean inShadow = false;
        for (Shape3D shape3d : objects) {
            Vector3 intersectionPoint = shape3d.getIntersectionPoint(ray);
            if (intersectionPoint != null && intersectionPoint.subtract(ray.getOrigin()).magnitude() < light.getAnchor()
                    .subtract(ray.getOrigin()).magnitude()) {
                inShadow = true;
            }
        }
        return inShadow;
    }

    public abstract Vector3 getNormal(Vector3 point);
}
