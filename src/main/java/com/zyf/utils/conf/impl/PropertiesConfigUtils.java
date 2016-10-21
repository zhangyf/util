package com.zyf.utils.conf.impl;

import com.zyf.utils.conf.ConfigTree;
import com.zyf.utils.conf.ConfigUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhangyufeng on 2016/9/28.
 *
 */

public class PropertiesConfigUtils extends ConfigUtils {

    @Override
    protected ConfigTree loadConfigFile(String path) throws IOException {
//        Map<String, String> params = new HashMap<>();
//        Properties properties = new Properties();
//
//        try (InputStream in = new FileInputStream(path)){
//            properties.load(in);
//
//            properties.stringPropertyNames().forEach(p -> params.put(p, properties.getProperty(p)));
//        }
//
//        return params;
        return null;
    }
}
