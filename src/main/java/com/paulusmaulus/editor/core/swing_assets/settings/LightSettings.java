package com.paulusmaulus.editor.core.swing_assets.settings;

import com.paulusmaulus.editor.core.interfaces.ObjectModification;
import com.paulusmaulus.editor.core.swing_assets.SettingPanel;
import com.paulusmaulus.editor.core.swing_assets.inputs.ColorInput;
import com.paulusmaulus.editor.core.swing_assets.inputs.InputField;
import com.paulusmaulus.editor.lights.Light;
import com.paulusmaulus.editor.utilities.Object3D;
import com.paulusmaulus.editor.utilities.math.Vector3;

public class LightSettings extends Settings {

	private Light light;
	private ColorInput colorInput;
	private InputField<Double> intensity;

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

		intensity = new InputField<>("Intensity", light.getIntensity());
		intensity.setAction(
				(Object3D object) -> ((Light) settingPanel.getCurrentItemDisplay().getCurrentItem())
						.setIntensity(Double.parseDouble(intensity.getValue())),
				settingPanel.getCurrentItemDisplay().getCurrentItem());

		add(colorInput);
		add(intensity);
	}
}
