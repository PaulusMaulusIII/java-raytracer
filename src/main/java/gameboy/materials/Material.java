package gameboy.materials;

import gameboy.lights.Light;
import gameboy.utilities.Shape3D;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.LinkedList;

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
        Vector3 normal = getNormal(rayHit);
        Vector3 hitPoint = rayHit.getHitPoint();
        Color baseColor = getColor(hitPoint);
        Color finalColor = Color.BLACK;

        LinkedList<Color> colors = new LinkedList<>();
        for (Light light : lights) {
            if (!inShadow(new Ray(hitPoint, light.getAnchor().subtract(hitPoint).normalize()), light, objects)) {
                Vector3 lightPosition = light.getAnchor();
                double brightnessFactor = normal.dot(lightPosition.subtract(hitPoint).normalize())
                        / hitPoint.distance(lightPosition) * hitPoint.distance(lightPosition);
                Color shadedColor = baseColor.deriveColor(0, 1, brightnessFactor, 1);
                colors.add(shadedColor.interpolate(Color.WHITE, ambient));
            }
            else {
                colors.add(Color.BLACK.interpolate(Color.WHITE, ambient));
            }
        }

        double red = 0;
        double green = 0;
        double blue = 0;
        double colorFactor = 1.0 / colors.size();
        for (Color color : colors) {
            red += (color.getRed() * colorFactor);
            green += (color.getGreen() * colorFactor);
            blue += (color.getBlue() * colorFactor);
        }

        finalColor = finalColor.interpolate(new Color(red, green, blue, 1), 1);

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

    public abstract Vector3 getNormal(RayHit hit);
}
