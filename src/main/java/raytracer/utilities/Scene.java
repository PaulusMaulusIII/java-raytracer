package raytracer.utilities;

import java.util.LinkedList;
import java.util.List;

import raytracer.lights.Light;

public class Scene {
    private Camera camera;
    private List<Shape> shapes = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();

    public Scene(Camera camera) {
        this.camera = camera;
    }

    public Scene(Camera camera, List<Shape> children) {
        this(camera);
        this.shapes = new LinkedList<>(children);
    }

    public Scene(Camera camera, List<Shape> children, List<Light> lights) {
        this(camera, children);
        this.lights = new LinkedList<>(lights);
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public Camera getCamera() {
        return camera;
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Object3D> getObjects() {
        List<Object3D> objects = new LinkedList<>();
        for (Light light : lights) {
            objects.add(light);
        }
        for (Shape shape : shapes) {
            objects.add(shape);
        }
        return objects;
    }
}