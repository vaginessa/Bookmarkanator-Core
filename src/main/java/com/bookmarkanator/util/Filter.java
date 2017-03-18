package com.bookmarkanator.util;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

public class Filter
{
    private List<AbstractBookmark> bookmarkList;
    private boolean ignoreCase;
    private static Filter filter;

    private Filter()
    {
        bookmarkList = new ArrayList<>();
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
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : this.results())
        {
            for (String tag : tags)
            {
                if (bk.getTags().contains(tag))
                {
                    results.add(bk);
                    break;
                }
            }
        }

        this.bookmarkList = results;
        return this;
    }

    public Filter keepWithAllTags(Set<String> tags)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : this.results())
        {
            if (tags!=null && !tags.isEmpty())
            {
                if (bk.getTags().containsAll(tags))
                {
                    results.add(bk);
                }
            }
        }

        this.bookmarkList = results;
        return this;
    }

    public Filter keepBookmarkTypes(Set<String> bookmarkTypeNames)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : this.results())
        {
            for (String s : bookmarkTypeNames)
            {
                if (bk.getTypeName().equals(s))
                {
                    results.add(bk);
                    break;
                }
            }
        }

        this.bookmarkList = results;

        return this;
    }

    /**
     * Filters a list of bookmarks by date range.
     *
     * @param bookmarkList    The list to filter
     * @param includeIfAfter  Include the bookmark if the date is equal to or after this date.
     * @param includeIfBefore Include the bookmark if the date is before this date only.
     * @return A list of date range fildtered bookmarks.
     */

    public Filter keepWithinDateRange(List<AbstractBookmark> bookmarkList, Date includeIfAfter, Date includeIfBefore)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : this.results())
        {
            if (bk.getCreationDate().compareTo(includeIfAfter) > -1 && bk.getCreationDate().compareTo(includeIfBefore) < 0)
            {
                results.add(bk);
            }
        }

        this.bookmarkList = results;
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
        List<AbstractBookmark> results = new ArrayList<>();

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
            results.add(bk);
        }

        this.bookmarkList = results;
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
        List<AbstractBookmark> results = new ArrayList<>();

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
            results.add(bk);
        }

        this.bookmarkList = results;
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
        List<AbstractBookmark> results = new ArrayList<>();

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
            results.add(bk);
        }

        this.bookmarkList = results;
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
        List<AbstractBookmark> results = new ArrayList<>();

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
            results.add(abs);
        }

        this.bookmarkList = results;
        return this;
    }

    public Filter includeIfIn(Set<AbstractBookmark> include)
    {
        List<AbstractBookmark> res = new ArrayList<>();

        for (AbstractBookmark abs: this.bookmarkList)
        {
            if (include.contains(abs))
            {
                res.add(abs);
            }
        }
        this.bookmarkList = res;
        return this;
    }

    public Filter includeIfIn(List<AbstractBookmark> include)
    {
        List<AbstractBookmark> res = new ArrayList<>();

        for (AbstractBookmark abs: this.bookmarkList)
        {
            if (include.contains(abs))
            {
                res.add(abs);
            }
        }
        this.bookmarkList = res;
        return this;
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
