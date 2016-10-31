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
            ConfigTree configTree = ConfigUtils.getConfig(getClass().getResource("/testXml.xml").toString());

            //System.out.println(configTree);

            System.out.println(configTree.getByName("cluster"));
            //System.out.println(configTree.getByName("subName1"));
            //System.out.println(configTree.getByName("subSubSubName1"));
            //System.out.println(configTree.containsByName("subSubName1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
