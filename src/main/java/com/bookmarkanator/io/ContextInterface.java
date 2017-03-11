package com.bookmarkanator.io;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

/**
 * The interface that other classes will access to perform operations (searching, sorting, adding, removing) of bookmarks.
 */
public interface ContextInterface
{

    //TODO convert to abstract class?
    void setBKIOInterface(BKIOInterface bkioInterface);

    BKIOInterface getBKIOInterface();

    /**
     * Gets a set of bookmark Id's of the bookmarks that depend on this bookmark in some way. Basically asking the question what do I depend on?
     * *Note: Individual bookmarks are responsible for setting their dependents.
     *
     * @param bookmarkId The bookmark to check for dependents.
     * @return A set of dependent bookmark Id's
     */
    Set<UUID> getDependents(UUID bookmarkId);

    /**
     * Gets a set of bookmark Id's that this bookmark depends on. Basically asking the question what depends on me?
     * *Note: Individual bookmarks are responsible for setting thei dependents. When dependents are set this depends on list is automatically updated.
     *
     * @param bookmarkId
     * @return
     */
    Set<UUID> getDependsOn(UUID bookmarkId);

    int addDependency(UUID theBookmark, UUID dependingBookmark);

    int removeDependency(UUID theBookmark, UUID dependingBookmark);


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

    void setNumSearchResults(int numSearchResults);

    List<AbstractBookmark> searchAll(String text);

    List<AbstractBookmark> searchBookmarkNames(String text);

    List<AbstractBookmark> searchTagsLoosly(String text);

    List<AbstractBookmark> searchTagsExact(Set<String> tags);

    List<AbstractBookmark> searchTagsFullMatch(Set<String> tags)
        throws Exception;

    List<AbstractBookmark> searchBookmarkTypes(String text);

    List<AbstractBookmark> searchBookmarkText(String text);

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

    /**
     * Running the bookmark edit command through this interface so it can propagate the edit, and notify listeners.
     * @param abstractBookmark  The abstractBookmark to call the edit on.
     */
    //TODO Move this method the the bookmark? That way it gets called for sure when there is an action.
    void bkAction(AbstractBookmark abstractBookmark)
        throws Exception;

    Set<String> getTags(Set<AbstractBookmark> bookmarks);
    Set<String> getTypes(Set<AbstractBookmark> bookmarks);
}
