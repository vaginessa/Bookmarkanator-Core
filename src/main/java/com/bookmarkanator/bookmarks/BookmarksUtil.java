package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.resourcetypes.*;

public class BookmarksUtil
{

    /**
     * Accepts a list of Bookmarks, and tags, and returns a list of Bookmarks that have one or more of the supplied
     * tags in them. Essentially it removes all Bookmarks that don't contain any of the supplied tags.
     *
     * @param bookmarks     The list of Bookmarks to sort
     * @param theseTagsOnly The list of allowed tags a bookmark can have. If it has a single tag it is added to the return list.
     * @return A list of Bookmars that have one or more tags from the supplied tags list.
     */
    public static List<Bookmark> getBookmarksWithTheseTagsOnly(List<Bookmark> bookmarks, Set<String> theseTagsOnly)
    {
        List<Bookmark> res = new ArrayList<>();

        for (Bookmark b : bookmarks)
        {//iterate through bookmarks
            if (intersect(b.getTags(), theseTagsOnly))
            {
                res.add(b);
            }
        }
        return res;
    }

    public static List<Bookmark> getBookmarksWithAllOfTheseTagsOnly(Set<Bookmark> bookmarks, Set<String> hasAllTheseTags)
    {
        List<Bookmark> res = new ArrayList<>();

        for (Bookmark b : bookmarks)
        {//iterate through bookmarks
            if (contains(b.getTags(), hasAllTheseTags))
            {
                res.add(b);
            }
        }
        return res;
    }

    public static boolean contains(Set<String> setTocheck, Set<String> mustHaveAllThese)
    {

        for (String s : mustHaveAllThese)
        {//iterate over all must have tags.
            if (!setTocheck.contains(s))
            {//return immediately when item not found in setToCheck
                return false;
            }
        }
        return true;
    }

    /**
     * Checks that there is at least one matching item between the two sets.
     *
     * @param a First set to check
     * @param b Second set to check
     * @return Returns true if there is at lease one matching item between the two sets.
     */
    public static boolean intersect(Set<String> a, Set<String> b)
    {
        for (String s : a)
        {
            if (b.contains(s))
                return true;
        }
        return false;
    }

    /**
     * This method gets a list of tags that are contained in the list of supplied Bookmarks.
     *
     * @param bookmarks The list of Bookmarks to extract the list of tags from.
     * @return A map of tags that the supplied bookmarks contained. Map<String, String> where both strings are the tag.
     */
    public static Set<String> getTags(List<Bookmark> bookmarks)
    {
        Set<String> tags = new HashSet<>();

        for (Bookmark b : bookmarks)
        {
            for (String s : b.getTags())
            {
                tags.add(s);
            }
        }
        return tags;
    }

    public static Set<String> getTags(Map<String, Bookmark> bookmarks)
    {
        Set<String> tags = new HashSet<>();

        for (String s: bookmarks.keySet())
        {
            Bookmark b = bookmarks.get(s);

            if (b!=null)
            {
                tags.addAll(b.getTags());
            }
        }
        return tags;
    }

    public static List<String> getSortedList(Set<String> items)
    {
        List<String> l = new ArrayList<>(items);
        Collections.sort(l);
        return l;
    }

    public static List<Bookmark> getBookmarksByType(List<Bookmark> bookmarks, Set<BasicResource> tagTypesToGet)
    {
        List<Bookmark> res = new ArrayList<>(bookmarks.size());

        for (Bookmark b : bookmarks)
        {
            for (BasicResource bb : tagTypesToGet)
            {
                if (b.getResource().getClass().getName().equals(bb.getClass().getName()))
                {
                    res.add(b);
                }
            }
        }
        return res;
    }

    public static Set<String> getBookmarkResourceTypeStrings(List<Bookmark> bookmarks)
    {
        Set<String> results = new HashSet<>();
        for (Bookmark b: bookmarks)
        {
            if (b.getResource()!=null)
            {

                if (b.getResource() instanceof DefaultSystemResource)
                {
                    DefaultSystemResource df = (DefaultSystemResource)b.getResource();
                    results.add(df.getClass().getName()+"."+df.getTypeString());
                    System.out.println("Class name "+df.getClass().getName()+"."+df.getTypeString());
                }
                else
                {
                    results.add(b.getResource().getClass().getName());
                    System.out.println("Class name "+b.getResource().getClass().getName());
                }

            }
        }

        return results;
    }


    public static void sortByCreatedDate(List<Bookmark> bookmarks, boolean ascending)
    {
        if (ascending)
        {
            Collections.sort(bookmarks, new Comparator<Bookmark>()
            {
                @Override
                public int compare(Bookmark o1, Bookmark o2)
                {
                    return o2.getCreatedDate().compareTo(o1.getCreatedDate());
                }
            });
        }
        else
        {
            Collections.sort(bookmarks, new Comparator<Bookmark>()
            {
                @Override
                public int compare(Bookmark o1, Bookmark o2)
                {
                    return o1.getCreatedDate().compareTo(o2.getCreatedDate());
                }
            });
        }
    }

    public static void sortByLastAccessed(List<Bookmark> bookmarks, boolean ascending)
    {
        if (ascending)
        {
            Collections.sort(bookmarks, new Comparator<Bookmark>()
            {
                @Override
                public int compare(Bookmark o1, Bookmark o2)
                {
                    return o2.getLastAccessedDate().compareTo(o1.getLastAccessedDate());
                }
            });
        }
        else
        {
            Collections.sort(bookmarks, new Comparator<Bookmark>()
            {
                @Override
                public int compare(Bookmark o1, Bookmark o2)
                {
                    return o1.getLastAccessedDate().compareTo(o2.getLastAccessedDate());
                }
            });
        }
    }

    /**
     * This method is used to get search suggestions for the supplied map of tags and text.
     *
     * @param tags                    a set of tags to search through.
     * @param text                    The text to search for.
     * @param preferedNumberOfResults The preferred number of results. It will try to get this number if possible
     * @return A list of tag strings that contain the supplied text within them.
     */
    public static List<String> getSuggestedTags(Set<String> tags, String text, int preferedNumberOfResults)
    {
        Map<String, Set<SubStringResults>> tagList = makeTagsList(tags);
        return getSuggestedTags(tagList, text, preferedNumberOfResults);
    }

    /**
     * This method is used to get search suggestions for the supplied map of tags and text.
     *
     * @param tagList                 The list of tags to search.
     * @param text                    The text to search for.
     * @param preferedNumberOfResults The preferred number of results. It will try to get this number if possible
     * @return A list of tag strings that contain the supplied text within them.
     */
    public static List<String> getSuggestedTags(Map<String, Set<SubStringResults>> tagList, String text, int preferedNumberOfResults)
    {
        //TODO Implement a more accurate tag search algorithm.

        Set<SubStringResults> res = tagList.get(text);
        if (res == null)
        {
            res = tagList.get(text.toUpperCase());
        }
        if (res == null)
        {
            res = tagList.get(text.toLowerCase());
        }

        List<String> resSet;
        Set<String> returnResult = new HashSet<>();

        if (res != null)
        {
            for (SubStringResults s : res)
            {//if there is a direct substring match, remove all results that are shorter than the supplied string
                returnResult.add(s.getText());//adding the actual tag found instead of the string used to find it.
            }
        }

        List<String> li = new ArrayList<>(returnResult);

        returnResult.clear();

        sortByLength(li, true);

        if (res == null || returnResult.size() < preferedNumberOfResults)
        {
            resSet = getMatchesForTextSubstring(tagList, text);

            for (String st : resSet)
            {
                returnResult.add(st);
            }
        }

        for (String s : returnResult)
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
     * This method breaks the tag string up and searches for each substring in the list of tag substrings.
     * </p>
     */
    private static List<String> getMatchesForTextSubstring(Map<String, Set<SubStringResults>> tagList, String text)
    {
        //TODO modify this method to produce more accurate search results.
        //TODO modify it so that it sorts by found tag first, and then replaces found tags by the real tags.
        Set<SubStringResults> res;
        Set<SubStringResults> substrings = getAllSubStrings(text);
        Set<String> tags = new HashSet<>();

        for (SubStringResults str : substrings)
        {//iterate through all substrings found for the supplied text
            res = tagList.get(str.getText());

            if (res != null)
            {
                for (SubStringResults s : res)
                {//add tags found
                    tags.add(s.getText());
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
     *
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

        for (String s : tmpStr)
        {
            if (s.length() == l)
            {
                equalList.add(s);
            }
            else if (s.length() > l)
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
                    return o2.length() - o1.length();
                }
            });
        }
        else
        {
            Collections.sort(li, new Comparator<String>()
            {
                @Override
                public int compare(String o1, String o2)
                {
                    return o1.length() - o2.length();
                }
            });
        }
    }

    /**
     * This method creates a map that contains tag search sub strings for faster tag searching.
     *
     * @param tags The tags to use to create the tag sub strings map.
     * @return A map of tag sub strings mapped to all the tags that they are contained in.
     */
    public static Map<String, Set<SubStringResults>> makeTagsList(Set<String> tags)
    {
        Map<String, Set<SubStringResults>> results = new HashMap<>();
        Set<SubStringResults> allSubstrings = getAllSubStrings(tags);

        for (SubStringResults s2 : allSubstrings)
        {//iterate through all substrings.
            Set<SubStringResults> tmp = results.get(s2.getText());

            //add an entry for this substring result object if needed.
            if (tmp==null)
            {//add an entry if one doesn't exist.
                tmp = new HashSet<>();
                tmp.add(s2);
                results.put(s2.getText(), tmp);
            }
            else
            {
                tmp.add(s2);
            }

            for (String t: s2.getTags())
            {//for each substring add or append to and entry.
                tmp = results.get(t);

                if (tmp==null)
                {//add an entry if one doesn't exist.
                    tmp = new HashSet<>();
                    tmp.add(s2);
                    results.put(t, tmp);
                }
                else
                {
                    tmp.add(s2);
                }
            }
        }

        return results;
    }

    /**
     * Accepts a set of tags and creates a set of SubStringResult objects that represent that tag and
     * all substrings it contains.
     *
     * @param tags  Set of tags to create substrings from.
     * @return  Set<SubStringResult> Each SubStringResult object contains a tag, and all substrings that are
     * contained within that tag.
     */
    public static Set<SubStringResults> getAllSubStrings(Set<String> tags)
    {
        Set<SubStringResults> subStringResults = new HashSet<>();

        for (String s : tags)
        {
            int a = 1;
            SubStringResults ssr = new SubStringResults();
            ssr.setText(s);
            while (a < s.length())
            {
                int pos = 0;
                while (a + pos <= s.length())
                {
                    ssr.addSubstring(s.substring(pos, pos + a));
                    subStringResults.add(ssr);
                    pos = pos + 1;
                }
                a = a + 1;
            }
        }
        return subStringResults;
    }

    public static Set<SubStringResults> getAllSubStrings(String text)
    {
        Set<String> set = new HashSet<>();
        set.add(text);
        return getAllSubStrings(set);
    }

    //TODO Add a tags map creation function?


}
