package com.zyf.utils.conf;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhangyufeng on 2016/9/28.
 */
public class PropertiesUtils {

    public static Map<String, String> loadPropertiesFile(InputStream in) throws IOException {
        Map<String, String> params = new HashMap<>();
        Properties properties = new Properties();

            properties.load(in);

            properties.stringPropertyNames().forEach(p -> params.put(p, properties.getProperty(p)));

        return params;
    }

    public static Map<String, String> loadPropertiesFile(String path) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        Properties properties = new Properties();
        
        try (InputStream in = new FileInputStream(path)){
            properties.load(in);

            properties.stringPropertyNames().forEach(p -> params.put(p, properties.getProperty(p)));
        }

        return params;
    }
}
