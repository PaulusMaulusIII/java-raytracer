package com.paulusmaulus.editor.core.swing_assets.inputs;

import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.paulusmaulus.editor.core.interfaces.ObjectModification;
import com.paulusmaulus.editor.utilities.Object3D;

public class Slider extends JPanel {
	private Label label;
	private JSlider slider;
	private TextField value;

	public Slider(String labelText, int min, int max) {
		super();
		label = new Label(labelText);
		slider = new JSlider(min * 10, max * 10);
		slider.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {
				value.setText("" + (Double.parseDouble(value.getText()) + ((double) slider.getValue() / 10)));
				slider.setValue(0);
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
		slider.setMinorTickSpacing(10);
		slider.setMajorTickSpacing(50);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		value = new TextField("0", 1);

		add(label);
		add(slider);
		add(value);
	}

	public void setValue(double value) {
		this.value.setText("" + value);
	}

	public double getValue() {
		return Double.parseDouble(value.getText());
	}

	public void setAction(ObjectModification modification, Object3D object) {
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				modification.modify(object);
			}
		});

		value.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modification.modify(object);
			}
		});
	}
}
