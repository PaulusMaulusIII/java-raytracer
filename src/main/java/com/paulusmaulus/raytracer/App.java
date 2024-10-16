package com.paulusmaulus.raytracer;

import java.awt.Component;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.paulusmaulus.raytracer.core.swing_assets.SettingPanel;
import com.paulusmaulus.raytracer.core.swing_assets.Viewport;
import com.paulusmaulus.raytracer.core.swing_assets.additional.CreateObjectDialog;
import com.paulusmaulus.raytracer.geometries.Sphere;
import com.paulusmaulus.raytracer.geometries.additional.Arrow;
import com.paulusmaulus.raytracer.geometries.additional.Skybox;
import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.materials.BasicMaterial;
import com.paulusmaulus.raytracer.shaders.PhongShader;
import com.paulusmaulus.raytracer.utilities.Camera;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.Scene;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class App implements Runnable {
    Scene scene = new Scene(new Camera(new Vector3(0, 0, -4), Math.toRadians(40)));

    JFrame main;
    JDialog settingsDialog;
    SettingPanel settings;
    MenuBar menuBar;
    Viewport viewport;

    public App() {
        main = new JFrame();
        main.setSize(1280, 720);
        main.setTitle("Java Ray-Tracer");
        main.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        scene.addObject(
                new Sphere(new Vector3(0, 0, 3), new BasicMaterial(new PhongShader(), new Color(243, 196, 207)), 2));

        scene.addObject(new Light(new Vector3(50, 1, 0), Color.WHITE, 100));

        scene.setSkybox(new Skybox(new File("src\\main\\resources\\textures\\Sky.jpg")));

        settingsDialog = new JDialog(main, "Settings");
        settings = new SettingPanel(main, scene);
        menuBar = createMenuBar(main, settings, settingsDialog, scene);
        viewport = new Viewport(main, settings, settingsDialog, main.getWidth(), main.getHeight());

        settingsDialog.add(settings);
        settingsDialog.setSize(settings.getSize());
        settingsDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        viewport.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        main.setVisible(true);
        main.setMenuBar(menuBar);
        main.add(viewport);
    }

    private MenuBar createMenuBar(JFrame main, SettingPanel settings, JDialog settingsDialog, Scene scene) {
        MenuBar menuBar = new MenuBar();

        Menu label = new Menu("Java Ray-Tracer");
        Menu camera = new Menu("Camera");
        Menu shapes = new Menu("Shapes");
        Menu lights = new Menu("Lights");

        MenuItem reloadMenu = new MenuItem("Reload MenuBar");
        reloadMenu.addActionListener(
                (ActionEvent e) -> main.setMenuBar(createMenuBar(main, settings, settingsDialog, scene)));

        MenuItem openSettings = new MenuItem("Open Settings");
        openSettings.addActionListener((ActionEvent e) -> settingsDialog.setVisible(true));

        MenuItem addObject = new MenuItem("Add Object");
        addObject.addActionListener(
                (ActionEvent e) -> new CreateObjectDialog(main, settingsDialog, settings).setVisible(true));

        MenuItem removeObject = new MenuItem("Remove Object");
        removeObject.addActionListener((ActionEvent e) -> {
            Object3D currentItem = settings.getCurrentItemDisplay().getCurrentItem();
            if (currentItem != null) {
                Menu menu = null;
                if (currentItem instanceof Shape) {
                    menu = shapes;
                    settings.getScene().getShapes().remove(currentItem);
                }
                else if (currentItem instanceof Light) {
                    menu = lights;
                    settings.getScene().getLights().remove(currentItem);
                }
                for (int i = 0; i < menu.getItemCount(); i++) {
                    if (((MenuItem) menu.getItem(i)).getLabel().equals(currentItem.getName())) {
                        menu.remove(i);
                        break;
                    }
                }
            }
        });

        label.add(openSettings);
        label.add(reloadMenu);
        label.add(addObject);
        label.add(removeObject);

        MenuItem cameraItem = new MenuItem("Edit camera");
        cameraItem.addActionListener((ActionEvent e) -> {
            settings.getCurrentItemDisplay().setCurrentItem(scene.getCamera());
            if (!settingsDialog.isVisible())
                settingsDialog.setVisible(true);
        });
        camera.add(cameraItem);

        for (Shape shape : scene.getShapes()) {
            if (!(shape instanceof Arrow)) {
                MenuItem menuItem = new MenuItem(shape.getName());
                menuItem.addActionListener((ActionEvent e) -> {
                    settings.getCurrentItemDisplay().setCurrentItem(shape);
                    if (!settingsDialog.isVisible())
                        settingsDialog.setVisible(true);
                });
                shapes.add(menuItem);
            }
        }

        for (Light light : scene.getLights()) {
            MenuItem menuItem = new MenuItem(light.getName());
            menuItem.addActionListener((ActionEvent e) -> {
                settings.getCurrentItemDisplay().setCurrentItem(light);
                if (!settingsDialog.isVisible())
                    settingsDialog.setVisible(true);
            });
            lights.add(menuItem);
        }

        menuBar.add(label);
        menuBar.add(camera);
        menuBar.add(shapes);
        menuBar.add(lights);

        return menuBar;
    }

    @Override
    public void run() {
        viewport.run();
    }

    public static void main(String[] args) {
        new App().run();
    }

}