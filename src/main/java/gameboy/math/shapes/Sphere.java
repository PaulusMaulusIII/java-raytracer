package gameboy.math.shapes;

import java.util.List;

import gameboy.math.Ray;
import gameboy.math.RayHit;
import gameboy.math.Vector3;
import gameboy.rendering.Light;
import gameboy.rendering.Material;
import gameboy.rendering.Shape3D;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class Sphere extends Shape3D {

    double radius;

    public Sphere(Vector3 anchor, double radius) {
        super(anchor);
        setMaterial(new SphereMaterial());
        this.radius = radius;
    }

    public Sphere(Vector3 anchor, Material material, double radius) {
        super(anchor, material);
        this.radius = radius;
    }

    @Override
    public List<Vector3> getPoints() {
        return new LinkedList<Vector3>(List.of(getAnchor()));
    }

    @Override
    public Vector3 getIntersectionPoint(Ray ray) {
        Vector3 oc = ray.getOrigin().subtract(getAnchor());
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2.0 * oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius * radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return null;
        }
        double t = (-b - Math.sqrt(discriminant)) / (2.0 * a);
        if (t >= 0)
            return ray.getOrigin().add(ray.getDirection().scale(t));
        return null;
    }

    public int getSide(Vector3 point) {
        int side = 0;
        side |= (point.x >= 0) ? 1 : 0;
        side |= (point.y >= 0) ? 2 : 0;
        side |= (point.z >= 0) ? 4 : 0;
        return side;
    }

    public class SphereMaterial extends Material {

        @Override
        public Color getColor(Vector3 point) {
            Color[] colors = { Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.BEIGE,
                    Color.PINK };
            return colors[getSide(point)];
        }

        @Override
        public Vector3 getNormal(Vector3 point) {
            return point.subtract(getAnchor()).normalize();
        }
    }
}
