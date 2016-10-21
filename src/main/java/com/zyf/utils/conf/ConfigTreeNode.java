package com.zyf.utils.conf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangyufeng on 2016/10/21.
 */

public class ConfigTreeNode {
    private String currentValue;
    private Map<String, ConfigTreeNode> subConfigTree;

    public ConfigTreeNode() {
        this("", new ConcurrentHashMap<>());
    }

    public ConfigTreeNode(String v, Map<String, ConfigTreeNode> subTree) {
        currentValue = v;
        subConfigTree = subTree;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public Map<String, ConfigTreeNode> getSubConfigTree() {
        return subConfigTree;
    }

    public void setSubConfigTree(Map<String, ConfigTreeNode> subConfigTree) {
        this.subConfigTree = subConfigTree;
    }

    public String toString() {
        return "{value=" + currentValue + "; subTree=" + subConfigTree +"}";
    }

}
