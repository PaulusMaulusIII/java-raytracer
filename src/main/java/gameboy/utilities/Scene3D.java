package gameboy.utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import gameboy.core.enums.Token;
import gameboy.lights.Light;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class Scene3D {
    private int currentCamera = 0;
    private List<Camera3D> cameras = new LinkedList<>();
    private List<Shape3D> children = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();
    private HashMap<Token, String> options;

    public Scene3D(Camera3D camera, List<Shape3D> children) {
        cameras.add(camera);
        this.children = children;
    }

    public Scene3D(List<Camera3D> cameras, List<Shape3D> children, List<Light> lights) {
        this.cameras = cameras;
        this.children = children;
        this.lights = lights;
    }

    public Scene3D(List<Camera3D> cameras, List<Shape3D> children, List<Light> lights, HashMap<Token, String> options) {
        this.cameras = cameras;
        this.children = children;
        this.lights = lights;
        this.options = options;
    }

    public Scene3D(Camera3D camera) {
        cameras.add(camera);
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

    public RayHit castRay(Ray ray, List<Shape3D> objects) {
        RayHit hit = null;

        for (Shape3D shape3d : objects) {
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