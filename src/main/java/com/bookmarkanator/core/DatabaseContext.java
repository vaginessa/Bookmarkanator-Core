package com.bookmarkanator.core;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;

/**
 * At some point if a database is used this context could be used.
 */
public class DatabaseContext implements ContextInterface
{
    @Override
    public void setBKIOInterface(BKIOInterface bkioInterface)
    {
        //generally this should be set by the DatabaseIO class when it creates this context.
    }

    @Override
    public BKIOInterface getBKIOInterface()
    {
        return null;
    }

    @Override
    public Set<UUID> getDependents(UUID bookmarkId)
    {
        return null;
    }

    @Override
    public Set<UUID> getDependsOn(UUID bookmarkId)
    {
        return null;
    }

    @Override
    public int addDependency(UUID theBookmark, UUID dependingBookmark)
    {
        return 0;
    }

    @Override
    public int removeDependency(UUID theBookmark, UUID dependingBookmark)
    {
        return 0;
    }

    @Override
    public Set<UUID> getBookmarkIDs()
    {
        return null;
    }

    @Override
    public Set<AbstractBookmark> getBookmarks()
    {
        return null;
    }

    @Override
    public AbstractBookmark getBookmark(UUID uuid)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> getBookmarks(Set<UUID> bookmarkIds)
    {
        return null;
    }

    @Override
    public void addAll(List<AbstractBookmark> bookmarks)
        throws Exception
    {

    }

    @Override
    public void addBookmark(AbstractBookmark bookmark)
        throws Exception
    {

    }

    @Override
    public AbstractBookmark removeBookmark(UUID bookmarkID)
    {
        return null;
    }

    @Override
    public void updateBookmark(AbstractBookmark bookmark)
        throws Exception
    {

    }

    @Override
    public int getNumSearchResults()
    {
        return 0;
    }

    @Override
    public void setNumSearchResults(int numSearchResults)
    {

    }

    @Override
    public List<AbstractBookmark> searchAll(String text)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> searchBookmarkNames(String text)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> searchTagsLoosly(String text)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> searchTagsExact(Set<String> tags)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> searchTagsFullMatch(Set<String> tags)
        throws Exception
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> searchBookmarkTypes(String text)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> searchBookmarkText(String text)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> filterHasAnyTag(List<AbstractBookmark> bookmarkList, Set<String> tags)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> filterHasAllTags(List<AbstractBookmark> bookmarkList, Set<String> tags)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> filterByBookmarkType(List<AbstractBookmark> bookmarkList, Set<String> bookmarkTypeNames)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> filterDate(List<AbstractBookmark> bookmarkList, Date includeIfAfter, Date includeIfBefore)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> excludeBookmarksWithNamesContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> excludeBookmarksContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> excludeBookmarkTagsContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        return null;
    }
}
