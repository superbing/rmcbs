package com.bfd.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ConfigUtil {
    
    private static String configFilePath = "application-production.properties";
    
    private static Properties prop = new Properties();
    
    private static final ConfigUtil instance = new ConfigUtil();
    
    private ConfigUtil() {
        this.init(configFilePath);
    }
    
    public static ConfigUtil getInstance() {
        return instance;
    }
    
    private void init(String fileName) {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            prop.load(in);
        } catch (Exception e) {
            log.error(fileName + " 解读properties文件异常:", e);
        }
    }
    
    public String get(String key) {
        return prop.getProperty(key).toString().trim();
    }
    
    public String get(String key, String defaultValue) {
        try {
            return prop.getProperty(key).toString().trim();
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}
