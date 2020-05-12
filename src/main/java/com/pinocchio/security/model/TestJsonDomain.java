package com.pinocchio.security.model;

import java.util.List;
import java.util.Map;

public class TestJsonDomain {
    private String name;
    private List<Map<String, String>> links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getLinks() {
        return links;
    }

    public void setLinks(List<Map<String, String>> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "TestJsonDomain{" +
                "name='" + name + '\'' +
                ", links=" + links +
                '}';
    }
}
