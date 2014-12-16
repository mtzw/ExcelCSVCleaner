package com.github.mtzw.filecleaner;

import java.util.ResourceBundle;

public class ApplicationProperties {

	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("application");

	public ApplicationProperties() {
		this.configPropertiesPath = BUNDLE.getString("config.path");
	}

	private String configPropertiesPath;

	public String getConfigPropertiesPath() {
		return configPropertiesPath;
	}

}
