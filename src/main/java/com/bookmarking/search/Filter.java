package com.bookmarking.search;

import java.text.*;
import java.util.*;
import com.bookmarking.bookmark.*;

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
        if (tags == null || tags.isEmpty())
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
        if (tags == null || tags.isEmpty())
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
     * Filters a list of bookmark by date range. If the bookmark date is null include it.
     */
    public Filter filterByCreatedDate(Date includeIfAfter, Date includeIfBefore)
    {
        for (AbstractBookmark bk : this.results())
        {
            boolean s1;
            boolean s2;

            if (includeIfBefore==null)
            {
                s1 = true;
            }
            else
            {
                if (bk.getCreationDate()==null)
                {
                    s1 = true;
                }
                else
                {
                    s1 = bk.getCreationDate().before(includeIfBefore);
                }
            }

            if (includeIfAfter==null)
            {
                s2 = true;
            }
            else
            {
                if (bk.getCreationDate()==null)
                {
                    s2 = true;
                }
                else
                {
                    s2 = bk.getCreationDate().after(includeIfAfter);
                }
            }

            if (s1 && s2)
            {
                tempList.add(bk);
            }
        }

        tempToMainList();
        return this;
    }

    /**
     * Filters a list of bookmark by date range. If the bookmark date is null include it.
     */
    public Filter filterByLastAccessedDate(Date includeIfAfter, Date includeIfBefore)
    {
        for (AbstractBookmark bk : this.results())
        {
            boolean s1;
            boolean s2;

            if (includeIfBefore==null)
            {
                s1 = true;
            }
            else
            {
                if (bk.getCreationDate()==null)
                {
                    s1 = true;
                }
                else
                {
                    s1 = bk.getLastAccessedDate().before(includeIfBefore);
                }
            }

            if (includeIfAfter==null)
            {
                s2 = true;
            }
            else
            {
                if (bk.getCreationDate()==null)
                {
                    s2 = true;
                }
                else
                {
                    s2 = bk.getLastAccessedDate().after(includeIfAfter);
                }
            }

            if (s1 && s2)
            {
                tempList.add(bk);
            }
        }

        tempToMainList();
        return this;
    }

    /**
     * Filters a list of bookmark by date range.
     *
     * @param includeIfAfter  Include the bookmark if the date is equal to or after this date.
     * @param includeIfBefore Include the bookmark if the date is before this date only.
     * @return A list of date range fildtered bookmark.
     */

    public Filter keepWithinDateRange(Date includeIfAfter, Date includeIfBefore, SearchOptions.DateType dateType)
    {
        if (dateType == SearchOptions.DateType.CREATION_DATE)
        {
            filterByCreatedDate(includeIfAfter,includeIfBefore);
        }
        else
        {
            filterByLastAccessedDate(includeIfAfter, includeIfBefore);
        }

        return this;
    }

    /**
     * Excludes bookmark that have any of the specified exclusion strings in the bookmark names.
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

    //    /**
    //     * Excludes bookmark that have any of the specified exclusion strings in the bookmark text.
    //     *
    //     * @param exclusions
    //     * @return
    //     */
    //    public Filter excludeTextThatContains(Set<String> exclusions)
    //    {
    //        continueA:
    //        for (AbstractBookmark bk : this.results())
    //        {
    //            for (String s : exclusions)
    //            {
    //                if (bk.getText().contains(s))
    //                {
    //                    continue continueA;
    //                }
    //            }
    //            tempList.add(bk);
    //        }
    //
    //        tempToMainList();
    //        return this;
    //    }

    /**
     * Excludes bookmark that have any of the specified exclusion strings in the bookmark tags.
     *
     * @param exclusions
     * @return
     */
    public Filter excludeIfTagsContainAny(Set<String> exclusions)
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
     * Excludes bookmark that have any of the specified exclusion strings that match tags in bookmark.
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
        if (searchOptions.getSelectedBKTypes() != null)
        {
            //Remove non selected bookmark types
            this.keepBookmarkTypesByClassName(searchOptions.getSelectedBKTypes());
        }

        //Remove all that don't fit within the date range.
        if (searchOptions.getStartDate() != null || searchOptions.getEndDate() != null)
        {
            Date start;

            if (searchOptions.getStartDate() != null)
            {
                start = searchOptions.getStartDate();
            }
            else
            {
                // Super old date as beginning range.
                start = new Date(Long.MIN_VALUE);
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

            this.keepWithinDateRange(start, end, searchOptions.getDateType());
        }

        //Filter by selected tags blocks.
        for (TagsInfo tagsInfo : searchOptions.getTagGroups())
        {
            switch (tagsInfo.getOperation())
            {
                case ALL_TAGS:
                    this.keepWithAllTags(tagsInfo.getTags());
                    break;
                case ANY_TAG:
                    this.keepWithAnyTag(tagsInfo.getTags());
                    break;
                case WITHOUT_TAGS:
                    this.excludeWithTags(tagsInfo.getTags());
                    break;
            }
        }

        // Note: Cannot do text search here. Must do search prior to calling this method. This is just a filter method not a search method.
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
     * @param bookmarks The bookmark to assign to the current filter class.
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
