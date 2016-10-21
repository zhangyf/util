package com.zyf.utils.conf;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by zhangyufeng on 2016/10/21.
 */
public class XmlUtilsTest {

    @Test
    public void testLoadXml() throws ClassNotFoundException, IOException {

        try {
            ConfigUtils.getConfig(getClass().getResource("/testXml.xml").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
