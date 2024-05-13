package gameboy.utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import gameboy.core.enums.Token;
import gameboy.lights.Light;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class Scene {
    private int currentCamera = 0;
    private List<Camera> cameras = new LinkedList<>();
    private List<Shape> children = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();
    private HashMap<Token, String> options;

    public Scene(Camera camera, List<Shape> children) {
        cameras.add(camera);
        this.children = children;
    }

    public Scene(List<Camera> cameras, List<Shape> children, List<Light> lights) {
        this.cameras = cameras;
        this.children = children;
        this.lights = lights;
    }

    public Scene(List<Camera> cameras, List<Shape> children, List<Light> lights, HashMap<Token, String> options) {
        this.cameras = cameras;
        this.children = children;
        this.lights = lights;
        this.options = options;
    }

    public Scene(Camera camera) {
        cameras.add(camera);
    }

    public List<Shape> getChildren() {
        return children;
    }

    public Camera getCurrentCamera() {
        return cameras.get(currentCamera);
    }

    public void nextCamera() {
        currentCamera++;
    }

    public RayHit castRay(Ray ray, List<Shape> objects) {
        RayHit hit = null;

        for (Shape shape3d : objects) {
            Vector3 hitPos = shape3d.getIntersectionPoint(ray);
            if (hitPos != null && (hit == null
                    || hit.getHitPoint().distance(ray.getOrigin()) > hitPos.distance(ray.getOrigin()))) {
                hit = new RayHit(ray, shape3d, hitPos);
                // System.out.println(hitPos.toString());
            }
        }
        return hit;
    }

    public List<Light> getLights() {
        return lights;
    }

    public HashMap<Token, String> getOptions() {
        return options;
    }
}