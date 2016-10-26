package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public abstract class AbstractBookmark <T>{
    private String name;
    private UUID id;
    private String text;//contents of the bookmark
    private Set<String> tags;

    /**
     * The type of this bookmark. Note: this must be unique, and link to a class name if dynamically loading class.
     * @return  A unique bookmark name.
     */
    public abstract String getTypeName();

    /**
     * The action that will happen when this bookmark is called.
     */
    public abstract T action(Context context);

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
