package com.github.mtzw.filecleaner;

import java.util.List;

public final class StringUtils {

	private static final String COMMA = ",";

	private static final String QUOTE = "\"";

	public static String trimWhiteSpace(String str) {
		if (str == null) {
			return str;
		}
		return org.apache.commons.lang3.StringUtils.strip(str.trim(), " \t　");
	}

	public static String listToCsv(List<String> strs) {
		return doListToCsv(strs);
	}

	private static <T> String doListToCsv(List<T> list) {
		if (list == null) {
			return "";
		}

		StringBuilder csv = new StringBuilder();
		boolean first = true;
		for (T element : list) {
			if (first) {
				first = false;
			} else {
				csv.append(COMMA);
			}
			csv.append(QUOTE).append(element == null ? "" : element)
					.append(QUOTE);
		}
		return csv.toString();
	}
}
