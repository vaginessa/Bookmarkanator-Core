package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public abstract class AbstractBookmark implements Comparable<AbstractBookmark>
{
    protected static String secretKey;//Key needed to access message board

    protected String name;//The user visible name
    protected UUID id;
    //    protected String text;//contents of the bookmark. Intended to be human readable text stored in the xml.
    protected Set<String> tags;
    protected Date creationDate;
    protected Date lastAccessedDate;
    protected Set<BKDependency> dependencies;//Things this bookmark wishes to be notified of.

    // ============================================================
    // Constructors
    // ============================================================

    public AbstractBookmark()
    {
        tags = new HashSet<>();
        id = UUID.randomUUID();
        name = "";
        //        text = "";
        creationDate = new Date();
        lastAccessedDate = new Date();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    //    public String getText()
    //    {
    //        return text;
    //    }
    //
    //    public void setText(String data) throws Exception {
    //        this.text = data;
    //    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
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

    public Set<BKDependency> getDependencies()
    {
        return dependencies;
    }

    public void setDependencies(Set<BKDependency> dependencies)
    {
        this.dependencies = dependencies;
    }

    public boolean setSecretKey(String secretKey)
    {
        if (secretKey != null)
        {
            AbstractBookmark.secretKey = secretKey;
            return true;
        }
        return false;
    }

    // ============================================================
    // Abstract Methods
    // ============================================================

    /**
     * The type name is the name that will be displayed to the user for this type of bookmark.
     *
     * @return Text of the name of the type of this bookmark.
     */
    public abstract String getTypeName();

    /**
     * The type location is used to organize bookmarks by their own defined categories.
     *
     * @return Returns a list of categories. The path will be this list in order, and at the end it the bookmark type string (UI's could do this in any way of course).
     */
    public abstract List<String> getTypeLocation();

    /**
     * The runAction that will happen when this bookmark is called.
     */
    public abstract void runAction()
        throws Exception;

    /**
     * Called prior to deleting this bookmark.
     */
    public abstract void destroy()
        throws Exception;

    /**
     * Gets a new instance of this type of bookmark;
     *
     * @return A new instance of this class of bookmark.
     */
    public abstract AbstractBookmark getNew();

    /**
     * The context is a string representation of configuration data that a particular bookmark requires.
     * This field is intended for more structured data. It can be in any form, xml, csv, json, etc... The parser doesn't validate anything
     * in between the content tags. The bookmark is expected to know how to deal with the structure of the string parsed in.
     *
     * @return A string of configuration data for this bookmark.
     */
    public abstract String getContent()
        throws Exception;

    /**
     * This method will be called mostly by the xml parser. It reads everything between the two context tags without consideration
     * for what format it is in, and sets it here as a single string. When it is set, is when the bookmark it self should do whatever
     * parsing is necessary to read it in.
     *
     * @param content The config string (usually) stored in the xml.
     */
    public abstract void setContent(String content)
        throws Exception;

    /**
     * Extract words to use for searching from the settings for this bookmark.
     *
     * @return A set of search words that can be used to locate this bookmark.
     */
    public abstract Set<String> getSearchWords();

}
