package raytracer.core.swing_assets.inputs;

import javax.swing.JPanel;

import raytracer.core.interfaces.ObjectModification;
import raytracer.utilities.Color;
import raytracer.utilities.Object3D;

public class ColorInput extends JPanel {
	private InputField redField;
	private InputField greenField;
	private InputField blueField;

	public ColorInput(Color color) {
		redField = new InputField("Red", color.getRed());
		greenField = new InputField("Green", color.getGreen());
		blueField = new InputField("Blue", color.getBlue());

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
