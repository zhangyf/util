package com.zyf.utils.conf;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyufeng on 2016/9/29.
 */
public class PropertiesUtilsTest {

    @Test
    public void testLoadProperties() throws ClassNotFoundException, IOException {
        Map<String, String> params = new HashMap<>();

        params = PropertiesUtils.loadPropertiesFile("");
        params.entrySet().forEach(p -> System.out.println(p.getKey() + "=" + p.getValue()));

        params = PropertiesUtils.loadPropertiesFile(getClass().getResourceAsStream("/testProperties.properties"));
        params.entrySet().forEach(p -> System.out.println(p.getKey() + "=" + p.getValue()));
    }
}
