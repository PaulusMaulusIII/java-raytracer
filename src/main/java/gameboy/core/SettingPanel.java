package gameboy.core;

import java.awt.PopupMenu;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gameboy.geometries.Sphere;
import gameboy.lights.Light;
import gameboy.materials.BasicMaterial;
import gameboy.utilities.Camera;
import gameboy.utilities.Color;
import gameboy.utilities.Scene;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Vector3;

public class SettingPanel extends JPanel {

	Scene scene = new Scene(new Camera(new Vector3(0, 2, -10), Math.toRadians(40)),
			new LinkedList<Shape>(List.of(new Sphere(new Vector3(0, 0, 0), new BasicMaterial(Color.PINK), 2))),
			new LinkedList<Light>(List.of(new Light(new Vector3(0, 10, -5), Color.YELLOW))));
	java.awt.List list = new java.awt.List();

	public SettingPanel(JFrame jFrame) {
		add(list);
		for (Shape shape : scene.getChildren()) {
			list.add(shape.toString());
		}
	}

	public Scene getScene() {
		return scene;
	}

}
