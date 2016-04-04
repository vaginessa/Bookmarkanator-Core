package bookmarks;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Bookmark {
	
	//static fields
	public static String SHARING_THIS_SYSTEM_ONLY = "Share only for this computer system";//only valid on this system
	public static String SHARING_THIS_USER_ONLY = "Share with all systems this user owns";//this user on all systems
	public static String SHARING_THIS_GROUP_ONLY = "Share with this group only";
	public static String SHARING_WITH_EVERYONE = "Share with everyone";
		
	//Sharing related fields
	private UUID ownerID;
	
	//Bookmark specific fields
	private UUID tagUUID;
	private String name;
	private String Description;
	private URI resourceAddress;//web or file address.
	private List<String> tags;//tags to associate with this bookmark
	private Date createdDate;
	private Date lastAccessedDate;
	private int numberOfAccesses;//how many times has this bookmark been viewed.
	
	private String resourceType;//System specific program to run this bookmark: Web Browser, terminal, File browser, Text file, etc...
	private String context;//Where is this bookmark useful
	private String sharing;//Keep on this file system only, Share with my other systems, Share with everyone.
	
	
	private Map<String, String> settingsMap;
}
