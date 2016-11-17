package com.bookmarkanator;

import com.bookmarkanator.core.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.util.*;
import org.junit.*;

public class TestBookmarksParsing
{
    @Test
    public void testParsingBookmarks()
        throws Exception
    {
        FileIO fileIO = new FileIO();
        fileIO.init("/Users/lloyd1/Projects/Bookmark-anator/src/main/resources/com.bookmarkanator.xml/BookmarksXMLTemplate.xml");
        ContextInterface fileContext = fileIO.getContext();
//        fileContext.getBookmark(UUID.fromString("94219EF5-6C6E-4A55-8DA7-5107ED80D0A9")).action(fileContext);
        fileIO.save();
        System.out.println();

        BookmarkFilter bf = new BookmarkFilter();

        bf.setBookmarks(null).excludeNamesWithText(null).results();
    }

}
