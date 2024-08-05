package com.paulusmaulus.raytracer.core.swing_assets.additional;

import java.awt.Label;

import com.paulusmaulus.raytracer.core.swing_assets.SettingPanel;
import com.paulusmaulus.raytracer.core.swing_assets.settings.CameraSettings;
import com.paulusmaulus.raytracer.core.swing_assets.settings.LightSettings;
import com.paulusmaulus.raytracer.core.swing_assets.settings.ShapeSettings;
import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.utilities.Camera;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.Shape;

public class CurrentItemDisplay extends VBox {

	private Object3D currentItem = null;
	private Label label = new Label();

	private SettingPanel settingPanel;

	public CurrentItemDisplay(SettingPanel parent) {
		super();
		this.settingPanel = parent;
		add(label);
		label.setText("No Object Selected!");
	}

	public void setCurrentItem(Object3D currentItem) {
		this.currentItem = currentItem;
		if (currentItem != null) {
			label.setText(currentItem.getName());
			try {
				remove(1);
			} catch (Exception e) {
			}
			if (currentItem instanceof Camera)
				add(new CameraSettings(settingPanel));
			else if (currentItem instanceof Shape)
				add(new ShapeSettings(settingPanel));
			else if (currentItem instanceof Light)
				add(new LightSettings(settingPanel));
		}
		else {
			label.setText("No Object Selected!");
			try {
				remove(1);
			} catch (Exception e) {
			}
		}
	}

	public Object3D getCurrentItem() {
		return currentItem;
	}

}
