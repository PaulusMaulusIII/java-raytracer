package com.paulusmaulus.raytracer.core.swing_assets.additional;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.paulusmaulus.raytracer.core.swing_assets.SettingPanel;
import com.paulusmaulus.raytracer.geometries.Circle;
import com.paulusmaulus.raytracer.geometries.Cube;
import com.paulusmaulus.raytracer.geometries.Cylinder;
import com.paulusmaulus.raytracer.geometries.Plane;
import com.paulusmaulus.raytracer.geometries.Sphere;
import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.materials.BasicMaterial;
import com.paulusmaulus.raytracer.shaders.PhongShader;
import com.paulusmaulus.raytracer.utilities.Camera;
import com.paulusmaulus.raytracer.utilities.Color;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.Shape;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class CreateObjectDialog extends JDialog {

	public CreateObjectDialog(JFrame owner, JDialog settingsDialog, SettingPanel settingPanel) {
		super(owner);
		Camera camera = settingPanel.getScene().getCamera();
		Vector3 inFrontOfCam = camera.getAnchor()
				.add(new Vector3(0, 0, 1).rotate(camera.getPitch(), camera.getYaw(), camera.getTilt()).scale(10));
		Vector3 axis = new Vector3(0, 1, 0);
		BasicMaterial whitePhongShader = new BasicMaterial(new PhongShader(), Color.WHITE);
		JComboBox<? extends Object3D> objectSelector = new JComboBox<>(new Object3D[] {
				new Cylinder(inFrontOfCam, whitePhongShader, axis, 2, 5), new Cube(inFrontOfCam, whitePhongShader, 4),
				new Plane(inFrontOfCam, whitePhongShader, axis), new Sphere(inFrontOfCam, whitePhongShader, 2),
				new Circle(inFrontOfCam, whitePhongShader, axis, 3), new Light(inFrontOfCam)
		});
		setTitle("Select Shape To Add");
		setSize(400, 100);
		objectSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object3D objectToAdd = (Object3D) objectSelector.getSelectedItem();
				if (objectToAdd instanceof Shape) {
					Shape shapeToAdd = (Shape) objectToAdd;
					MenuItem menuItem = new MenuItem(shapeToAdd.toString());
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							settingPanel.getCurrentItemDisplay().setCurrentItem(shapeToAdd);
							if (!settingsDialog.isVisible())
								settingsDialog.setVisible(true);
						}
					});
					owner.getMenuBar().getMenu(2).add(menuItem);
					settingPanel.getScene().getShapes().add(shapeToAdd);
				}
				else if (objectToAdd instanceof Light) {
					Light lightToAdd = (Light) objectToAdd;
					MenuItem menuItem = new MenuItem(lightToAdd.toString());
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							settingPanel.getCurrentItemDisplay().setCurrentItem(lightToAdd);
							if (!settingsDialog.isVisible())
								settingsDialog.setVisible(true);
						}
					});
					owner.getMenuBar().getMenu(3).add(menuItem);
					settingPanel.getScene().getLights().add(lightToAdd);
				}

				dispose();
			}
		});
		objectSelector.setAlignmentX(CENTER_ALIGNMENT);
		objectSelector.setAlignmentY(CENTER_ALIGNMENT);

		add(objectSelector);

		setVisible(true);
	}
}
