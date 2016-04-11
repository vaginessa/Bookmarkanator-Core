package com.bookmarkanator.bookmarks;

import java.util.*;
import org.junit.*;

/**
 * Created by micah on 4/10/16.
 */
public class TestBookmarksUtil {

    @Test
    public void testTagSuggestions()
    {
//        System.out.println("Length "+BookmarksUtil.getAllSubStrings(getTags()).size());


        BookmarksUtil.getAllSubStrings(getTags()).forEach(System.out::println);


//        Map<String, Set<String>> tagsList =  BookmarksUtil.makeTagsList(getTags());
//
        List<String> res = BookmarksUtil.getSuggestedTags(getTags(), "1", 10);
        List<String> res2 = BookmarksUtil.getSuggestedTags(getTags(), "123", 10);
        List<String> res3 = BookmarksUtil.getSuggestedTags(getTags(), "56618", 10);

        System.out.println();

    }

    public Set<String> getTags()
    {
        Set<String> tags = new HashSet<>();

        tags.add("ABC");
        tags.add("123");
        tags.add("DEF");
        tags.add("456");
        tags.add("GHI");
        tags.add("789");
        tags.add("JKL");
        tags.add("MNO");
        tags.add("PQR");
        tags.add("STU");
        tags.add("DEF");
        tags.add("456");
        tags.add("GHI");
        tags.add("789");
        tags.add("JKL");
        tags.add("MNO");
        tags.add("PQR");
        tags.add("STU");
        tags.add("DEF");
        tags.add("456");
        tags.add("GHI");
        tags.add("789");
        tags.add("JKL");
        tags.add("MNO");
        tags.add("PQR");
        tags.add("STU");
        tags.add("123ABC");

        return tags;
    }
}
