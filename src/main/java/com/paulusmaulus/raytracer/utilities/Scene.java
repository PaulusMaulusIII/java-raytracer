package com.paulusmaulus.raytracer.utilities;

import java.util.LinkedList;
import java.util.List;

import com.paulusmaulus.raytracer.geometries.additional.Skybox;
import com.paulusmaulus.raytracer.lights.Light;

public class Scene {
    private Skybox skybox;
    private Camera camera;
    private List<Shape> shapes = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();

    public Scene(Camera camera) {
        this.camera = camera;
        this.skybox = new Skybox(Color.MAGENTA);
    }

    public Scene(Camera camera, List<Shape> children) {
        this(camera);
        this.shapes = new LinkedList<>(children);
    }

    public Scene(Camera camera, List<Shape> children, List<Light> lights) {
        this(camera, children);
        this.lights = new LinkedList<>(lights);
    }

    public Scene(Camera camera, List<Shape> children, List<Light> lights, Skybox skybox) {
        this(camera, children);
        this.lights = new LinkedList<>(lights);
        this.skybox = skybox;
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

    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public Skybox getSkybox() {
        return skybox;
    }

    public void addObject(Object3D object) {
        if (object instanceof Shape) {
            shapes.add((Shape) object);
        }
        else if (object instanceof Light) {
            lights.add((Light) object);
        }
    }
}