package com.bookmarkanator;

import java.util.*;

public class Bookmark {
    private String type;
    private String name;
    private UUID id;
    private String theText;
    private Set<String> tags;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTheText() {
        return theText;
    }

    public void setTheText(String theText) {
        this.theText = theText;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
