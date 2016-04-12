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
        List<String> resSet;
        Set<String> returnResult = new HashSet<>();

        if (res!=null)
        {
            for (String[] s: res)
            {//if there is a direct substring match, remove all results that are shorter than the supplied string
                returnResult.add(s[1]);//adding the actual tag found instead of the string used to find it.
            }
        }

        List<String> li = new ArrayList<>(returnResult);

        returnResult.clear();

        sortByLength(li, true);

        if (res==null || returnResult.size()<preferedNumberOfResults)
        {
            resSet = getMatchesForTextSubstring(tagList,text);

            for (String st: resSet)
            {
                returnResult.add(st);
            }
        }

        for (String s: returnResult)
        {
            if (!li.contains(s))
            {//don't add any duplicates to the end of the list
                li.add(s);
            }
        }

        return li;
    }

    /**
     * Used in case there are too few matches found by using the entire tag string.
     * <p>
     *     This method breaks the tag string up and searches for each substring in the list of tag substrings.
     * </p>
     */
    private static List<String> getMatchesForTextSubstring(Map<String, Set<String[]>> tagList, String text)
    {
        //TODO modify this method to produce more accurate search results.
        //TODO modify it so that it sorts by found tag first, and then replaces found tags by the real tags.
        Set<String[]> res;
        Set<String[]> substrings = getAllSubStrings(text);
        Set<String> tags = new HashSet<>();

        for (String[] str: substrings)
        {//iterate through all substrings found for the supplied text
            res = tagList.get(str[0]);

            if (res!=null)
            {
                for (String[] s: res)
                {//add tags found
                    tags.add(s[1]);
                }
            }
        }

        List<String> tagsFound = new ArrayList<>();

        tagsFound.addAll(tags);

        sortByLength(tagsFound, text);

        return tagsFound;
    }

    /**
     * Sorts the supplied list ordering the items closest to the length of the supplied text first, followed by all
     * larger, and then all shorter.
     * @param li
     * @param text
     */
    private static void sortByLength(List<String> li, String text)
    {
        List<String> tmpStr = new ArrayList<>(li);//make a copy to work with
        List<String> equalList = new ArrayList<>();
        List<String> largerList = new ArrayList<>();
        List<String> smallerList = new ArrayList<>();

        li.clear();

        int l = text.length();

        for (String s: tmpStr)
        {
            if (s.length()==l)
            {
                equalList.add(s);
            }
            else if (s.length()>l)
            {
                largerList.add(s);
            }
            else
            {
                smallerList.add(s);
            }
        }

        sortByLength(largerList, true);
        sortByLength(smallerList, true);
        li.addAll(equalList);
        li.addAll(largerList);
        li.addAll(smallerList);
    }

    private static void sortByLength(List<String> li, boolean ascending)
    {
        if (!ascending)
        {
            Collections.sort(li, new Comparator<String>()
            {
                @Override
                public int compare(String o1, String o2)
                {
                    return o2.length()-o1.length();
                }
            });
        }
        else {
            Collections.sort(li, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.length() - o2.length();
                }
            });
        }
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

    public static Set<String[]> getAllSubStrings(String text)
    {
        Set<String> set = new HashSet<>();
        set.add(text);
        return getAllSubStrings(set);
    }

    //TODO Add a tags map creation function?
}
