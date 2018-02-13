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
        return getName().equals(name)
                || getValue().stream().filter(v -> v instanceof ConfigTreeNode)
                .anyMatch(e -> ((ConfigTreeNode) e).containsByName(name));
    }

    public boolean containsByAttributeName(String attrName) {

        return getAttributes().containsKey(attrName)
                ||
                getValue().stream().filter(v -> v instanceof ConfigTreeNode)
                        .anyMatch(e -> ((ConfigTreeNode) e).containsByAttributeName(attrName));

    }

    public boolean containsByAttributeValue(String attrValue) {

        return getAttributes().entrySet().stream().anyMatch(e -> e.getValue().equals(attrValue))
                ||
                getValue().stream()
                        .filter(v -> v instanceof ConfigTreeNode)
                        .anyMatch(e -> ((ConfigTreeNode) e).containsByAttributeValue(attrValue));
    }


    public Collection<ConfigTreeNode> getByName(String name) {
        Collection<ConfigTreeNode> ret = new HashSet<>();

        if (getName().equals(name)) {
            ret.add(this);
        }

        if (containsByName(name)) {
            getValue().stream().filter(v -> v instanceof ConfigTreeNode).forEach(e -> {
                    ret.addAll(((ConfigTreeNode) e).getByName(name));
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
