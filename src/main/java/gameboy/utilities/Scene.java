package gameboy.utilities;

import java.util.LinkedList;
import java.util.List;

import gameboy.lights.Light;

public class Scene {
    private int currentCamera = 0;
    private List<Camera> cameras = new LinkedList<>();
    private List<Shape> shapes = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();

    public Scene(Camera camera, List<Shape> children) {
        cameras.add(camera);
        this.shapes = children;
    }

    public Scene(Camera camera, List<Shape> children, List<Light> lights) {
        cameras.add(camera);
        this.shapes = children;
        this.lights = lights;
    }

    public Scene(List<Camera> cameras, List<Shape> children, List<Light> lights) {
        this.cameras = cameras;
        this.shapes = children;
        this.lights = lights;
    }

    public Scene(Camera camera) {
        cameras.add(camera);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public Camera getCurrentCamera() {
        return cameras.get(currentCamera);
    }

    public void nextCamera() {
        currentCamera++;
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

}