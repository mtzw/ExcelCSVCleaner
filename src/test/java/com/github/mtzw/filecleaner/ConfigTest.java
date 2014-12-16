package com.github.mtzw.filecleaner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

import org.junit.Test;

public class ConfigTest {
	private static Config config = new Config();

	@Test
	public void testGetInDir() {
		assertThat(config.getInDir(), is("src/test/resources/in"));
	}

	@Test
	public void testGetInCharset() {
		assertThat(config.getInCharset(), is(Charset.forName("MS932")));
	}

	@Test
	public void testGetOutDir() {
		assertThat(config.getOutDir(), is("src/test/resources/out"));
	}

	@Test
	public void testGetOutCharset() {
		assertThat(config.getOutCharset(), is(Charset.forName("UTF-8")));
	}

	@Test
	public void testGetInterval() {
		assertThat(config.getInterval(), is(50000L));
	}

}
