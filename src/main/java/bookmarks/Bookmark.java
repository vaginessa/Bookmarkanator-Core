package bookmarks;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Bookmark {
	private UUID tagUUID;
	private UUID ownerID;//who does this tag belong to?
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
