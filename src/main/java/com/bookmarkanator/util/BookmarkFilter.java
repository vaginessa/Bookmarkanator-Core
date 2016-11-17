package com.bookmarkanator.util;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

public class BookmarkFilter
{
    private List<AbstractBookmark> bookmarkList;

    public BookmarkFilter()
    {
        bookmarkList = new ArrayList<>();
    }

    public BookmarkFilter setBookmarks(List<AbstractBookmark> bookmarks)
    {
        this.bookmarkList.clear();
        this.bookmarkList.addAll(bookmarks);
        return this;
    }

    public List<AbstractBookmark> results()
    {
        return bookmarkList;
    }

    public BookmarkFilter sortAscending()
    {
        Collections.sort(bookmarkList);
        return this;
    }

    public BookmarkFilter sortDescending()
    {
        Collections.reverse(bookmarkList);
        return this;
    }

    public BookmarkFilter keepWithAnyTag(Set<String> tags)
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

    public BookmarkFilter keepWithAllTags(Set<String> tags)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : this.results())
        {
            if (bk.getTags().containsAll(tags))
            {
                results.add(bk);
            }
        }

        this.bookmarkList = results;
        return this;
    }

    public BookmarkFilter keepBookmarkTypes(Set<String> bookmarkTypeNames)
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

    public BookmarkFilter keepWithinDateRange(List<AbstractBookmark> bookmarkList, Date includeIfAfter, Date includeIfBefore)
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
    public BookmarkFilter excludeNamesWithText(Set<String> exclusions)
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
    public BookmarkFilter excludeTextWithText(Set<String> exclusions)
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
    public BookmarkFilter excludeWithTagsWithText(Set<String> exclusions)
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
}
