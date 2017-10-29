package com.bookmarking.file;

import java.io.*;
import java.util.*;
import javax.xml.bind.annotation.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.search.*;

@XmlRootElement(name="searchGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchGroup implements Serializable
{
    @XmlElement
    private Search<UUID> bookmarkNames;

    // Search bookmark type names (text, web, terminal etc...)
    @XmlElement
    private Search<UUID> bookmarkTypeNames;
    // Searches through the search words the bookmarks contain
    @XmlElement
    private Search<UUID> bookmarkText;
    @XmlElement
    private Search<UUID> bookmarkTags;
    @XmlElement
    private Search<UUID> bookmarkTypeLocations;

    // The number of desired search results. It will try new search methods until the number of results is obtained.
    private int numSearchResults;

    public SearchGroup()
    {
        bookmarkNames = new Search<>();
        bookmarkTypeNames = new Search<>();
        bookmarkText = new Search<>();
        bookmarkTags = new Search<>();
        bookmarkTypeLocations = new Search<>();
        numSearchResults = 20;
    }

    public void removeBookmark(UUID bookmarkId)
    {
        bookmarkNames.remove(bookmarkId);
        bookmarkTypeNames.remove(bookmarkId);
        bookmarkText.remove(bookmarkId);
        bookmarkTags.remove(bookmarkId);
        bookmarkTypeLocations.remove(bookmarkId);
    }

    public void addBookmark(AbstractBookmark abstractBookmark)
        throws Exception
    {
        if (!bookmarkNames.contains(abstractBookmark.getId()))
        {
            bookmarkNames.add(abstractBookmark.getId(), abstractBookmark.getName());
        }

        if (!bookmarkTypeNames.contains(abstractBookmark.getId()))
        {
            bookmarkTypeNames.add(abstractBookmark.getId(), abstractBookmark.getTypeName());
        }

        if (!bookmarkText.contains(abstractBookmark.getId()))
        {
            bookmarkText.add(abstractBookmark.getId(), abstractBookmark.getSearchWords());
        }

        if (!bookmarkTags.contains(abstractBookmark.getId()))
        {
            bookmarkTags.add(abstractBookmark.getId(), abstractBookmark.getTags());
        }

        if (!bookmarkTypeLocations.contains(abstractBookmark.getId()))
        {
            bookmarkTypeLocations.add(abstractBookmark.getId(), new HashSet<>(abstractBookmark.getTypeLocation()));
        }
    }

    public Set<UUID> applySearchOptions(SearchOptions searchOptions)
    {
        Set<UUID> res = new HashSet<>();

        // Note: Order of search is important. Names and tags should generally be searched first.

        if (searchOptions.getSearchBookmarkNames())
        {
            res.addAll(bookmarkNames.searchAll(searchOptions.getSearchTerm(), getNumSearchResults()));
        }
        if (searchOptions.getSearchTags() && res.size()<getNumSearchResults())
        {
            res.addAll(bookmarkTags.searchAll(searchOptions.getSearchTerm(), getNumSearchResults()));
        }
        if (searchOptions.getSearchBookmarkTypes() && res.size()<getNumSearchResults())
        {
            res.addAll(bookmarkTypeNames.searchAll(searchOptions.getSearchTerm(), getNumSearchResults()));
        }
        if (searchOptions.getSearchBookmarkText() && res.size()<getNumSearchResults())
        {
            res.addAll(bookmarkText.searchAll(searchOptions.getSearchTerm(), getNumSearchResults()));
        }

        return res;
    }

    public Search<UUID> getBookmarkNames()
    {
        return bookmarkNames;
    }

    public void setBookmarkNames(Search<UUID> bookmarkNames)
    {
        this.bookmarkNames = bookmarkNames;
    }

    public Search<UUID> getBookmarkTypeNames()
    {
        return bookmarkTypeNames;
    }

    public void setBookmarkTypeNames(Search<UUID> bookmarkTypeNames)
    {
        this.bookmarkTypeNames = bookmarkTypeNames;
    }

    public Search<UUID> getBookmarkText()
    {
        return bookmarkText;
    }

    public void setBookmarkText(Search<UUID> bookmarkText)
    {
        this.bookmarkText = bookmarkText;
    }

    public Search<UUID> getBookmarkTags()
    {
        return bookmarkTags;
    }

    public void setBookmarkTags(Search<UUID> bookmarkTags)
    {
        this.bookmarkTags = bookmarkTags;
    }

    public Search<UUID> getBookmarkTypeLocations()
    {
        return bookmarkTypeLocations;
    }

    public void setBookmarkTypeLocations(Search<UUID> bookmarkTypeLocations)
    {
        this.bookmarkTypeLocations = bookmarkTypeLocations;
    }

    public int getNumSearchResults()
    {
        return numSearchResults;
    }

    public void setNumSearchResults(int numSearchResults)
    {
        this.numSearchResults = numSearchResults;
    }
}
