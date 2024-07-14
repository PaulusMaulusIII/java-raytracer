package gameboy.core.enums;

import java.util.LinkedList;
import java.util.List;

public enum Assets {
	// Assets
	CAMERA("Camera"), LIGHT("Light");

	private final String Propertiestring;

	private Assets(String Propertiestring) {
		this.Propertiestring = Propertiestring;
	}

	public String getPropertiestring() {
		return Propertiestring;
	}

	public static final List<Assets> ASSETS = new LinkedList<>(List.of(CAMERA, LIGHT));
}
