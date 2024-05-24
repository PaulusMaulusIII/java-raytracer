package gameboy.core.enums;

import java.util.LinkedList;
import java.util.List;

public enum Materials {

	// Materials
	CHECKER("checker"), MIRROR("mirror"), CUBEMAT("cube"), SPHEREMAT("sphere"), BASIC("basic"), WAVE("wave");

	private final String Propertiestring;

	private Materials(String Propertiestring) {
		this.Propertiestring = Propertiestring;
	}

	public String getPropertiestring() {
		return Propertiestring;
	}

	public static final List<Materials> MATERIALS = new LinkedList<>(
			List.of(CHECKER, MIRROR, CUBEMAT, SPHEREMAT, BASIC, WAVE));
}
