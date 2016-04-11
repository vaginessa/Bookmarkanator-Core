package com.bookmarkanator.bookmarks;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by micah on 4/10/16.
 */
public class TestBookmarksUtil {

    @Test
    public void testTagSuggestions()
    {



        BookmarksUtil.getAllSubStrings(getTags()).forEach(System.out::println);


//        Map<String, Set<String>> tagsList =  BookmarksUtil.makeTagsList(getTags());
//
//        List<String> res = BookmarksUtil.getSuggestedTags(getTags(), "1", 0);
//        List<String> res2 = BookmarksUtil.getSuggestedTags(getTags(), "123", 0);
//        List<String> res3 = BookmarksUtil.getSuggestedTags(getTags(), "56618", 0);


        System.out.println("hb".substring(1,2)+" "+"hb".length());
    }

    public Map<String, String> getTags()
    {
        Map<String, String> tags = new HashMap<>();

        tags.put("ABC","ABC" );
        tags.put("123654566184894619849","123654566184894619849" );
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
