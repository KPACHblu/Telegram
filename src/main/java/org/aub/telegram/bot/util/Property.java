package org.aub.telegram.bot.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class Property {
    private Properties properties;
    public Property(String propertyFile) {
        properties = new Properties();
        InputStream inputStream = Property.class.getClassLoader().getResourceAsStream(propertyFile);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Can't load properties");
            }
        } else {
            throw new RuntimeException("property file '" + propertyFile + "' not found in the classpath");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getPropertyParam(String key, Object... params) {
        return MessageFormat.format(properties.getProperty(key), params);

    }

}
