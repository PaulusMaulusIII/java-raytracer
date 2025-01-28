package com.paulusmaulus.editor.core.swing_assets.inputs;

import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.paulusmaulus.editor.core.interfaces.ObjectModification;
import com.paulusmaulus.editor.utilities.Object3D;

public class InputField<V> extends JPanel {
	private Label label = new Label();
	private TextField field = new TextField();
	private Label unit = new Label();

	public InputField(String labelText, V value) {
		super();
		label.setText(labelText);
		add(label);
		field.setText("" + value);
		add(field);
		add(unit);
	}

	public InputField(String labelText, V value, String unitText) {
		this(labelText, value);
		unit.setText(unitText);
	}

	public void setValue(V value) {
		field.setText("" + value);
	}

	public String getValue() {
		return field.getText();
	}

	public void setAction(ObjectModification modification, Object3D object) {
		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modification.modify(object);
			}
		});
	}
}
