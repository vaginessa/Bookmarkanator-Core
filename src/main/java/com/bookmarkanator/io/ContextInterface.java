package com.bookmarkanator.io;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

/**
 * The interface that other classes will access to perform operations (searching, sorting, adding, removing) of bookmarks.
 */
public interface ContextInterface
{
    void setBKIOInterface(BKIOInterface bkioInterface);

    BKIOInterface getBKIOInterface();

    Set<UUID> getBookmarkIDs();

    Set<AbstractBookmark> getBookmarks();

    AbstractBookmark getBookmark(UUID uuid);

    List<AbstractBookmark> getBookmarks(Set<UUID> bookmarkIds);

    void addAll(List<AbstractBookmark> bookmarks)
        throws Exception;

    void addBookmark(AbstractBookmark bookmark)
            throws Exception;

    AbstractBookmark delete(UUID bookmarkID);

    List<AbstractBookmark> deleteAll(Set<UUID> bookmarks);

    void updateBookmark(AbstractBookmark bookmark)
        throws Exception;

    int getNumSearchResults();

    /**
     * Sets the number of search results it is trying to obtain on each search. It could match this number early (for instance on bookmark
     * name search), or it might need to try searching all sorts of things to fill this quantity of results.
     * @param numSearchResults
     */
    void setNumSearchResults(int numSearchResults);

    /**
     * Searches bookmark names, tags, type names, and text words.
     * @param text  The text to search for.
     * @return
     */
    List<AbstractBookmark> searchAll(String text);

    List<AbstractBookmark> searchBookmarkNames(String text);

    List<AbstractBookmark> searchTagsLoosly(String text);

    List<AbstractBookmark> searchTagsExact(Set<String> tags);

    List<AbstractBookmark> searchTagsFullMatch(Set<String> tags)
        throws Exception;

    List<AbstractBookmark> searchBookmarkTypes(String text);

    List<AbstractBookmark> searchBookmarkText(String text);

    boolean isDirty();

    //Will not set the dirty state while this is true
    void setAlwaysClean(boolean alwaysClean);
    /**
     * Creates a report listing the bookmarks, and possibly their tags as well.
     * @param includeTags  Should tags for each bookmark be included in the report?
     * @param limit  Limit the number of bookmarks included in the report.
     * @return
     */
    String bookmarksReport(boolean includeTags, int limit);

    /**
     * Searches for and renames all oldTag's with newTag
     * @param newTag  The tag to replace oldTag with
     * @param oldTag  The tag that will be replaced
     */
    void renameTag(String newTag, String oldTag );

    /**
     * Replaces all tags in the tagsToMerge list with a single tag.
     * @param resultingTag  The single tag to replace the list of tags with.
     * @param tagsToMerge  The list of tags to replace with a single tag.
     */
    void mergeTags(String resultingTag, Set<String> tagsToMerge);

    /**
     * Deletes all matching tags.
     * @param tagsToDelete  A set of tags to delete.
     */
    void deleteTags(Set<String> tagsToDelete);

    Set<String> getAllTags();
    Set<String> getTagsFromBookmarks(Collection<AbstractBookmark> bookmarks);
    Set<String> getTypes(Set<AbstractBookmark> bookmarks);
    Set<String> getTypesClassNames(Set<AbstractBookmark> bookmarks);
}
