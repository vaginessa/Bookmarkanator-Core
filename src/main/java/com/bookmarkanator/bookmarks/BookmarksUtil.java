package com.bookmarkanator.bookmarks;

import com.bookmarkanator.resourcetypes.BasicResource;

import java.util.*;

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
    public static List<Bookmark> getBookmarksWithTheseTagsOnly(List<Bookmark> bookmarks, Map<String, String> theseTagsOnly) {
        List<Bookmark> res = new ArrayList<>();

        for (Bookmark b : bookmarks) {//iterate through bookmarks
            for (String s : b.getTags().keySet()) {//iterate through this bookmarks tags
                if (theseTagsOnly.get(s) != null) {//add if it has a tag in the list
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
    public static Map<String, String> getTags(List<Bookmark> bookmarks) {
        Map<String, String> tags = new HashMap<>();

        for (Bookmark b : bookmarks) {
            for (String s : b.getTags().keySet()) {
                tags.put(s, s);
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

    public static List<String> getSuggestedTags(Map<String, String> tags, String text, int numberOfSuggestions)
    {
        Map<String, Set<String>> tagList = makeTagsList(tags);
        Set<String> res = tagList.get(text);
        List<String> li = new ArrayList<>();

        for (String s: res)
        {
            if (s.length()>=text.length())
            {
               li.add(s);
            }
        }

        return li;
    }

    /**
     * This method creates a map that contains tag search substrings for faster tag searching.
     * @param tags  The tags to use to create the tag substrings map.
     * @return  A map of tag substrings mapped to all the tags that they are contained in.
     */
    public static Map<String, Set<String>> makeTagsList(Map<String, String> tags)
    {
        Map<String, Set<String>> results = new HashMap<>();
        Set<String> allSubstrings = getAllSubStrings(tags);
        Set<String> tmpSet;

        for (String s2: allSubstrings)
        {//iterate through all substrings.
            tmpSet = new HashSet<>();
            results.put(s2, tmpSet);
            for (String s: allSubstrings)
            {//iterate over all tags
                if (s.contains(s2))
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
//    public static Set<String> getAllSubStrings(Map<String, String> tags)
//    {
//        Set<String> subStrings = new HashSet<>();
//
//        for (String s: tags.keySet())
//        {
//            for (int c=0;c<s.length();c++)
//            {
//                subStrings.add(s.substring(0,s.length()-c));
//                subStrings.add(s.substring(c,s.length()));
//                subStrings.add(""+s.charAt(c));
//            }
//        }
//        return subStrings;
//    }

    public static Set<String> getAllSubStrings(Map<String, String> tags)
    {
        Set<String> subStrings = new HashSet<>();

        for (String s: tags.keySet())
        {
            int a = s.length();
            System.out.println("--> "+s);
            while (a>1)
            {
               int pos = 0;
                System.out.println("A "+a);
                while (a+pos<s.length()+1)
                {
                    System.out.println(s.substring(pos,a)+" "+pos);
                    pos = pos+1;
                }
                a = a-1;
            }
//            for (int c=0;c<s.length();c++)
//            {
//                subStrings.add(s.substring(0,s.length()-c));
//                subStrings.add(s.substring(c,s.length()));
//                subStrings.add(""+s.charAt(c));
//            }
        }
        return subStrings;
    }

    //TODO Add a tags map creation function?
}
