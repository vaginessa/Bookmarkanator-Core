package com.bookmarkanator.bookmarks;

import java.util.*;

public abstract class AbstractBookmark implements Comparable<AbstractBookmark>
{
    protected static String secretKey;//Key needed to access message board

    protected String name;//The user visible name
    protected UUID id;
    protected Set<String> tags;
    protected Date creationDate;
    protected Date lastAccessedDate;
    protected Set<String> supportedActions;// The list of actions this bookmarks supports
    protected Map<String, Set<AbstractBookmark>> beforeListeners;
    protected Map<String, Set<AbstractBookmark>> afterListeners;

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
        supportedActions = new HashSet<>();
        beforeListeners = new HashMap<>();
        afterListeners = new HashMap<>();
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

    public boolean setSecretKey(String secretKey)
    {
        if (secretKey != null)
        {
            AbstractBookmark.secretKey = secretKey;
            return true;
        }
        return false;
    }

    protected void setSupportedActions(Set<String> newActions)
    {
        this.supportedActions = newActions;
    }

    public Set<String> getSupportedActions()
    {
        return supportedActions;
    }

    /**
     * Run specified action.
     *
     * @return The result string for the action
     * @throws Exception
     */
    public String runAction()
        throws Exception
    {
        return runAction("");
    }

    /**
     * Run whatever default action this bookmark has.
     *
     * @return The result string for the action
     * @throws Exception
     */
    public String runAction(String actionString)
        throws Exception
    {
        return runTheAction(actionString);
    }

    public void addBeforeListener(AbstractBookmark abstractBookmark, String actionString)
    {
        Set<AbstractBookmark> items = beforeListeners.get(actionString);

        if (items == null)
        {
            items = new HashSet<>();
            beforeListeners.put(actionString, items);
        }

        items.add(abstractBookmark);
    }

    public void removeBeforeListener(AbstractBookmark abstractBookmark, String actionString)
    {
        Set<AbstractBookmark> items = beforeListeners.get(actionString);

        if (items != null)
        {
            items.remove(abstractBookmark);
        }
    }

    public void addAfterListener(AbstractBookmark abstractBookmark, String actionString)
    {
        Set<AbstractBookmark> items = afterListeners.get(actionString);

        if (items == null)
        {
            items = new HashSet<>();
            afterListeners.put(actionString, items);
        }

        items.add(abstractBookmark);
    }

    public void removeAfterListener(AbstractBookmark abstractBookmark, String actionString)
    {
        Set<AbstractBookmark> items = afterListeners.get(actionString);

        if (items != null)
        {
            items.remove(abstractBookmark);
        }
    }

    private void notifyBeforeListeners(String actionString)
    {
        Set<AbstractBookmark> items = beforeListeners.get(actionString);

        if (items != null)
        {
            for (AbstractBookmark abstractBookmark : items)
            {
                abstractBookmark.notifyBeforeAction(this, actionString);
            }
        }
    }

    private void notifyAfterListeners(String actionString)
    {
        Set<AbstractBookmark> items = afterListeners.get(actionString);

        if (items != null)
        {
            for (AbstractBookmark abstractBookmark : items)
            {
                abstractBookmark.notifyAfterAction(this, actionString);
            }
        }
    }

    // ============================================================
    // Abstract Methods
    // ============================================================

    public abstract void notifyBeforeAction(AbstractBookmark source, String actionString);

    public abstract void notifyAfterAction(AbstractBookmark source, String actionString);

    /**
     * Run specified action
     *
     * @param action The action to run
     * @return The result string for the action
     * @throws Exception
     */
    protected abstract String runTheAction(String action)
        throws Exception;

    /**
     * This is the human readable name for this bookmark type.
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
    public abstract Set<String> getSearchWords()
        throws Exception;


    public abstract void systemInit();
    public abstract void systemShuttingDown();
}
