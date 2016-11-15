package com.bookmarkanator;

import com.bookmarkanator.core.*;
import com.bookmarkanator.io.*;
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
        fileIO.save();
        System.out.println();
    }

}
