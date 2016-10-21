package com.zyf.utils.conf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangyufeng on 2016/10/21.
 */
public class ConfigTree {
    private ConfigTreeNode root;

    public ConfigTree() {
        root = new ConfigTreeNode();
    }

    public void setRoot(ConfigTreeNode root) {
        this.root = root;
    }

    public ConfigTreeNode getRoot() {
        return root;
    }

    public String toString() {
        return root.toString();
    }
}
