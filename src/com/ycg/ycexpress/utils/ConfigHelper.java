package com.ycg.ycexpress.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public class ConfigHelper {

	private Properties properties;

	private FileInputStream inputStream;

	private String configFileName;

	public ConfigHelper(String configFilePath) {

		this.properties = new Properties();

		this.configFileName = System.getProperty("user.dir") + System.getProperty("file.separator") + configFilePath;
		try {
			this.inputStream = new FileInputStream(configFileName);
			this.properties.load(this.inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (this.inputStream != null) {
				try {
					this.inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getStrValue(String key) {
		if (this.properties.containsKey(key)) {
			return this.properties.getProperty(key, "").trim();
		}

		return "";
	}

	public String getStrValue(String key, String defaultValue) {
		String result = this.getStrValue(key);
		return result.length() > 0 ? result : defaultValue;
	}

	public int getIntValue(String key) {
		if (this.properties.containsKey(key)) {
			try {
				return Integer.parseInt(this.properties.getProperty(key, "0"));
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return -1;
	}

	public int getIntValue(String key, int defaultValue) {
		int result = this.getIntValue(key);
		return (result == -1) ? defaultValue : result;
	}
	
	public boolean getBooleanValue(String key){
		if (this.properties.containsKey(key)) {
			try {
				return Boolean.parseBoolean(this.properties.getProperty(key, "false"));
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return false;
	}
	
	public void writeValue(Map<String, String> params) {

		try {
			FileOutputStream fos = new FileOutputStream(this.configFileName);

			Set<Map.Entry<String, String>> paramSet = params.entrySet();
			Iterator<Entry<String, String>> ite = paramSet.iterator();
			while (ite.hasNext()) {
				Entry<String, String> param = ite.next();
				this.properties.setProperty(param.getKey(), param.getValue());
			}
			this.properties.store(fos, "set");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
