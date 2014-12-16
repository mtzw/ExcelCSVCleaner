package com.github.mtzw.filecleaner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ApplicationPropertiesTest {

	@Test
	public void testGetConfigPropertiesPath() {
		ApplicationProperties properties = new ApplicationProperties();
		assertThat(properties.getConfigPropertiesPath(),
				is("src/test/resources/config.properties"));
	}

}
