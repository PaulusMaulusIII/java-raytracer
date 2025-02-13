package com.paulusmaulus.editor.core.swing_assets.settings;

import com.paulusmaulus.editor.core.swing_assets.additional.VBox;
import com.paulusmaulus.editor.core.swing_assets.inputs.Slider;
import com.paulusmaulus.editor.utilities.math.Vector3;

public abstract class Settings extends VBox {

	protected Slider xSlider = new Slider("xAxis", -10, 10);
	protected Slider ySlider = new Slider("yAxis", -10, 10);
	protected Slider zSlider = new Slider("zAxis", -10, 10);

	public Settings() {
		add(xSlider);
		add(ySlider);
		add(zSlider);
	}

	public void setPosValue(Vector3 pos) {
		xSlider.setValue(pos.x);
		ySlider.setValue(pos.y);
		zSlider.setValue(pos.z);
	}
}