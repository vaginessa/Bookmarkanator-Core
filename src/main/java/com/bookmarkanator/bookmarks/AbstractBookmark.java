package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public abstract class AbstractBookmark implements Comparable<AbstractBookmark>{
    private String name;//The user visible name
    private UUID id;
    private String text;//contents of the bookmark
    private Set<String> tags;
    private Date creationDate;
    private Date lastAccessedDate;

    private AbstractBookmark()
    {
        tags = new HashSet<>();
        id = UUID.randomUUID();
        name = "";
        text = "";
        creationDate = new Date();
        lastAccessedDate = new Date();
    }

    /**
     * Bookmarks are required to have a constructor that accepts a ContextINterface because it allows extending classes the opportunity to implement
     * custom behaviour upon being initialized.
     * @param contextInterface  The Bookmark context object for the custom bookmark to use.
     */
    public AbstractBookmark(ContextInterface contextInterface)
    {//Do nothing for the abstract bookmark.
        this();
    }

    /**
     * The type name is the name that will be displayed to the user for this type of bookmark.
     * On the back end the fully qualified class name will be used for accessing storage and other
     * operations.
     * @return  Text of the name of the type of this bookmark.
     */
    public abstract String getTypeName();

    /**
     * The type location is used to organize bookmarks by their own defined categories.
     * @return  Returns a list of categories. The path will be this list in order, and at the end it the bookmark type string (UI's could do thin in any way of course).
     */
    public abstract List<String> getTypeLocation();

    /**
     * The action that will happen when this bookmark is called.
     */
    public abstract void action(ContextInterface context) throws Exception;

    /**
     * Gets a new instance of this type of bookmark;
     * @return  A new instance of this class of bookmark.
     */
    public abstract AbstractBookmark getNew();

    /**
     * Write this bookmark to the com.bookmarkanator.xml string that will represent it.
     * @return  The settings this bookmark wants to preserve written to a String in com.bookmarkanator.xml format.
     *
     * Note: This com.bookmarkanator.xml will be placed inside a larger com.bookmarkanator.xml structure, and only represents a single bookmark.
     */
    public abstract String getSettings();

    /**
     * Populate the settings of this bookmark with a string containing com.bookmarkanator.xml for that purpose.
     * @param xml  The string to parse and use to configure the settings of this specific type of bookmark.
     */
    public abstract void setSettings(String xml);

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

    public void addTag(String tag)
    {
        this.tags.add(tag);
    }

    public void removeTag(String tag)
    {
        this.tags.remove(tag);
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public Date getLastAccessedDate()
    {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(Date lastAccessedDate)
    {
        this.lastAccessedDate = lastAccessedDate;
    }


}
