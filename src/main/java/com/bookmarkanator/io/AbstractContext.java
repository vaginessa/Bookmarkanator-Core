package com.bookmarkanator.io;

import com.bookmarkanator.bookmarks.AbstractBookmark;
import com.bookmarkanator.core.Settings;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The interface that other classes will access to perform operations (searching, sorting, adding, removing) of bookmarks.
 */
public abstract class AbstractContext implements Statistics
{
    private BKIOInterface bkioInterface;
    private int numberOfSearchResults;
    private boolean isDirty;
    private Settings settings;

    public void setBKIOInterface(BKIOInterface bkioInterface)
    {
        this.bkioInterface = bkioInterface;
    }

    public BKIOInterface getBKIOInterface()
    {
        return this.bkioInterface;
    }

    public abstract Set<UUID> getBookmarkIDs();

    public abstract Set<AbstractBookmark> getBookmarks();

    public abstract AbstractBookmark getBookmark(UUID uuid);

    public abstract List<AbstractBookmark> getBookmarks(Set<UUID> bookmarkIds);

    public abstract void addAll(List<AbstractBookmark> bookmarks)
        throws Exception;

    public abstract void addBookmark(AbstractBookmark bookmark)
            throws Exception;

    public abstract AbstractBookmark delete(UUID bookmarkID);

    public abstract List<AbstractBookmark> deleteAll(Set<UUID> bookmarks);

    public abstract void updateBookmark(AbstractBookmark bookmark)
        throws Exception;

    public Settings getSettings()
    {
        return this.settings;
    }

    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    public int getNumSearchResults()
    {
        return this.numberOfSearchResults;
    }

    /**
     * Sets the number of search results it is trying to obtain on each search. It could match this number early (for instance on bookmark
     * name search), or it might need to try searching all sorts of things to fill this quantity of results.
     * @param numSearchResults
     */
    public void setNumSearchResults(int numSearchResults)
    {
        this.numberOfSearchResults = numSearchResults;
    }

    /**
     * Searches bookmark names, tags, type names, and text words.
     * @param text  The text to search for.
     * @return
     */
    public abstract List<AbstractBookmark> searchAll(String text);

    public abstract List<AbstractBookmark> searchBookmarkNames(String text);

    public abstract List<AbstractBookmark> searchTagsLoosly(String text);

    public abstract List<AbstractBookmark> searchTagsExact(Set<String> tags);

    public abstract List<AbstractBookmark> searchTagsFullMatch(Set<String> tags)
        throws Exception;

    public abstract List<AbstractBookmark> searchBookmarkTypes(String text);

    public abstract List<AbstractBookmark> searchBookmarkText(String text);

    public boolean isDirty()
    {
        return isDirty;
    }

    public void setDirty()
    {
        this.isDirty = true;
    }

    public void setClean()
    {
        this.isDirty = false;
    }

    /**
     * Searches for and renames all oldTag's with newTag
     * @param newTag  The tag to replace oldTag with
     * @param oldTag  The tag that will be replaced
     */
    public abstract void renameTag(String newTag, String oldTag );

    /**
     * Replaces all tags in the tagsToMerge list with a single tag.
     * @param resultingTag  The single tag to replace the list of tags with.
     * @param tagsToMerge  The list of tags to replace with a single tag.
     */
    public abstract void mergeTags(String resultingTag, Set<String> tagsToMerge);

    /**
     * Deletes all matching tags.
     * @param tagsToDelete  A set of tags to delete.
     */
    public abstract void deleteTags(Set<String> tagsToDelete);

    public abstract Set<String> getAllTags();
    public abstract Set<String> getTagsFromBookmarks(Collection<AbstractBookmark> bookmarks);
    public abstract Set<String> getTypes(Set<AbstractBookmark> bookmarks);
    public abstract Set<String> getTypesClassNames(Set<AbstractBookmark> bookmarks);
}
