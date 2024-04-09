package gameboy.math.shapes;

import gameboy.math.Ray;
import gameboy.math.Vector3;
import gameboy.math.enums.Axis;
import gameboy.rendering.Material;
import gameboy.rendering.Shape3D;

import javafx.scene.paint.Color;
import java.util.List;
import java.util.LinkedList;

public class Plane extends Shape3D {

    Axis axis;

    public Plane(Vector3 anchor, Axis axis) {
        super(anchor);
        setMaterial(new PlaneMaterial());
        this.axis = axis;
    }

    public Plane(Vector3 anchor, Axis axis, Color color) {
        super(anchor);
        setMaterial(new PlaneMaterial(color));
        this.axis = axis;
    }

    @Override
    public List<Vector3> getPoints() {
        return new LinkedList<Vector3>(List.of(getAnchor()));
    }

    @Override
    public Vector3 getIntersectionPoint(Ray ray) {
        double coordinate = 1;
        double origin = 0;
        double direction = 0;
        switch (axis) {
        case X:
            origin = ray.getOrigin().x;
            direction = ray.getDirection().x;
            coordinate = getAnchor().x;
            break;
        case Y:
            origin = ray.getOrigin().y;
            direction = ray.getDirection().y;
            coordinate = getAnchor().y;
            break;
        case Z:
            origin = ray.getOrigin().z;
            direction = ray.getDirection().z;
            coordinate = getAnchor().z;
            break;

        default:
            break;
        }

        double t = (coordinate - origin) / direction;

        return (t >= 0 && t < Double.POSITIVE_INFINITY) ? ray.getOrigin().add(ray.getDirection().scale(t)) : null;
    }

    public Axis getAxis() {
        return axis;
    }

    public class PlaneMaterial extends Material {

        public PlaneMaterial() {
            super();
        }

        public PlaneMaterial(Color color) {
            super(color);
        }

        @Override
        public Vector3 getNormal(Vector3 point) {
            switch (axis) {
            case X:
                return new Vector3(1, 0, 0);
            case Y:
                return new Vector3(0, 1, 0);
            case Z:
                return new Vector3(0, 0, 1);

            default:
                return null;
            }
        }
    }
}
