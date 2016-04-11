package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.resourcetypes.*;

/**
 * Created by micah on 4/9/16.
 */
public class BookmarksUtil {

    /**
     * Accepts a list of Bookmarks, and tags, and returns a list of Bookmarks that have one or more of the supplied
     * tags in them. Essentially it removes all Bookmarks that don't contain any of the supplied tags.
     *
     * @param bookmarks     The list of Bookmarks to sort
     * @param theseTagsOnly The list of allowed tags a bookmark can have. If it has a single tag it is added to the return list.
     * @return A list of Bookmars that have one or more tags from the supplied tags list.
     */
    public static List<Bookmark> getBookmarksWithTheseTagsOnly(List<Bookmark> bookmarks, Set<String> theseTagsOnly) {
        List<Bookmark> res = new ArrayList<>();

        for (Bookmark b : bookmarks) {//iterate through bookmarks
            for (String s : b.getTags()) {//iterate through this bookmarks tags
                if (!theseTagsOnly.contains(s)) {//add if it has a tag in the list
                    res.add(b);
                }
            }
        }
        return res;
    }

    /**
     * This method gets a list of tags that are contained in the list of supplied Bookmarks.
     *
     * @param bookmarks The list of Bookmarks to extract the list of tags from.
     * @return A map of tags that the supplied bookmarks contained. Map<String, String> where both strings are the tag.
     */
    public static Set<String> getTags(List<Bookmark> bookmarks) {
        Set<String> tags = new HashSet<>();

        for (Bookmark b : bookmarks) {
            for (String s : b.getTags()) {
                tags.add(s);
            }
        }
        return tags;
    }

    public static List<Bookmark> getBookmarksByType(List<Bookmark> bookmarks, List<BasicResource> tagTypesToGet) {
        List<Bookmark> res = new ArrayList<>(bookmarks.size());

        for (Bookmark b : bookmarks) {
            for (BasicResource bb : tagTypesToGet) {
                if (b.getResource().getClass().getName().equals(bb.getClass().getName())) {
                    res.add(b);
                }
            }
        }
        return res;
    }


    public static void sortByCreatedDate(List<Bookmark> bookmarks, boolean ascending) {
        if (ascending) {
            Collections.sort(bookmarks, new Comparator<Bookmark>() {
                @Override
                public int compare(Bookmark o1, Bookmark o2) {
                    return o2.getCreatedDate().compareTo(o1.getCreatedDate());
                }
            });
        } else {
            Collections.sort(bookmarks, new Comparator<Bookmark>() {
                @Override
                public int compare(Bookmark o1, Bookmark o2) {
                    return o1.getCreatedDate().compareTo(o2.getCreatedDate());
                }
            });
        }
    }

    public static void sortByLastAccessed(List<Bookmark> bookmarks, boolean ascending) {
        if (ascending) {
            Collections.sort(bookmarks, new Comparator<Bookmark>() {
                @Override
                public int compare(Bookmark o1, Bookmark o2) {
                    return o2.getLastAccessedDate().compareTo(o1.getLastAccessedDate());
                }
            });
        } else {
            Collections.sort(bookmarks, new Comparator<Bookmark>() {
                @Override
                public int compare(Bookmark o1, Bookmark o2) {
                    return o1.getLastAccessedDate().compareTo(o2.getLastAccessedDate());
                }
            });
        }
    }


    /**
     * This method is used to get search suggestions for the supplied map of tags and text.
     * @param tags  A map of available tags.
     * @param text  The text to search for.
     * @return  A list of tag strings that contain the supplied text within them.
     */
    public static List<String> getSuggestedTags(Set<String> tags, String text, int preferedNumberOfResults)
    {
        Map<String, Set<String[]>> tagList = makeTagsList(tags);
        Set<String[]> res = tagList.get(text);
        Set<String[]> resSet = new LinkedHashSet<>();
        Set<String> returnResult = new LinkedHashSet<>();

        if (res!=null)
        {
            for (String[] s: res)
            {//if there is a direct substring match, remove all results that are shorter than the supplied string
                if (s[0].length()>=text.length())
                {
                    returnResult.add(s[1]);//creates a list of unique tag entries that were found.
                }
            }
        }

        if (res==null || returnResult.size()<preferedNumberOfResults)
        {
            resSet = getMatchesForTextSubstring(tagList,text);

            for (String[] st: resSet)
            {
                returnResult.add(st[1]);
            }
        }

        //        sortByLength(li);
        return new ArrayList<>(returnResult);
    }

    /**
     * Used in case there are too few matches found by using the entire tag string.
     * <p>
     *     This method breaks the tag string up and searches for each substring in the list of tag substrings.
     * </p>
     */
    private static Set<String[]> getMatchesForTextSubstring(Map<String, Set<String[]>> tagList, String text)
    {
        Set<String[]> res = new HashSet<>();
        //TODO implement method.

        return res;
    }

    private static void sortByLength(List<String[]> li)
    {
        Collections.sort(li, new Comparator<String[]>()
        {
            @Override
            public int compare(String[] o1, String[] o2)
            {
                return o1[0].length()-o2[0].length();
            }
        });
    }

    /**
     * This method creates a map that contains tag search substrings for faster tag searching.
     * @param tags  The tags to use to create the tag substrings map.
     * @return  A map of tag substrings mapped to all the tags that they are contained in.
     */
    public static Map<String, Set<String[]>> makeTagsList(Set<String> tags)
    {
        Map<String, Set<String[]>> results = new HashMap<>();
        Set<String[]> allSubstrings = getAllSubStrings(tags);
        Set<String[]> tmpSet;

        for (String[] s2: allSubstrings)
        {//iterate through all substrings.
            tmpSet = new HashSet<>();
            results.put(s2[0], tmpSet);
            for (String[] s: allSubstrings)
            {//iterate over all tags
                if (s[0].contains(s2[0]))
                {//if the tag contains the the substring we are at then add it to the map
                    tmpSet.add(s);
                }
            }
        }

        return results;
    }

    /**
     * Accepts a map of tags, and returns a set of all tags, and all substrings of those tags.
     * @param tags the tags to extract the sub-strings from.
     * @return A set of all tags, and all sub-strings of those tags.
     */
    public static Set<String[]> getAllSubStrings(Set<String> tags)
    {
        Set<String[]> subStrings = new HashSet<>();
        String[] tmpS;

        for (String s: tags)
        {
            int a = 1;
//            System.out.println("--> "+s);
            while (a<s.length())
            {
               int pos = 0;
//                System.out.println("length "+a);
                while (a+pos<=s.length())
                {
//                    System.out.println("Result "+s.substring(pos,pos+a)+" position "+pos);
                    tmpS = new String[]{s.substring(pos,pos+a), s};
                    subStrings.add(tmpS);
                    pos = pos+1;
                }
                a = a+1;
            }
            tmpS= new String[]{s, s};
            subStrings.add(tmpS);
        }
        return subStrings;
    }

    //TODO Add a tags map creation function?
}
