package com.paulusmaulus.raytracer.core.swing_assets.settings;

import com.paulusmaulus.raytracer.core.interfaces.ObjectModification;
import com.paulusmaulus.raytracer.core.swing_assets.SettingPanel;
import com.paulusmaulus.raytracer.core.swing_assets.inputs.ColorInput;
import com.paulusmaulus.raytracer.core.swing_assets.inputs.InputField;
import com.paulusmaulus.raytracer.lights.Light;
import com.paulusmaulus.raytracer.utilities.Object3D;
import com.paulusmaulus.raytracer.utilities.math.Vector3;

public class LightSettings extends Settings {

	private Light light;
	private ColorInput colorInput;
	private InputField intensity = new InputField("Intensity", 1.0);

	private ObjectModification setLightPosition = (Object3D currentItem) -> ((Light) currentItem)
			.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));;

	private SettingPanel settingPanel;

	public LightSettings(SettingPanel parent) {
		super();
		this.settingPanel = parent;
		this.light = (Light) settingPanel.getCurrentItemDisplay().getCurrentItem();
		colorInput = new ColorInput(light.getColor());

		xSlider.setValue(light.getAnchor().x);
		ySlider.setValue(light.getAnchor().y);
		zSlider.setValue(light.getAnchor().z);
		xSlider.setAction(setLightPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());
		ySlider.setAction(setLightPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());
		zSlider.setAction(setLightPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());

		colorInput
				.setAction(
						(Object3D object) -> ((Light) settingPanel.getCurrentItemDisplay().getCurrentItem())
								.setColor(colorInput.getColor()),
						settingPanel.getCurrentItemDisplay().getCurrentItem());
		intensity.setAction(
				(Object3D object) -> ((Light) settingPanel.getCurrentItemDisplay().getCurrentItem())
						.setIntensity(Double.parseDouble(intensity.getValue())),
				settingPanel.getCurrentItemDisplay().getCurrentItem());
		add(colorInput);
		add(intensity);
	}
}
