package com.zyf.utils.conf;

import java.util.HashSet;
import java.util.Set;

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

    public boolean containsByName(String name) {
        return getRoot().containsByName(name);
    }

    public boolean containsByAttributeName(String attriName) {
        return getRoot().containsByAttributeName(attriName);
    }

    public boolean containsByAttributeValue(String value) {
        return getRoot().containsByAttributeValue(value);
    }

    public Set<ConfigTreeNode> getByName(String name) {
        Set<ConfigTreeNode> result = new HashSet<>();

        if (getRoot().getName().equals("name")) {
            result.add(getRoot());
        }

        getRoot().getValue().forEach(e -> {
            if (e instanceof ConfigTreeNode) {
                ConfigTreeNode tmp = (ConfigTreeNode) e;
                tmp.getByName(name).forEach(result::add);
            }
        });

        return result;
    }

    public String toString() {
        return root.toString();
    }
}
