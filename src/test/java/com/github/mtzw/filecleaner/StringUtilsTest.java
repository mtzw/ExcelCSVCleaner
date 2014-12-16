package com.github.mtzw.filecleaner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static com.github.mtzw.filecleaner.StringUtils.*;

import java.util.Arrays;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testTrimWhiteSpace() {
		assertThat(trimWhiteSpace("あい　う えお 　 "), is("あい　う えお"));
		assertThat(trimWhiteSpace("　abccccc"), is("abccccc"));
		assertThat(trimWhiteSpace(" 　** **　**＊＊　　  "), is("** **　**＊＊"));
	}

	@Test
	public void testListToCsv() {
		String actual = listToCsv(Arrays.asList("あああ", "いいい", "ううう"));
		assertThat(actual, is("\"あああ\",\"いいい\",\"ううう\""));

		actual = listToCsv(Arrays.asList("a\nbc", "xyz"));
		assertThat(actual, is("\"a\nbc\",\"xyz\""));
	}

}
