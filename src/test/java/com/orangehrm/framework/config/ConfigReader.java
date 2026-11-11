package com.orangehrm.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Loads test environment settings from src/test/resources/config.properties. */
public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties was not found on the test classpath.");
            }
            PROPERTIES.load(input);
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private ConfigReader() { }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        String value = systemValue != null ? systemValue : PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing configuration property: " + key);
        }
        return value.trim();
    }

    public static int getInt(String key) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Configuration property '" + key + "' must be an integer.", exception);
        }
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static String baseUrl() {
        String url = get("base.url");
        return url.endsWith("/") ? url : url + "/";
    }
}
