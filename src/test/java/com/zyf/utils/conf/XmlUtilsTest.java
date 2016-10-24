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
            System.out.println("root value=" + configTree.getRoot().getCurrentValue());
            configTree.getRoot().getSubConfigTree().entrySet().forEach(
                    e -> {
                        if(e.getValue().isLeaf()) {
                            System.out.println("leaf node: " + e.getKey() + "=" + e.getValue());
                        } else {
                            System.out.println("subTree: ");
                            e.getValue().getSubConfigTree().entrySet().forEach(
                                    en -> {
                                        if (en.getValue().isLeaf()) {
                                            System.out.println("leaf node: " + en.getKey() + "=" + en.getValue());
                                        } else {
                                            System.out.println("subTree: " + en.getValue());
                                        }
                                    }
                            );
                        }}
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
