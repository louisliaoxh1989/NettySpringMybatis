package com.ycg.ycexpress.platform.http.appserver;

import java.util.List;
import java.util.Map;

public class ParamUtil {

	public static String getParameterByName(String paramName, Map<String, List<String>> params) {
		List<String> list = params.get(paramName);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static String getStringParamByName(String paramName, Map<String, List<String>> params, String defaultValue) {
		List<String> list = params.get(paramName);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return defaultValue;
		}
	}

	public static int getIntParamByName(String paramName, Map<String, List<String>> params, int defaultValue) {
		String value = getParameterByName(paramName, params);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static float getFloatParamByName(String paramName, Map<String, List<String>> params, float defaultValue) {
		String value = getParameterByName(paramName, params);
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static double getDoubleParamByName(String paramName, Map<String, List<String>> params, double defaultValue) {
		String value = getParameterByName(paramName, params);
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/** Prevent instantiation */
	private ParamUtil() {
	}

}
