package gameboy.utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import gameboy.core.enums.Properties;
import gameboy.lights.Light;

public class Scene {
    private int currentCamera = 0;
    private List<Camera> cameras = new LinkedList<>();
    private List<Shape> children = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();
    private HashMap<Properties, String> options;

    public Scene(Camera camera, List<Shape> children) {
        cameras.add(camera);
        this.children = children;
    }

    public Scene(List<Camera> cameras, List<Shape> children, List<Light> lights) {
        this.cameras = cameras;
        this.children = children;
        this.lights = lights;
    }

    public Scene(List<Camera> cameras, List<Shape> children, List<Light> lights, HashMap<Properties, String> options) {
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

    public List<Light> getLights() {
        return lights;
    }

    public HashMap<Properties, String> getOptions() {
        return options;
    }
}