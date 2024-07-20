package com.paulusmaulus.raytracer.core.swing_assets;

import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.paulusmaulus.raytracer.core.swing_assets.additional.CurrentItemDisplay;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.Scene;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class SettingPanel extends JPanel {
	List<Vector3> polygonVertices = Arrays.asList(new Vector3(-2, 0, 0), new Vector3(2, 0, 0), new Vector3(0, 0, 4),
			new Vector3(0, 4, 2));
	Scene scene;
	JFrame main;
	SettingPanel settingPanel = this;
	CurrentItemDisplay currentItemDisplay;

	public SettingPanel(JFrame jFrame, Scene scene) {
		setSize(500, 800);
		main = jFrame;
		this.scene = scene;
		currentItemDisplay = new CurrentItemDisplay(this);
		add(new JScrollPane(currentItemDisplay));
	}

	public Scene getScene() {
		return scene;
	}

	public void setCurrentItem(Object3D object) {
		if (currentItemDisplay != null)
			currentItemDisplay.setCurrentItem(object);
	}

	public CurrentItemDisplay getCurrentItemDisplay() {
		return currentItemDisplay;
	}
}