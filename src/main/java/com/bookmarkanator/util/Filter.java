package com.bookmarkanator.util;

import java.text.*;
import java.util.*;
import com.bookmarkanator.bookmarks.*;

/**
 * This class is used to filter a list of bookmarks.
 */
public class Filter
{
    private static Filter filter;

    private List<AbstractBookmark> bookmarkList;
    private List<AbstractBookmark> tempList;
    private boolean ignoreCase;

    private Filter()
    {
        bookmarkList = new ArrayList<>();
        tempList = new ArrayList<>();
        ignoreCase = true;
    }

    private Filter bookmarksList(List<AbstractBookmark> bookmarks)
    {
        this.bookmarkList.clear();
        this.bookmarkList.addAll(bookmarks);
        return this;
    }

    private Filter bookmarksSet(Set<AbstractBookmark> bookmarks)
    {
        this.bookmarkList.clear();
        this.bookmarkList.addAll(bookmarks);
        return this;
    }

    public List<AbstractBookmark> results()
    {
        return new ArrayList<>(bookmarkList);
    }

    public Filter sortAscending()
    {
        Collections.sort(bookmarkList);
        return this;
    }

    public Filter sortDescending()
    {
        Collections.reverse(bookmarkList);
        return this;
    }

    public Filter keepWithAnyTag(Set<String> tags)
    {
        if (tags==null || tags.isEmpty())
        {
            return this;
        }

        for (AbstractBookmark bk : this.results())
        {
            for (String tag : tags)
            {
                if (bk.getTags().contains(tag))
                {
                    tempList.add(bk);
                    break;
                }
            }
        }

        tempToMainList();
        return this;
    }

    public Filter keepWithAllTags(Set<String> tags)
    {
        if (tags==null || tags.isEmpty())
        {
            return this;
        }

        for (AbstractBookmark bk : this.bookmarkList)
        {
                if (bk.getTags().containsAll(tags))
                {
                    tempList.add(bk);
                }
        }

        tempToMainList();
        return this;
    }

    public Filter keepBookmarkTypesByTypeName(Set<String> bookmarkTypeNames)
    {
        for (AbstractBookmark bk : this.results())
        {
            if (bookmarkTypeNames.contains(bk.getTypeName()))
            {
                tempList.add(bk);
                break;
            }
        }

        tempToMainList();
        return this;
    }

    public Filter keepBookmarkTypesByClassName(Set<String> bookmarkClassNames)
    {
        for (AbstractBookmark bk : this.results())
        {
            if (bookmarkClassNames.contains(bk.getClass().getCanonicalName()))
            {
                tempList.add(bk);
            }
        }

        tempToMainList();
        return this;
    }

    /**
     * Filters a list of bookmarks by date range.
     *
     * @param includeIfAfter  Include the bookmark if the date is equal to or after this date.
     * @param includeIfBefore Include the bookmark if the date is before this date only.
     * @return A list of date range fildtered bookmarks.
     */

    public Filter keepWithinDateRange( Date includeIfAfter, Date includeIfBefore)
    {

        for (AbstractBookmark bk : this.results())
        {
            if (bk.getCreationDate().compareTo(includeIfAfter) > -1 && bk.getCreationDate().compareTo(includeIfBefore) < 0)
            {
                tempList.add(bk);
            }
        }

        tempToMainList();
        return this;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark names.
     *
     * @param exclusions
     * @return
     */
    public Filter excludeNamesWithText(List<String> exclusions)
    {
        continueA:
        for (AbstractBookmark bk : this.results())
        {
            for (String s : exclusions)
            {
                if (bk.getName().contains(s))
                {
                    continue continueA;
                }
            }
            tempList.add(bk);
        }

        tempToMainList();
        return this;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark text.
     *
     * @param exclusions
     * @return
     */
    public Filter excludeTextWithText(Set<String> exclusions)
    {
        continueA:
        for (AbstractBookmark bk : this.results())
        {
            for (String s : exclusions)
            {
                if (bk.getText().contains(s))
                {
                    continue continueA;
                }
            }
            tempList.add(bk);
        }

        tempToMainList();
        return this;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark tags.
     *
     * @param exclusions
     * @return
     */
    public Filter excludeWithTagsWithText(Set<String> exclusions)
    {
        continueA:
        for (AbstractBookmark bk : this.results())
        {
            for (String s : exclusions)
            {
                for (String st : bk.getTags())
                {
                    if (st.contains(s))
                    {
                        continue continueA;
                    }
                }
            }
            tempList.add(bk);
        }

        tempToMainList();
        return this;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings that match tags in bookmarks.
     *
     * @param exclusions
     * @return
     */
    public Filter excludeWithTags(Set<String> exclusions)
    {
        continueA:
        for (AbstractBookmark abs : this.bookmarkList)
        {
            Set<String> tags = abs.getTags();
            for (String s : exclusions)
            {
                if (tags.contains(s))
                {
                    continue continueA;
                }
            }
            tempList.add(abs);
        }

        tempToMainList();
        return this;
    }

    public Filter includeIfIn(Set<AbstractBookmark> include)
    {
        for (AbstractBookmark abs : this.bookmarkList)
        {
            if (include.contains(abs))
            {
                tempList.add(abs);
            }
        }
        tempToMainList();
        return this;
    }

    public Filter includeIfIn(List<AbstractBookmark> include)
    {
        for (AbstractBookmark abs : this.bookmarkList)
        {
            if (include.contains(abs))
            {
                tempList.add(abs);
            }
        }
        tempToMainList();
        return this;
    }

    public Filter filterBySearchOptions(SearchOptions searchOptions)
        throws ParseException
    {
        //Remove non selected bookmark types
        this.keepBookmarkTypesByClassName(searchOptions.getSelectedTypes());

        //Remove all that dont fit within the date range.
        if (searchOptions.getStartDate()!=null || searchOptions.getEndDate()!=null)
        {
            Date start;

            if (searchOptions.getStartDate() != null)
            {
                start = searchOptions.getStartDate();
            }
            else
            {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                start = sdf.parse("01/01/1971");
            }

            Date end;

            if (searchOptions.getEndDate() != null)
            {
                end = searchOptions.getEndDate();
            }
            else
            {
                end = new Date();
            }

            this.keepWithinDateRange(start, end);
        }

        //Filter by selected tags blocks.
        for (SearchOptions.TagsInfo tagsInfo : searchOptions.getTagGroups())
        {
            switch (tagsInfo.getOperation())
            {
                case SearchOptions.INCLUDE_BOOKMARKS_WITH_ALL_TAGS:
                    this.keepWithAllTags(tagsInfo.getTags());
                    break;
                case SearchOptions.INCLUDE_BOOKMARKS_WITH_ANY_TAGS:
                    this.keepWithAnyTag(tagsInfo.getTags());
                    break;
                case SearchOptions.INCLUDE_BOOKMARKS_WITHOUT_TAGS:
                    this.excludeWithTags(tagsInfo.getTags());
                    break;
            }
        }

        //Note: Cannot do search here. Must do search prior to calling this method. This is just a filter method not a search method.
        return this;
    }

    private void tempToMainList()
    {
        this.bookmarkList.clear();
        this.bookmarkList.addAll(tempList);
        this.tempList.clear();
    }

    /**
     * Static initializer/getter block
     *
     * @param bookmarks The bookmarks to assign to the current filter class.
     * @return The most recently created Filter object or a newly created one.
     */
    public static Filter use(Collection<AbstractBookmark> bookmarks)
    {
        if (Filter.filter == null)
        {
            Filter.filter = new Filter();
        }

        if (bookmarks instanceof List)
        {
            Filter.filter.bookmarksList((List) bookmarks);
        }
        else if (bookmarks instanceof Set)
        {
            Filter.filter.bookmarksSet((Set) bookmarks);
        }

        return Filter.filter;
    }

}
