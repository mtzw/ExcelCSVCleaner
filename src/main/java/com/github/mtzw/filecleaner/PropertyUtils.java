package com.github.mtzw.filecleaner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {

	public static Properties getProperties(String propertiesPath)
			throws Exception {
		Properties properties = new Properties();
		try {
			properties.load(getResource(propertiesPath));
		} catch (Exception e) {
			throw e;
		}
		return properties;
	}

	private static InputStream getResource(String propertiesPath)
			throws Exception {
		return new FileInputStream(new File(propertiesPath));
	}

}
