package cn.zyf.utils.conf;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        return Objects.requireNonNull(getValue().stream().findFirst().orElse(null)).toString();
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

        if (getAttributes().entrySet().stream().anyMatch(e -> e.getValue().equals(value))) {
            return true;
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

    public Collection<ConfigTreeNode> getByName(String name) {
        Collection<ConfigTreeNode> ret = new HashSet<>();

        if (getName().equals(name)) {
            ret.add(this);
        }

        if (containsByName(name)) {
            getValue().forEach(e -> {
                if (e instanceof ConfigTreeNode) {
                    ConfigTreeNode tmp = (ConfigTreeNode) e;
                    ret.addAll(tmp.getByName(name));
                }
            });
        }

        return ret;
    }

    public String toString() {

        return "{name=" + getName()
                + "; value=" + getValue()
                + "; attr=" + getAttributes().entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(" ","{","}")) + "}";

    }
}
