package com.github.mtzw.filecleaner.unsplit;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

public class FileCleanerTest {

	@Test
	public void testMain() {
		try {
			FileCleaner.main(new String[] { "System.exit抑止" });
		} catch (Throwable t) {
			fail();
		}
		assertTrue(Files.exists(Paths
				.get("src/test/resources/out/testdata.csv")));
	}

}
