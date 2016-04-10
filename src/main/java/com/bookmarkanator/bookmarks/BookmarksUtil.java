package com.bookmarkanator.bookmarks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by micah on 4/9/16.
 */
public class BookmarksUtil {

    /**
     * Accepts a list of Bookmarks, and tags, and returns a list of Bookmarks that have one or more of the supplied
     * tags in them. Essentially it removes all Bookmarks that don't contain any of the supplied tags.
     * @param bookmarks  The list of Bookmarks to sort
     * @param theseTagsOnly  The list of allowed tags a bookmark can have. If it has a single tag it is added to the return list.
     * @return  A list of Bookmars that have one or more tags from the supplied tags list.
     */
    public static List<Bookmark> getBookmarksWithTheseTagsOnly(List<Bookmark> bookmarks, Map<String, String> theseTagsOnly)
    {
        List<Bookmark> res = new ArrayList<>();

        for (Bookmark b: bookmarks)
        {//iterate through bookmarks
            for (String s: b.getTags().keySet())
            {//iterate through this bookmarks tags
                if (theseTagsOnly.get(s)!=null)
                {//add if it has a tag in the list
                    res.add(b);
                }
            }
        }
        return res;
    }

    /**
     * This method gets a list of tags that are contained in the list of supplied Bookmarks.
     * @param bookmarks  The list of Bookmarks to extract the list of tags from.
     * @return  A map of tags that the supplied bookmarks contained. Map<String, String> where both strings are the tag.
     */
    public static Map<String, String> getTags(List<Bookmark> bookmarks)
    {
        Map<String, String> tags = new HashMap<>();

        for (Bookmark b: bookmarks)
        {
            for (String s: b.getTags().keySet())
            {
                tags.put(s,s);
            }
        }
        return tags;
    }

    //TODO add sort by created date function.

    //TODO Add a sort by last accessed date function.

    //TODO Add a tag search suggestions method (supply a list of tags, and a string, and it will return a list of suggested items.

    //TODO Add a filter bookmarks by resource type.

    //TODO Add a tags map creation function?
}
