package gameboy.core.enums;

import java.util.List;

public enum Materials {

	// Materials
	CHECKER("checker",
			List.of(Properties.COLOR, Properties.SEC_COLOR, Properties.GRIDSIZE, Properties.REFELECTIVITY,
					Properties.EMISSION)),
	MIRROR("mirror", List.of(Properties.COLOR, Properties.EMISSION)),
	CUBEMAT("cube", List.of(Properties.REFELECTIVITY, Properties.EMISSION)),
	SPHEREMAT("sphere", List.of(Properties.REFELECTIVITY, Properties.EMISSION)),
	BASIC("basic", List.of(Properties.COLOR, Properties.REFELECTIVITY, Properties.EMISSION)),
	WAVE("wave", List.of(Properties.COLOR, Properties.REFELECTIVITY, Properties.EMISSION));

	private final String propertystring;
	private final List<Properties> properties;

	private Materials(String propertystring, List<Properties> properties) {
		this.propertystring = propertystring;
		this.properties = properties;
	}

	public String getPropertystring() {
		return propertystring;
	}

	public List<Properties> getProperties() {
		return properties;
	}

	public static final List<Materials> MATERIALS = List.of(CHECKER, MIRROR, CUBEMAT, SPHEREMAT, BASIC, WAVE);
}
