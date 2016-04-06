package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.resourcetypes.*;

public class Bookmark {
	
	//static fields
	public static String SHARING_THIS_SYSTEM_ONLY = "Share only for this computer system";//only valid on this system
	public static String SHARING_THIS_USER_ONLY = "Share with all systems this user owns";//this user on all systems
	public static String SHARING_WITH_OTHERS = "Share with this group only";//Shares with the list of user or group id's specified.

    //Bookmark specific fields
    private UUID tagUUID;
    private String name;
    private String Description;

    private List<String> tags;//tags to associate with this bookmark

	//Sharing related fields
	private UUID ownerID;
    private List<UUID> shareWith;//a list of user or group UUID's to share this bookmark with.
    private String sharing;//type of sharing

    //Access related
    private Date createdDate;
    private Date lastAccessedDate;
    private int numberOfAccesses;//how many times has this bookmark been viewed.

    private List<BasicResource> resource;//represents the type, and values of the resource this bookmark points to.
	
	private Map<String, String> settingsMap;

    public Bookmark()
    {
        tagUUID = UUID.randomUUID();
    }

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

    public List<BasicResource> getResource()
    {
        return resource;
    }

    public void setResource(List<BasicResource> resource)
    {
        this.resource = resource;
    }

    public Map<String, String> getSettingsMap()
    {
        return settingsMap;
    }

    public void setSettingsMap(Map<String, String> settingsMap)
    {
        this.settingsMap = settingsMap;
    }
}
