package com.framework.utils;

import com.framework.driver.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigReader {

    private static final String configFilePath = "src/main/resources/config.properties";
    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    public static final Properties properties = new Properties();

    private ConfigReader() {
        // Private constructor to prevent instantiation
    }
    public static Properties readConfigFile() {

        try {
        FileInputStream fileInputStream=new FileInputStream(configFilePath);
        properties.load(fileInputStream);

    }catch (IOException e) {
            log.error("Error reading config file: " + e.getMessage());
        }
        return properties;
    }


    public static String getProperty(String key) {
        String propertyKey = readConfigFile().getProperty(key);

        if (propertyKey == null) {
            log.warn("Configuration key '{}' not found in config.properties. Returning null.", key);
        }
        return propertyKey;

    }

    public static String getProperty(String key, String defaultValue) {
        return readConfigFile().getProperty(key, defaultValue);
    }
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        log.info("Property '{}' set to '{}'", key, value);
    }
}
