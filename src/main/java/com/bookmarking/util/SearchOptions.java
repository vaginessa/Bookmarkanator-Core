package com.bookmarking.util;

import java.util.*;

/**
 * This is a class used to encapsulate all the search options that are possible. Encapsulating them here allows the saving of search
 * recipes, and it simplifies the search interface.
 */
public class SearchOptions
{
    public static final String INCLUDE_BOOKMARKS_WITH_ANY_TAGS = "ANY TAG";
    public static final String INCLUDE_BOOKMARKS_WITH_ALL_TAGS = "ALL TAGS";
    public static final String INCLUDE_BOOKMARKS_WITHOUT_TAGS = "WITHOUT TAGS";

    private String searchTerm;
    private Date startDate;
    private Date endDate;
    private boolean searchBookmarkText = true;
    private boolean searchBookmarkNames = true;
    private boolean searchBookmarkTypes = true;
    private boolean searchTags = true;
    private List<TagsInfo> tagsInfoList;
    private Set<String> selectedBookmarkTypes;

    public SearchOptions()
    {
        tagsInfoList = new ArrayList<>();
        selectedBookmarkTypes = new HashSet<>();
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

    public void addTags(String operation, Set<String> tags)
    {
        TagsInfo tmp = new TagsInfo();
        tmp.setTags(tags);
        tmp.setOperation(operation);

        tagsInfoList.add(tmp);
    }

    public Set<String> getFirst(String operation)
    {
        for (TagsInfo tagsInfo : tagsInfoList)
        {
            if (tagsInfo.getOperation().equalsIgnoreCase(operation))
            {
                return tagsInfo.getTags();
            }
        }

        return null;
    }

    public Set<String> getLast(String operation)
    {
        Set<String> res = null;

        for (TagsInfo tagsInfo : tagsInfoList)
        {
            if (tagsInfo.getOperation().equalsIgnoreCase(operation))
            {
                res = tagsInfo.getTags();
            }
        }

        return res;
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
        selectedBookmarkTypes.add(bkType);
    }

    public void setUnselectedBKType(String bkType)
    {
        selectedBookmarkTypes.remove(bkType);
    }

    public void setSelectAllBKTypes(Set<String> bkTypes)
    {
        selectedBookmarkTypes.clear();
        selectedBookmarkTypes.addAll(bkTypes);
    }

    public void setUnselectAllBKTypes()
    {
        selectedBookmarkTypes.clear();
    }

    public Set<String> getSelectedTypes()
    {
        return Collections.unmodifiableSet(selectedBookmarkTypes);
    }

    public class TagsInfo implements Comparable
    {
        private Set<String> tags;
        private Date date;
        private String operation;
        private UUID id;

        public TagsInfo()
        {
            tags = new HashSet<>();
            date = new Date();
            id = UUID.randomUUID();
            operation = SearchOptions.INCLUDE_BOOKMARKS_WITH_ALL_TAGS;
        }

        public Set<String> getTags()
        {
            return tags;
        }

        public void setTags(Set<String> tags)
        {
            this.tags.clear();
            this.tags.addAll(tags);
        }

        public Date getDate()
        {
            return date;
        }

        public void setDate(Date date)
        {
            this.date = date;
        }

        public String getOperation()
        {
            return operation;
        }

        public void setOperation(String operation)
        {
            this.operation = operation;
        }

        public UUID getId()
        {
            return id;
        }

        public void setId(UUID id)
        {
            this.id = id;
        }

        @Override
        public int compareTo(Object o)
        {
            if (o instanceof TagsInfo)
            {
                TagsInfo tmp = (TagsInfo) o;
                id.compareTo(tmp.getId());
            }
            if (o instanceof UUID)
            {
                return id.compareTo((UUID) o);
            }
            return 1;
        }
    }
}
