package gameboy.rendering;

import gameboy.math.RayHit;
import gameboy.math.Vector3;
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

    public Color shade(RayHit rayHit, List<Light> lights) {
        Vector3 hitPoint = rayHit.getHitPoint();
        Color baseColor = getColor(hitPoint);
        Vector3 normal = getNormal(hitPoint);
        Color finalColor = Color.BLACK;

        for (Light light : lights) {
            Vector3 lightPosition = light.getAnchor();
            double brightnessFactor = normal.dot(lightPosition.subtract(hitPoint).normalize())
                    / hitPoint.distance(lightPosition) * 16;
            Color shadedColor = baseColor.deriveColor(0, 1, brightnessFactor, 1);
            finalColor = finalColor.interpolate(shadedColor, 1);
        }

        return finalColor;
    }

    public abstract Vector3 getNormal(Vector3 point);
}
