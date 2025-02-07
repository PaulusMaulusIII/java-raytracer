package com.paulusmaulus.editor.core.swing_assets.inputs;

import javax.swing.JPanel;

import com.paulusmaulus.editor.core.interfaces.ObjectModification;
import com.paulusmaulus.editor.utilities.Color;
import com.paulusmaulus.editor.utilities.Object3D;

public class ColorInput extends JPanel {
	private InputField<Integer> redField;
	private InputField<Integer> greenField;
	private InputField<Integer> blueField;

	public ColorInput(Color color) {
		redField = new InputField<>("Red", color.getRed());
		greenField = new InputField<>("Green", color.getGreen());
		blueField = new InputField<>("Blue", color.getBlue());

		redField.setBackground(Color.RED.toAWT());
		greenField.setBackground(Color.GREEN.toAWT());
		blueField.setBackground(Color.BLUE.toAWT());

		add(redField);
		add(greenField);
		add(blueField);
	}

	public void setAction(ObjectModification objectModification, Object3D object) {
		redField.setAction(objectModification, object);
		greenField.setAction(objectModification, object);
		blueField.setAction(objectModification, object);
	}

	public Color getColor() {
		return new Color(Integer.parseInt(redField.getValue()), Integer.parseInt(greenField.getValue()),
				Integer.parseInt(blueField.getValue()));
	}
}
