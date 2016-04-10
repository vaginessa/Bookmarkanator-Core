package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.interfaces.*;
import com.bookmarkanator.resourcetypes.*;

public class Bookmark implements XMLWritable{

    // ============================================================
    // Fields
    // ============================================================

    //static fields
	public static String SHARING_THIS_SYSTEM_ONLY = "Share only for this computer system";//only valid on this system
	public static String SHARING_THIS_USER_ONLY = "Share with all systems this user owns";//this user on all systems
	public static String SHARING_WITH_OTHERS = "Share with this group only";//Shares with the list of user or group id's specified.

    //Bookmark specific fields
    private UUID tagUUID;
    private String name;//name of the tag
    private String Description;//description of the tag

    private Map<String, String> tags;//tags to associate with this bookmark. Hashmap used to force unique tags, and
    //enable faster searching.

	//Sharing related fields
	private UUID ownerID;
    private List<UUID> shareWith;//a list of user or group UUID's to share this bookmark with.
    private String sharing;//type of sharing

    //Access related
    private Date createdDate;
    private Date lastAccessedDate;
    private int numberOfAccesses;//how many times has this bookmark been viewed.

    private BasicResource resource;//represents the type, and values of the resource this bookmark points to. Such as a web address type, with value 'www.yahoo.com'
    private Map<UUID, Integer> addedBookmarks;//A list of the bookmark UUIDs that have been added to this bookmark and their indexes within the list.


    // ============================================================
    // Constructors
    // ============================================================

    public Bookmark()
    {
        tagUUID = UUID.randomUUID();
        createdDate = new Date();
        lastAccessedDate = new Date();
        numberOfAccesses = 0;
        sharing = Bookmark.SHARING_THIS_USER_ONLY;

        tags = new HashMap<>();
        shareWith = new ArrayList<>();
        addedBookmarks = new HashMap<UUID, Integer>();
    }

    // ============================================================
    // Methods
    // ============================================================

    public UUID getTagUUID()
    {
        return tagUUID;
    }

    public void setTagUUID(UUID tagUUID)
    {
        this.tagUUID = tagUUID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setDescription(String description)
    {
        Description = description;
    }

    public Map<String, String> getTags()
    {
        return tags;
    }

    public void setTags(Map<String, String> tags)
    {
        this.tags = tags;
    }

    public void addTag(String tag)
    {
        tag = tag.replaceAll(","," ");
        tag = tag.trim();
        tag = tag.toUpperCase();
        tags.put(tag, tag);
    }

    public UUID getOwnerID()
    {
        return ownerID;
    }

    public void setOwnerID(UUID ownerID)
    {
        this.ownerID = ownerID;
    }

    public List<UUID> getShareWith()
    {
        return shareWith;
    }

    public void setShareWith(List<UUID> shareWith)
    {
        this.shareWith = shareWith;
    }

    public void addShare(UUID toShareWith)
    {
        shareWith.add(toShareWith);
    }

    public String getSharing()
    {
        return sharing;
    }

    public void setSharing(String sharing)
    {
        this.sharing = sharing;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Date getLastAccessedDate()
    {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(Date lastAccessedDate)
    {
        this.lastAccessedDate = lastAccessedDate;
    }

    public int getNumberOfAccesses()
    {
        return numberOfAccesses;
    }

    public void setNumberOfAccesses(int numberOfAccesses)
    {
        this.numberOfAccesses = numberOfAccesses;
    }

    public BasicResource getResource()
    {
        return resource;
    }

    public void setResource(BasicResource resource)
    {
        this.resource = resource;
    }

    public Map<UUID, Integer> getAddedBookmarks()
    {
        return addedBookmarks;
    }

    public void setAddedBookmarks(Map<UUID, Integer> addedBookmarks)
    {
        this.addedBookmarks = addedBookmarks;
    }

    public void addChildBookmark(UUID bookmarkID, int index)
    {
        addedBookmarks.put(bookmarkID, index);
    }

    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs);
        sb.append("<bookmark uuid=\"");
        sb.append(getTagUUID());
        sb.append("\" sharing=\"");
        sb.append(getSharing());
        sb.append("\">");
        sb.append("\n");
        sb.append(prependTabs);
        sb.append("\t<name>");
        sb.append(getName());
        sb.append("</name>");
        sb.append("\n");
        sb.append(prependTabs);
        sb.append("\t<description>");
        sb.append(getDescription());
        sb.append("</description>");
        sb.append("\n");
        sb.append(prependTabs);
        sb.append("\t<bookmark-owner>");
        sb.append(getOwnerID());
        sb.append("</bookmark-owner>");
        sb.append("\n");
        tagsToXML(sb, prependTabs);
        sb.append("\n");
        childBookmarksToXML(sb, prependTabs);
        sb.append(prependTabs);
        sb.append("\n");
        sb.append(prependTabs);
        sb.append("</bookmark>");
    }

    private void tagsToXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs);
        sb.append("\t<tags>");
        sb.append("\n");
        for (String s: getTags().keySet())
        {
            sb.append(prependTabs);
            sb.append("\t\t<tag>");
            sb.append(s);
            sb.append("</tag>");
            sb.append("\n");
        }
        sb.append(prependTabs);
        sb.append("\t</tags>");
    }

    private void childBookmarksToXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs);
        sb.append("\t<child-bookmarks>");
        sb.append("\n");
        for (UUID uuid: getAddedBookmarks().keySet())
        {
            sb.append(prependTabs);
            sb.append("\t\t<child-bookmark index=\"");
            sb.append(getAddedBookmarks().get(uuid));
            sb.append("\" uuid=\"");
            sb.append(uuid.toString());
            sb.append("\" />");
            sb.append("\n");
        }
        sb.append(prependTabs);
        sb.append("\t</child-bookmarks>");
    }

}
