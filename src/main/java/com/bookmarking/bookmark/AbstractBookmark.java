package com.bookmarking.bookmark;

import java.util.*;
import org.apache.logging.log4j.*;

/**
 * The base class for all bookmarks.
 *
 * What is a bookmark? A bookmark is a group of code designed to work with a specific chunk of data. For example a web bookmark can operate on
 * hypertext links.
 */
public abstract class AbstractBookmark implements Comparable<AbstractBookmark>
{
    private static final Logger logger = LogManager.getLogger(AbstractBookmark.class.getCanonicalName());

    // The UI class that represents this bookmark
    private BookmarkUIInterface uiInterface;

    //The user visible name
    protected String name;

    // The bookmark Id
    protected UUID id;

    // Tags associated with this bookmark
    protected Set<String> tags;

    // Dates
    private Date creationDate;
    private Date modificationDate;
    private Date lastAccessedDate;

    // The list of actions this bookmark supports or understands
    private Set<String> supportedActions;
    private Map<String, Set<AbstractBookmark>> beforeListeners;
    private Map<String, Set<AbstractBookmark>> afterListeners;

    // ============================================================
    // Constructors
    // ============================================================

    public AbstractBookmark()
    {
        tags = new HashSet<>();
        id = UUID.randomUUID();
        name = "";
        creationDate = new Date();
        lastAccessedDate = new Date();
        supportedActions = new HashSet<>();
        beforeListeners = new HashMap<>();
        afterListeners = new HashMap<>();
    }

    // ============================================================
    // Methods
    // ============================================================

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

    public Date getModificationDate()
    {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate)
    {
        this.modificationDate = modificationDate;
    }

    public Date getLastAccessedDate()
    {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(Date lastAccessedDate)
    {
        this.lastAccessedDate = lastAccessedDate;
    }

    /**
     * This is a secret key that is intended to be set by the message board
     * as the key that this bookmark can use to post to the message board.
     *
     * This must remain an abstract method because the key needs to be specific to each class, and that has to
     * be implemented in the extending class.
     *
     * @param messageBoardKey The secret key to use when modifying message board messages.
     * @return True if it was set.
     */
    public abstract boolean setMessageBoardKey(String messageBoardKey);

    public Set<String> getSupportedActions()
    {
        return supportedActions;
    }

    /**
     * Run whatever default action this bookmark has.
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
     * Run specified action.
     *
     * @return The result string for the action
     * @throws Exception
     */
    public abstract String runAction(String actionString)
        throws Exception;

    /**
     * Before listeners will be called prior to this bookmark calling it's runActionCode
     *
     * @param abstractBookmark The bookmark that is listening
     * @param actionString     The action string it is waiting for.
     */
    public void addBeforeListener(AbstractBookmark abstractBookmark, String actionString)
    {
        if (abstractBookmark == this)
        {
            logger.warn("Bookmark tried to add itself as a listener to itself");
            return;
        }

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

    /**
     * After listeners will be called after this bookmark calls it's runActionCode
     *
     * @param abstractBookmark The bookmark that is listening
     * @param actionString     The action string it is waiting for.
     */
    public void addAfterListener(AbstractBookmark abstractBookmark, String actionString)
    {
        if (abstractBookmark == this)
        {
            logger.warn("Bookmark tried to add itself as a listener to itself");
            return;
        }

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

        // Remove this bookmark from the action list just in case.
        items.remove(this);

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

        // Remove this bookmark from the action list just in case.
        items.remove(this);

        if (items != null)
        {
            for (AbstractBookmark abstractBookmark : items)
            {
                abstractBookmark.notifyAfterAction(this, actionString);
            }
        }
    }

    /**
     * @return  The UI element that this bookmark is able to communicate with.
     */
    public BookmarkUIInterface getUiInterface()
    {
        return uiInterface;
    }

    /**
     * The class that represents this bookmark to the user.
     * @param uiInterface  The UI element that this bookmark is able to communicate with.
     */
    public void setUiInterface(BookmarkUIInterface uiInterface)
    {
        this.uiInterface = uiInterface;
    }

    // ============================================================
    // Abstract Methods
    // ============================================================

    public abstract void notifyBeforeAction(AbstractBookmark source, String actionString);

    public abstract void notifyAfterAction(AbstractBookmark source, String actionString);

    /**
     * This is the human readable name for this bookmark group.
     *
     * @return Text of the name of the group of this bookmark.
     */
    public abstract String getTypeName();

    /**
     * The group location is used to organize bookmark by their own defined categories.
     *
     * @return Returns a list of categories. The path will be this list in order, and at the end it the bookmark group string (UI's could do this in any way of course).
     */
    public abstract List<String> getTypeLocation();

    /**
     * Called prior to deleting this bookmark.
     */
    public abstract void destroy()
        throws Exception;

    /**
     * Gets a new instance of this type of bookmark.
     *
     * @return A new instance of this type of bookmark.
     */
    public abstract AbstractBookmark getNew();

    /**
     * The content is a string representation of configuration data that a particular bookmark requires.
     * This field is intended for more structured data. It can be in any form, xml, csv, json, etc... The parser doesn't validate anything
     * in between the content tags. The bookmark is expected to know how to deal with the structure of the string parsed in.
     *
     * @return A string of configuration data for this bookmark.
     */
    public abstract String getContent();

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

    /**
     * Called as the system is starting up so that if individual bookmark want to do some kind of configuration they can.
     */
    public abstract void systemInit();

    /**
     * Called prior to shutting the system down, so that individual bookmark can perform any actions they deem necessary
     * prior to being shut down.
     */
    public abstract void systemShuttingDown();

    /**
     * This method is used to determine if this bookmark knows what to do with the string data supplied. For example if there were a drag and drop
     * action on a bookmark, the data could be encoded in string form, and each bookmark could determine if they want to handle it or not.
     * @param data  The data the should be consumed.
     * @return  True if this bookmark can handle this form of data, false otherwise.
     */
    public abstract boolean canConsume(String data);

}
