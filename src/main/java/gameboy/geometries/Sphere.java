package gameboy.geometries;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import gameboy.materials.Material;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class Sphere extends Shape {

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

    public class SphereMaterial extends Material {
        /**
         * Calculates, which side of the {@code Sphere} the {@code Vector3} point is on,
         * based on relation to {@code Vector3} anchor property. Implemented at
         * {@code Shape3D}
         * 
         * @param point {@code Vector3} point on {@code Sphere} dividided in 8
         * @return {@code int} side of {@code Sphere} from 0-8
         */
        public int getSide(Vector3 point) {
            int side = 0;
            side |= (point.x - getAnchor().x >= 0) ? 1 : 0;
            side |= (point.y - getAnchor().y >= 0) ? 2 : 0;
            side |= (point.z - getAnchor().z >= 0) ? 4 : 0;
            return side;
        }

        @Override
        public Color getColor(Vector3 point) {
            Color[] colors = {
                    Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.LIGHT_GRAY,
                    Color.PINK
            };
            return colors[getSide(point)];
        }

        @Override
        public Vector3 getNormal(RayHit rayHit) {
            return rayHit.getHitPoint().subtract(getAnchor()).normalize();
        }
    }
}
