package com.zyf.utils.conf;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangyufeng on 2016/10/21.
 */

public class ConfigTreeNode {

    private String currentValue;
    private Map<String, ConfigTreeNode> subConfigTree;
    private Map<String, String> nodeAttributes;

    public ConfigTreeNode() {
        this("", new ConcurrentHashMap<>());
    }

    public ConfigTreeNode(String v, Map<String, ConfigTreeNode> subTree) {
        currentValue = v;
        subConfigTree = subTree;
        nodeAttributes = new ConcurrentHashMap<>();
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public String getStringValue() {
        return currentValue;
    }

    public Integer getIntegerValue() {
        return Integer.parseInt(currentValue);
    }

    public Long getLongValue() {
        return Long.parseLong(currentValue);
    }

    public Short getShortValue() {
        return Short.parseShort(currentValue);
    }

    public Boolean getBooleanValue() {
        return Boolean.parseBoolean(currentValue);
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

    public void addAttribute(String name, String value) {
        nodeAttributes.putIfAbsent(name, value);
    }

    public void updateAttribute(String name, String value) {
        nodeAttributes.put(name, value);
    }

    public String getAttribute(String name) {
        return nodeAttributes.get(name);
    }

    public Set<String> getAttributes() {
        return nodeAttributes.keySet();
    }

    public boolean isLeaf() {
        return !currentValue.equals("") && subConfigTree == null;
    }

    public boolean containsKey(String name) {
        if (subConfigTree == null) {
            return false;
        }

        if (subConfigTree.containsKey(name)) {
            return true;
        } else {
            for (Map.Entry<String, ConfigTreeNode> entry : subConfigTree.entrySet()) {
                if (entry.getValue().containsKey(name)) {
                    return true;
                }
            }
        }

        return false;
    }

    public ConfigTreeNode get(String name) {
        return traverse(name);
    }

    public Set<String> getSubKeys() {
        return subConfigTree.keySet();
    }

    ConfigTreeNode traverse(String name) {

        if (subConfigTree == null) {
            return null;
        }

        if (subConfigTree.containsKey(name)) {
            if (subConfigTree.containsKey(name)) {
                return subConfigTree.get(name);
            } else {
                for (Map.Entry<String, ConfigTreeNode> entry : subConfigTree.entrySet()) {
                    if (entry.getValue().containsKey(name)) {
                        return entry.getValue();
                    }
                }
            }
        }
        return null;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        nodeAttributes.entrySet().forEach(e -> {
            sb.append(e.getKey() + "=" + e.getValue() + " ");
        });
        sb.append("}");

        return "{value=" + currentValue
                + "; attr=" + sb.toString()
                +"  subTree=" + subConfigTree +"}";
    }

}
