package com.bookmarking.bookmark;

import java.util.*;
import com.bookmarking.search.*;
import com.bookmarking.ui.*;
import org.apache.logging.log4j.*;

/**
 * The base class for all bookmarks.
 * <p>
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
    private UUID creationUser;

    private Date modificationDate;
    private UUID modificationUser;

    private Date lastAccessedDate;
    private UUID lastAccessedBy;

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
     * @return The UI element that this bookmark is able to communicate with.
     */
    public BookmarkUIInterface getUiInterface()
    {
        return uiInterface;
    }

    /**
     * The class that represents this bookmark to the user.
     *
     * @param uiInterface The UI element that this bookmark is able to communicate with.
     */
    public void setUiInterface(BookmarkUIInterface uiInterface)
    {
        this.uiInterface = uiInterface;
    }

    public UUID getCreationUser()
    {
        return creationUser;
    }

    public void setCreationUser(UUID creationUser)
    {
        this.creationUser = creationUser;
    }

    public UUID getModificationUser()
    {
        return modificationUser;
    }

    public void setModificationUser(UUID modificationUser)
    {
        this.modificationUser = modificationUser;
    }

    public UUID getLastAccessedBy()
    {
        return lastAccessedBy;
    }

    public void setLastAccessedBy(UUID lastAccessedBy)
    {
        this.lastAccessedBy = lastAccessedBy;
    }

    /**
     * The group location is used to organize bookmark by their own defined categories.
     *
     * @return Returns a list of categories. The path will be this list in order, and at the end it the bookmark group string (UI's could do this in any way of course).
     */
    public List<String> getGroupingLocation()
    {
        return new ArrayList<>();
    }

    /**
     * This is the method that will be called by any bookmarks that this bookmark is listening to prior to initiating an action.
     *
     * @param source       The source bookmark.
     * @param actionString The action string that is about to happen (it is assumed that the listener knows what this string
     *                     will mean)
     */
    public void notifyBeforeAction(AbstractBookmark source, String actionString)
    {
        // Do nothing by default
    }

    /**
     * This is the method that will be called by any bookmarks that this bookmark is listening to after initiating an action.
     *
     * @param source       The source bookmark.
     * @param actionString The action string that has just occurred (it is assumed that the listener knows what this string
     *                     will mean)
     */
    public void notifyAfterAction(AbstractBookmark source, String actionString)
    {
        // Do nothing by default
    }

    /**
     * Called prior to deleting this bookmark.
     */
    public void destroy()
        throws Exception
    {
        // Do nothing by default
    }

    /**
     * Called as the system is starting up so that if individual bookmark want to do some kind of configuration they can.
     */
    public void systemInit()
    {

    }

    /**
     * Called prior to shutting the system down, so that individual bookmark can perform any actions they deem necessary
     * prior to being shut down.
     */
    public void systemShuttingDown()
    {
        // Do nothing by default
    }

    // ============================================================
    // Abstract Methods
    // ============================================================

    /**
     * This method is used to determine if this bookmark knows what to do with the string data supplied. For example if there were a drag and drop
     * action on a bookmark, the data could be encoded in string form, and each bookmark could determine if they want to handle it or not.
     *
     * @param data The data the should be consumed.
     * @return True if this bookmark can handle this form of data, false otherwise.
     */
    public abstract boolean canConsume(String data);

    /**
     * Run specified action(s) with action string.
     *
     * @param actionStrings Zero or more specific configuration instructions or actions for running the action.
     * @return The result of the action. Returns an array of strings so that individual statuses can be given to the individual strings sent in.
     * @throws Exception
     */
    public abstract String[] runAction(String[]... actionStrings)
        throws Exception;

    /**
     * @return A list of possible actions this bookmark can do.
     */
    public abstract String[] getActions();

    /**
     * This is the human readable name for this type of bookmark.
     *
     * @return Human readable type name.
     */
    public abstract String getTypeName();

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

    @Override
    public int compareTo(AbstractBookmark o)
    {
        if (o==null || o.getId()==null)
        {// Null stuff sorts first
            return -1;
        }

        return o.getId().compareTo(this.getId());
    }

    public static Comparator<AbstractBookmark> getNameComparator()
    {
        return (o1, o2) ->
        {
            if (o1==o2)
            {
                return 0;
            }

            if (o1 == null)
            {
                return -1;
            }

            if (o2==null)
            {
                return 1;
            }

            String name1 = o1.getName();
            String name2 = o2.getName();

            if (name1!=null)
            {
                return name1.compareTo(name2);
            }
            else if (name2==null)
            {
                return 0;
            }
            else
            {
                return 1;
            }
        };
    }

    public static Comparator<AbstractBookmark> getDateComparator(SearchOptions.DateType dateType)
    {
        return (o1, o2) ->
        {
            Date o1Date;
            Date o2Date;

            if (dateType == SearchOptions.DateType.ACCESSED_DATE)
            {
                o1Date = o1.getLastAccessedDate();
                o2Date = o2.getLastAccessedDate();
            }
            else
            {
                o1Date = o1.getCreationDate();
                o2Date = o2.getCreationDate();
            }

            if ((o1 == null && o2 == null))
            {
                return 0;
            }

            if (o1 == null)
            {
                return -1;
            }

            if (o2 == null)
            {
                return 1;
            }

            if (o1Date == null && o2Date == null)
            {
                return 0;
            }

            if (o1Date == null)
            {
                return -1;
            }

            if (o2Date == null)
            {
                return 1;
            }

            return o1Date.compareTo(o2Date);
        };
    }
}


