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

    private List<String> tags;//tags to associate with this bookmark

	//Sharing related fields
	private UUID ownerID;
    private List<UUID> shareWith;//a list of user or group UUID's to share this bookmark with.
    private String sharing;//type of sharing

    //Access related
    private Date createdDate;
    private Date lastAccessedDate;
    private int numberOfAccesses;//how many times has this bookmark been viewed.

    private BasicResource resource;//represents the type, and values of the resource this bookmark points to. Such as a web address type, with value 'www.yahoo.com'
    private List<BasicResource> addedBookmarks;//A list of the bookmarks that have been added to this bookmark (they will be converted into strings).


    // ============================================================
    // Constructors
    // ============================================================

    public Bookmark()
    {
        tagUUID = UUID.randomUUID();
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

    public List<String> getTags()
    {
        return tags;
    }

    public void setTags(List<String> tags)
    {
        this.tags = tags;
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

    public List<BasicResource> getAddedBookmarks()
    {
        return addedBookmarks;
    }

    public void setAddedBookmarks(List<BasicResource> addedBookmarks)
    {
        this.addedBookmarks = addedBookmarks;
    }

    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs+"<bookmark uuid=\"");
        sb.append(getTagUUID());
        sb.append(" sharing=\"");
        sb.append(getSharing());
        sb.append("\">");
        sb.append("\n");
        sb.append(prependTabs+"\t<name>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+getName());
        sb.append("\n");
        sb.append(prependTabs+"\t</name>");
        sb.append("\n");
        sb.append(prependTabs+"\t<description>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+getDescription());
        sb.append("\n");
        sb.append(prependTabs+"\t</description>");
        sb.append("\n");
        sb.append(prependTabs+"\t<tag-owner>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+getOwnerID());
        sb.append("\n");
        sb.append(prependTabs+"\t</tag-owner>");
        sb.append("\n");
        //TODO add write tags here.
        //TODO add write bookmark resources here.
        sb.append(prependTabs+"</bookmark>");
    }
}
