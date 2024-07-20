package raytracer.core.swing_assets.settings;

import raytracer.core.interfaces.ObjectModification;
import raytracer.core.swing_assets.SettingPanel;
import raytracer.core.swing_assets.inputs.InputField;
import raytracer.core.swing_assets.inputs.Slider;
import raytracer.utilities.Camera;
import raytracer.utilities.Object3D;
import raytracer.utilities.math.Vector3;

public class CameraSettings extends Settings {
	private Camera cam;
	private Slider pitchSlider = new Slider("Pitch", -10, 10);
	private Slider yawSlider = new Slider("Yaw", -10, 10);
	private InputField fovField;

	private ObjectModification setCameraPosition = (Object3D currentItem) -> ((Camera) currentItem)
			.setAnchor(new Vector3(xSlider.getValue(), ySlider.getValue(), zSlider.getValue()));

	private SettingPanel settingPanel;

	public CameraSettings(SettingPanel parent) {
		super();
		this.settingPanel = parent;
		cam = (Camera) settingPanel.getCurrentItemDisplay().getCurrentItem();
		fovField = new InputField("FOV", Math.toDegrees(cam.getFOV()), "°");

		xSlider.setValue(cam.getAnchor().x);
		ySlider.setValue(cam.getAnchor().y);
		zSlider.setValue(cam.getAnchor().z);
		xSlider.setAction(setCameraPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());
		ySlider.setAction(setCameraPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());
		zSlider.setAction(setCameraPosition, settingPanel.getCurrentItemDisplay().getCurrentItem());

		pitchSlider.setAction((Object3D object) -> ((Camera) object).setPitch(Math.toRadians(-pitchSlider.getValue())),
				settingPanel.getCurrentItemDisplay().getCurrentItem());
		add(pitchSlider);

		yawSlider.setAction((Object3D object) -> ((Camera) object).setYaw(Math.toRadians(yawSlider.getValue())),
				settingPanel.getCurrentItemDisplay().getCurrentItem());
		add(yawSlider);

		fovField.setAction(
				(Object3D object) -> ((Camera) object).setFOV(Math.toRadians(Double.parseDouble(fovField.getValue()))),
				settingPanel.getCurrentItemDisplay().getCurrentItem());
		add(fovField);
	}
}