package com.zyf.utils.conf;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;

/**
 * Created by zhangyufeng on 2016/10/21.
 */

public class ConfigTreeNode {

    private String name;
    private Map<String, String> attributes;
    private Set<Object> value;

    public ConfigTreeNode() {
        name = "";
        value = new HashSet<>();
        attributes = new ConcurrentHashMap<>();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Object> getValue() {
        return value;
    }

    public String getStringValue() {
        if (getValue().size() == 1) {
            for (Object o : getValue()) {
                return (o instanceof String) ? (String) o : null;
            }
        }

        return null;
    }

    public Integer getIntegerValue() {
        return (getStringValue() != null) ? Integer.parseInt(getStringValue()) : null;
    }

    public Long getLongValue() {
        return (getStringValue() != null) ? Long.parseLong(getStringValue()) : null;
    }

    public Short getShortValue() {
        return (getStringValue() != null) ? Short.parseShort(getStringValue()) : null;
    }

    public Boolean getBooleanValue() {
        return (getStringValue() != null) ? Boolean.parseBoolean(getStringValue()) : null;
    }

    public void addValue(Object currentValue) {
        if (currentValue instanceof Set) {
            @SuppressWarnings("unchecked")
            Set<ConfigTreeNode> newSet = (Set<ConfigTreeNode>) currentValue;
            for (Object o : newSet) {
                getValue().add(o);
            }
        } else {
            getValue().add(currentValue);
        }
    }

    public void addAttribute(String name, String value) {
        getAttributes().put(name, value);
    }

    public boolean containsByName(String name) {
        if (getName().equals(name)) {
            return true;
        }

        for (Object subValue : getValue()) {
            if (subValue instanceof ConfigTreeNode) {
                ConfigTreeNode subNode = (ConfigTreeNode) subValue;
                if (subNode.containsByName(name)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean containsByAttributeName(String name) {
        if (getAttributes().containsKey(name)) {
            return true;
        }

        for (Object subValue : getValue()) {
            if (subValue instanceof ConfigTreeNode) {
                ConfigTreeNode subNode = (ConfigTreeNode) subValue;
                if (subNode.containsByAttributeName(name)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean containsByAttributeValue(String value) {
        for (Map.Entry<String, String> entry : getAttributes().entrySet()) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }

        for (Object subValue : getValue()) {
            if (subValue instanceof ConfigTreeNode) {
                ConfigTreeNode subNode = (ConfigTreeNode) subValue;
                if (subNode.containsByAttributeValue(value)) {
                    return true;
                }
            }
        }

        return false;

    }

    public Set<ConfigTreeNode> getByName(String name) {
        Set<ConfigTreeNode> ret = new HashSet<>();

        if (getName().equals(name)) {
            ret.add(this);
        }

        if (containsByName(name)) {
            getValue().forEach(e -> {
                if (e instanceof ConfigTreeNode) {
                    ConfigTreeNode tmp = (ConfigTreeNode) e;
                    tmp.getByName(name).forEach(ret::add);
                }
            });
        }

        return ret;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        getAttributes().entrySet().forEach(e -> {
            sb.append(e.getKey() + "=" + e.getValue() + " ");
        });
        sb.append("}");

        return "{name=" + getName()
                + "; value=" + getValue()
                + "; attr=" + sb.toString() + "}";
    }

}
