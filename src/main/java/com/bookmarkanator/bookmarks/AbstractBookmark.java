package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public abstract class AbstractBookmark {
    private String name;//The user visible name
    private UUID id;
    private String text;//contents of the bookmark
    private Set<String> tags;

    /**
     * The type name is the name that will be displayed to the user for this type of bookmark.
     * On the back end the fully qualified class name will be used for accessing storage and other
     * operations.
     * @return  Text of the name of the type of this bookmark.
     */
    public abstract String getTypeName();

    /**
     * The action that will happen when this bookmark is called.
     */
    public abstract void action(Context context) throws Exception;

    /**
     * Write this bookmark to the xml string that will represent it.
     * @return  The settings this bookmark wants to preserve written to a String in xml format.
     *
     * Note: This xml will be placed inside a larger xml structure, and only represents a single bookmark.
     */
    public abstract String toXML();

    /**
     * Populate the settings of this bookmark with a string containing xml for that purpose.
     * @param xml  The string to parse and use to configure the settings of this specific type of bookmark.
     */
    public abstract void fromXML(String xml);

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
