package com.bookmarkanator.core;

import java.util.*;
import org.apache.logging.log4j.*;

public class BKDependency
{
    private static final Logger logger = LogManager.getLogger(BKDependency.class.getCanonicalName());
    private UUID bookmarkId;
    private String typeString;
    private String action;

    public UUID getBookmarkId()
    {
        return bookmarkId;
    }

    public void setBookmarkId(UUID bookmarkId)
    {
        this.bookmarkId = bookmarkId;
    }

    public String getTypeString()
    {
        return typeString;
    }

    public void setTypeString(String typeString)
    {
        this.typeString = typeString;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }
}
