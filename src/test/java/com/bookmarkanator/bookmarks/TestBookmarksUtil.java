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
        System.out.println("Length "+BookmarksUtil.getAllSubStrings(getTags()).size());


        BookmarksUtil.getAllSubStrings(getTags()).forEach(System.out::println);


//        Map<String, Set<String>> tagsList =  BookmarksUtil.makeTagsList(getTags());
//
//        List<String> res = BookmarksUtil.getSuggestedTags(getTags(), "1", 0);
//        List<String> res2 = BookmarksUtil.getSuggestedTags(getTags(), "123", 0);
//        List<String> res3 = BookmarksUtil.getSuggestedTags(getTags(), "56618", 0);



    }

    public Map<String, String> getTags()
    {
        Map<String, String> tags = new HashMap<>();

        tags.put("ABC","ABC" );
        tags.put("123","123" );
        tags.put("DEF","DEF" );
        tags.put("456","456" );
        tags.put("GHI","GHI" );
        tags.put("789","789" );
        tags.put("JKL","JKL" );
        tags.put("MNO","MNO" );
        tags.put("PQR","PQR" );
        tags.put("STU","STU" );
        tags.put("DEF","DEF" );
        tags.put("456","456" );
        tags.put("GHI","GHI" );
        tags.put("789","789" );
        tags.put("JKL","JKL" );
        tags.put("MNO","MNO" );
        tags.put("PQR","PQR" );
        tags.put("STU","STU" );
        tags.put("DEF","DEF" );
        tags.put("456","456" );
        tags.put("GHI","GHI" );
        tags.put("789","789" );
        tags.put("JKL","JKL" );
        tags.put("MNO","MNO" );
        tags.put("PQR","PQR" );
        tags.put("STU","STU" );


        return tags;
    }
}
