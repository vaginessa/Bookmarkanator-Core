package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public abstract class AbstractBookmark implements Comparable<AbstractBookmark>
{


    //TODO Add a field here to have settings passing? Or do it some other way?

    protected static String secretKey;//Key needed to access message board
    public enum ActionTypes
    {
        delete("delete"),
        create("create"),
        save("save"),
        update("update");

        private String action;

        ActionTypes(String action)
        {
            this.action = action;
        }

        public String value()
        {
            return this.action;
        }
    }

    private String name;//The user visible name
    private UUID id;
    private String text;//contents of the bookmark - depends on which kind of bookmark it is.
    private Set<String> tags;
    private Date creationDate;
    private Date lastAccessedDate;
    private Set<BKDependency> dependencies;//Things this bookmark wishes to be notified of.

    // ============================================================
    // Constructors
    // ============================================================

    public AbstractBookmark()
    {
        tags = new HashSet<>();
        id = UUID.randomUUID();
        name = "";
        text = "";
        creationDate = new Date();
        lastAccessedDate = new Date();
    }
//
//    /**
//     * Bookmarks are required to have a constructor that accepts a ContextInterface because it allows extending classes the opportunity to implement
//     * custom behaviour upon being initialized.
//     *
//     * @param contextInterface The Bookmark context object for the custom bookmark to use.
//     */
//    public AbstractBookmark(ContextInterface contextInterface)
//    {//Do nothing for the abstract bookmark.
//        this();
//        this.contextInterface = contextInterface;
//    }

    // ============================================================
    // Methods
    // ============================================================
//
//    /**
//     * @return The context interface that this bookmark belongs to (The context where this bookmark was added).
//     */
//    public ContextInterface getContextInterface()
//    {
//        return contextInterface;
//    }
//
//    /**
//     * @param contextInterface The context interface that this bookmark belongs to (The context where this bookmark was added).
//     */
//    public void setContextInterface(ContextInterface contextInterface)
//    {
//        this.contextInterface = contextInterface;
//    }

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

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

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
        if (secretKey!=null)
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
     * Write this bookmark to the com.bookmarkanator.xml string that will represent it.
     *
     * @return The settings this bookmark wants to preserve written to a String in com.bookmarkanator.xml format.
     * <p/>
     * Note: The string returned will be placed inside a larger com.bookmarkanator.xml structure, and only represents a single bookmark. A string in any form can be written here.
     */
    public abstract String getSettings();

    /**
     * Populate the settings of this bookmark with a string containing com.bookmarkanator.xml for that purpose. This bookmark must understand how to interpret the string sent in.
     * The form of this string depends on how this bookmark type writes it's settings out.
     *
     * @param settings The string to parse and use to configure the settings of this specific type of bookmark.
     */
    public abstract void setSettings(String settings);

}
