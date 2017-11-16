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
    private Set<String> tagsPresent;

    // Exclude all bookmark types not present, unless the list is null. If null include all.
    private Set<String> selectedBookmarkTypes;

    public SearchOptions()
    {
        tagsInfoList = new ArrayList<>();
        tagsPresent = new HashSet<>();
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

    public boolean getSearchBookmarkText()
    {
        return searchBookmarkText;
    }

    public void setSearchBookmarkText(boolean searchBookmarkText)
    {
        this.searchBookmarkText = searchBookmarkText;
    }

    public boolean getSearchBookmarkNames()
    {
        return searchBookmarkNames;
    }

    public void setSearchBookmarkNames(boolean searchBookmarkNames)
    {
        this.searchBookmarkNames = searchBookmarkNames;
    }

    public boolean getSearchBookmarkTypes()
    {
        return searchBookmarkTypes;
    }

    public void setSearchBookmarkTypes(boolean searchBookmarkTypes)
    {
        this.searchBookmarkTypes = searchBookmarkTypes;
    }

    public boolean getSearchTags()
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

        computeTagsPresent();
    }

    public void addTags(TagsInfo tagsInfo)
    {
        tagsInfoList.add(tagsInfo);

        computeTagsPresent();
    }

    public List<TagsInfo> getTagGroups()
    {
        return Collections.unmodifiableList(tagsInfoList);
    }

    public TagsInfo getLastTagsInfo()
    {
        if (tagsInfoList==null || tagsInfoList.isEmpty())
        {
            return null;
        }

        return tagsInfoList.get(tagsInfoList.size()-1);
    }

    public void clearTagGroups()
    {
        tagsInfoList.clear();
        tagsPresent.clear();
    }

    public Set<String> getTagsPresent()
    {
        return Collections.unmodifiableSet(tagsPresent);
    }

    public void removeAllTags(String tag)
    {
        List<TagsInfo> removals = new ArrayList<>();

        if (tagsInfoList!=null)
        {
            for (TagsInfo tagsInfo: tagsInfoList)
            {
                tagsInfo.getTags().remove(tag);

                if (tagsInfo.getTags().isEmpty())
                {
                    removals.add(tagsInfo);
                }
            }
        }

        tagsInfoList.removeAll(removals);

        tagsPresent.remove(tag);
    }

    public void removeTag(String tag, TagsInfo tagsInfo)
    {
        if (tagsInfo !=null)
        {
            tagsInfo.getTags().remove(tag);

            if (tagsInfo.getTags().isEmpty())
            {
                tagsInfoList.remove(tagsInfo);
            }
        }

        tagsPresent.remove(tag);
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

    public void computeTagsPresent()
    {
        tagsPresent.clear();

        for (TagsInfo tagsInfo: tagsInfoList)
        {
            tagsPresent.addAll(tagsInfo.getTags());
        }
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
