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
    private long offsetFromNow;

    // Which date type to use for filtering.
    private DateType dateType;

    // Aspects of bookmarks to search
    private boolean searchBookmarkText = true;
    private boolean searchBookmarkNames = true;
    private boolean searchBookmarkTypes = true;
    private boolean searchTags = true;

    // List of tag search options along with type (all, any, none)
    private List<Operation> operationList;

    // Exclude all bookmark types not present, unless the list is null. If null include all.
    private Set<String> selectedBookmarkTypes;

    public SearchOptions()
    {
        operationList = new ArrayList<>();
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

    public long getOffsetFromNow()
    {
        return offsetFromNow;
    }

    public void setOffsetFromNow(long offsetFromNow)
    {
        this.offsetFromNow = offsetFromNow;
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

    public Operation add(Operation.TagOptions operation, String tag)
    {
        Set<String> tags = new HashSet<>();
        tags.add(tag);
        return add(operation, tags);
    }

    public Operation add(Operation.TagOptions operation, Set<String> tags)
    {
        Operation tagsInfo = new Operation();
        tagsInfo.setOperation(operation);
        tagsInfo.setTags(tags);

        operationList.add(tagsInfo);

        return tagsInfo;
    }

    public void add(Operation operation)
    {
        if (operation != null)
        {
           operationList.add(operation);
        }
    }

    public void remove(Operation operation)
    {
        operationList.remove(operation);
    }

    public List<Operation> getTagOperations()
    {
        List<Operation> res = new ArrayList<>();
        Set<Operation> removals = new HashSet<>();

        for (Operation o: operationList)
        {
            if (o.getTags().isEmpty())
            {
                removals.add(o);
            }
            else
            {
                res.add(o);
            }
        }

        operationList.removeAll(removals);

        return res;
    }

    public Operation getLastOperation()
    {
        if (operationList==null || operationList.isEmpty())
        {
            return null;
        }

        return operationList.get(operationList.size()-1);
    }

    public void clear()
    {
        operationList.clear();
    }

    public Set<String> getTagsPresent()
    {
        Set<String> res = new HashSet<>();
        Set<Operation> removals = new HashSet<>();

        for (Operation o: operationList)
        {
            if (o.getTags().isEmpty())
            {
                removals.add(o);
            }
            else
            {
                res.addAll(o.getTags());
            }
        }

        operationList.removeAll(removals);

        return res;
    }

    public void removeAll(String tag)
    {
        for (Operation o : operationList)
        {
            o.getTags().remove(tag);
        }
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
        if (selectedBookmarkTypes == null)
        {
            return null;
        }

        return Collections.unmodifiableSet(selectedBookmarkTypes);
    }

    public DateType getDateType()
    {
        return dateType;
    }

    public void setDateType(DateType dateType)
    {
        this.dateType = dateType;
    }

    // ============================================================
    // Private Methods
    // ============================================================

    private void initSelectedTypes()
    {
        if (selectedBookmarkTypes == null)
        {
            selectedBookmarkTypes = new HashSet<>();
        }
    }
}
