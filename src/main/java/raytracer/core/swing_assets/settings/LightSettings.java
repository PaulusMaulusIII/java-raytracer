package raytracer.core.swing_assets.settings;

import raytracer.core.interfaces.ObjectModification;
import raytracer.core.swing_assets.SettingPanel;
import raytracer.core.swing_assets.inputs.ColorInput;
import raytracer.core.swing_assets.inputs.InputField;
import raytracer.lights.Light;
import raytracer.utilities.Object3D;
import raytracer.utilities.math.Vector3;

public class LightSettings extends Settings {

	private Light light;
	private ColorInput colorInput = new ColorInput(light.getColor());
	private InputField intensity = new InputField("Intensity", 1.0);

	private ObjectModification setLightPosition = (Object3D currentItem) -> ((Light) currentItem)
			.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));;

	private SettingPanel settingPanel;

	public LightSettings(SettingPanel parent) {
		super();
		this.settingPanel = parent;
		this.light = (Light) settingPanel.getCurrentItemDisplay().getCurrentItem();

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
