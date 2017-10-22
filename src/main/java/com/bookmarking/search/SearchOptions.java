package com.bookmarking.search;

import java.util.*;

/**
 * This is a class used to encapsulate all the search options that are possible. Encapsulating them here allows the saving of search
 * recipes, and it simplifies the search interface.
 */
public class SearchOptions
{
    public enum DateType
    {
        CREATION_DATE,
        ACCESSED_DATE
    }

    private String searchTerm;

    // Date range's to locate bookmarks in (inclusive) can be null.
    private Date startDate;
    private Date endDate;

    // Which date type to use for filtering.
    private DateType dateType;

    // Aspects of bookmarks to search
    private boolean searchBookmarkText = true;
    private boolean searchBookmarkNames = true;
    private boolean searchBookmarkTypes = true;
    private boolean searchTags = true;

    // List of tag search options along with type (all, any, none)
    private List<TagsInfo> tagsInfoList;

    // Exclude all bookmark types not present, unless the list is null. If null include all.
    private Set<String> selectedBookmarkTypes;

    public SearchOptions()
    {
        tagsInfoList = new ArrayList<>();
    }

    public String getSearchTerm()
    {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm)
    {
        this.searchTerm = searchTerm;
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

    public void addTags(TagsInfo.TagOptions operation, Set<String> tags)
    {
        TagsInfo tmp = new TagsInfo();
        tmp.setTags(tags);
        tmp.setOperation(operation);

        tagsInfoList.add(tmp);
    }

    public List<TagsInfo> getTagGroups()
    {
        return tagsInfoList;
    }

    public Set<String> getTagsFromAllGroups()
    {
        Set<String> res = new HashSet<>();

        for (TagsInfo tagsInfo : tagsInfoList)
        {
            res.addAll(tagsInfo.getTags());
        }

        return res;
    }

    public void setSelectedBKType(String bkType)
    {
        initSelectedTypes();
        selectedBookmarkTypes.add(bkType);
    }

    public void setUnselectedBKType(String bkType)
    {
        initSelectedTypes();
        selectedBookmarkTypes.remove(bkType);
    }

    public void setSelectAllBKTypes(Set<String> bkTypes)
    {
        initSelectedTypes();
        selectedBookmarkTypes.clear();
        selectedBookmarkTypes.addAll(bkTypes);
    }

    public void setUnselectAllBKTypes()
    {
        initSelectedTypes();
        selectedBookmarkTypes.clear();
    }

    public Set<String> getSelectedBKTypes()
    {
        if (selectedBookmarkTypes==null)
        {
            return null;
        }

        return Collections.unmodifiableSet(selectedBookmarkTypes);
    }

    private void initSelectedTypes()
    {
        if (selectedBookmarkTypes==null)
        {
            selectedBookmarkTypes = new HashSet<>();
        }
    }

    public DateType getDateType()
    {
        return dateType;
    }

    public void setDateType(DateType dateType)
    {
        this.dateType = dateType;
    }
}
