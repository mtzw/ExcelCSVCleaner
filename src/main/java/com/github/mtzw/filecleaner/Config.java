package com.github.mtzw.filecleaner;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	static final Logger LOGGER = LoggerFactory.getLogger("app");

	private String inDir;
	private Charset inCharset;
	private String outDir;
	private Charset outCharset;
	private Long interval;

	public Config() {
		String propertiesPath = new ApplicationProperties()
				.getConfigPropertiesPath();
		Properties properties;
		try {
			properties = PropertyUtils.getProperties(propertiesPath);
			this.inDir = properties.getProperty("in.dir");
			this.inCharset = Charset.forName(properties.getProperty("in.charset"));
			this.outDir = properties.getProperty("out.dir");
			this.outCharset = Charset.forName(properties.getProperty("out.charset"));
			this.interval = Long.valueOf(properties.getProperty("interval"));
		} catch (Exception e) {
			if (e instanceof FileNotFoundException) {
				LOGGER.error(String.format("設定ファイル[%s]が存在しません", propertiesPath));
			} else {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public String getInDir() {
		return inDir;
	}

	public Charset getInCharset() {
		return inCharset;
	}

	public String getOutDir() {
		return outDir;
	}

	public Charset getOutCharset() {
		return outCharset;
	}

	public Long getInterval() {
		return interval;
	}

}
