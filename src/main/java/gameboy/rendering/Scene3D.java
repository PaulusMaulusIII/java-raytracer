package gameboy.rendering;

import java.util.LinkedList;
import java.util.List;

import gameboy.math.Ray;
import gameboy.math.RayHit;
import gameboy.math.Vector3;
import javafx.scene.canvas.Canvas;

public class Scene3D {
    private Canvas canvas;
    private int currentCamera = 0;
    private List<Camera3D> cameras = new LinkedList<>();
    private List<Shape3D> children = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();

    public Scene3D(Canvas canvas, Camera3D camera,
            List<Shape3D> children) {
        cameras.add(camera);
        this.children = children;
        this.canvas = canvas;
    }

    public Scene3D(Canvas canvas, Camera3D camera) {
        cameras.add(camera);
        this.canvas = canvas;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public List<Shape3D> getChildren() {
        return children;
    }

    public Camera3D getCurrentCamera() {
        return cameras.get(currentCamera);
    }

    public void nextCamera() {
        currentCamera++;
    }

    public RayHit castRay(Ray ray) {
        RayHit hit = null;

        for (Shape3D shape3d : getChildren()) {
            Vector3 hitPos = shape3d.getIntersectionPoint(ray);
            if (hitPos != null && (hit == null || hit.getHitPoint().distance(
                    ray.getOrigin()) > hitPos.distance(ray.getOrigin()))) {
                hit = new RayHit(ray, shape3d, hitPos);
                // System.out.println(hitPos.toString());
            }

        }

        return hit;
    }

    public List<Light> getLights() {
        return lights;
    }
}