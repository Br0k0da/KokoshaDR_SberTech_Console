package org.example.utils;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader()
                .getResourceAsStream("config.properties");) {
            PROPERTIES.load(inputStream);
        } catch (IOException ex) {
            System.out.println("При получении информации о БД произошла ошибка.");
            System.exit(0);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
