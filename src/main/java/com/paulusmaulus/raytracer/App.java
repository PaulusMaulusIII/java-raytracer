package com.paulusmaulus.raytracer;

import java.awt.Component;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.paulusmaulus.raytracer.core.swing_assets.SettingPanel;
import com.paulusmaulus.raytracer.core.swing_assets.Viewport;
import com.paulusmaulus.raytracer.core.swing_assets.additional.CreateObjectDialog;
import com.paulusmaulus.raytracer.geometries.Sphere;
import com.paulusmaulus.raytracer.geometries.additional.Arrow;
import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.materials.BasicMaterial;
import com.paulusmaulus.raytracer.shaders.PhongShader;
import com.paulusmaulus.raytracer.utilities.Camera;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.Scene;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class App {
    Scene scene = new Scene(new Camera(new Vector3(0, 0, -4), Math.toRadians(40)),
            List.of(new Sphere(new Vector3(0, 0, 0), new BasicMaterial(new PhongShader(), Color.BLACK), 2)),
            List.of(new Light(new Vector3(7.5, 5, 20), new Color(255, 255, 72), 50),
                    new Light(new Vector3(-7.5, 5, 20), new Color(255, 0, 72), 50)));

    public App() {

        JFrame main = new JFrame();
        main.setSize(1280, 745);
        main.setTitle("Java Ray-Tracer");
        main.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JDialog settingsDialog = new JDialog(main, "Settings");
        SettingPanel settings = new SettingPanel(main, scene);
        MenuBar menuBar = createMenuBar(main, settings, settingsDialog, scene);
        Viewport viewport = new Viewport(main, settings, settingsDialog, main.getWidth(), main.getHeight());

        settingsDialog.add(settings);
        settingsDialog.setSize(settings.getSize());
        settingsDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        viewport.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        main.setVisible(true);
        main.setMenuBar(menuBar);
        main.add(viewport);
        viewport.run();
    }

    private MenuBar createMenuBar(JFrame main, SettingPanel settings, JDialog settingsDialog, Scene scene) {
        MenuBar menuBar = new MenuBar();

        Menu label = new Menu("Java Ray-Tracer");
        Menu camera = new Menu("Camera");
        Menu shapes = new Menu("Shapes");
        Menu lights = new Menu("Lights");

        MenuItem openSettings = new MenuItem("Open Settings");
        openSettings.addActionListener((ActionEvent e) -> {
            settingsDialog.setVisible(true);
        });

        MenuItem addObject = new MenuItem("Add Object");
        addObject.addActionListener((ActionEvent e) -> {
            new CreateObjectDialog(main, settingsDialog, settings).setVisible(true);
        });

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
                    if (((MenuItem) menu.getItem(i)).getLabel().equals(currentItem.toString())) {
                        menu.remove(i);
                        break;
                    }
                }
            }
        });

        label.add(openSettings);
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
                MenuItem menuItem = new MenuItem(shape.toString());
                menuItem.addActionListener((ActionEvent e) -> {
                    settings.getCurrentItemDisplay().setCurrentItem(shape);
                    if (!settingsDialog.isVisible())
                        settingsDialog.setVisible(true);
                });
                shapes.add(menuItem);
            }
        }

        for (Light light : scene.getLights()) {
            MenuItem menuItem = new MenuItem(light.toString());
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

    public static void main(String[] args) {
        new App();
    }

}