package com.zyf.utils.conf;

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

    public boolean containsKey(String name) {
        return root.containsKey(name);
    }

    public ConfigTreeNode get(String name) {
        ConfigTreeNode value = null;
        if (root.containsKey(name)) {
            value = root.traverse(name);
        }

        return value;
    }

    public String toString() {
        return root.toString();
    }
}
