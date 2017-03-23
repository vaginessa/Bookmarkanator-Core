package com.bookmarkanator.util;

import java.util.*;

/**
 * This is a class used to encapsulate all the options that are possible from the search panel.
 *
 * Adding it to a class so it will be easier to save searches.
 */
public class SearchParam
{
    public static final String INCLUDE_BOOKMARKS_WITH_ANY_TAGS = "ANY TAG";
    public static final String INCLUDE_BOOKMARKS_WITH_ALL_TAGS = "ALL TAGS";
    public static final String INCLUDE_BOOKMARKS_WITHOUT_TAGS = "WITHOUT TAGS";

    private String searhTerm;
    private Date startDate;
    private Date endDate;
    private boolean searchBookmarkText;
    private boolean searchBookmarkNames;
    private boolean searchBookmarkTypes;
    private boolean searchTags;
    private Set<String> tags;
    private String tagMode;

    public String getSearhTerm()
    {
        return searhTerm;
    }

    public void setSearhTerm(String searhTerm)
    {
        this.searhTerm = searhTerm;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public boolean isSearchBookmarkText()
    {
        return searchBookmarkText;
    }

    public void setSearchBookmarkText(boolean searchBookmarkText)
    {
        this.searchBookmarkText = searchBookmarkText;
    }

    public boolean isSearchBookmarkNames()
    {
        return searchBookmarkNames;
    }

    public void setSearchBookmarkNames(boolean searchBookmarkNames)
    {
        this.searchBookmarkNames = searchBookmarkNames;
    }

    public boolean isSearchBookmarkTypes()
    {
        return searchBookmarkTypes;
    }

    public void setSearchBookmarkTypes(boolean searchBookmarkTypes)
    {
        this.searchBookmarkTypes = searchBookmarkTypes;
    }

    public boolean isSearchTags()
    {
        return searchTags;
    }

    public void setSearchTags(boolean searchTags)
    {
        this.searchTags = searchTags;
    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }

    public String getTagMode()
    {
        return tagMode;
    }

    public void setTagMode(String tagMode)
    {
        this.tagMode = tagMode;
    }
}
