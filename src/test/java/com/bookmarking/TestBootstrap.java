package com.bookmarking;

import java.util.*;
import com.bookmarking.bookmark.*;
import org.junit.*;

public class TestBootstrap
{
    @Test
    public void testBootstrap()
        throws Exception
    {
        Bootstrap bootstrap = new Bootstrap();
        Set<AbstractBookmark> bookmarks = bootstrap.getIOInterface().getAllBookmarks();
        System.out.println(bookmarks.size());

        TestBookmark testBookmark = new TestBookmark();
        testBookmark.setContent("This is a test bookmark!");
        bootstrap.getIOInterface().addBookmark(testBookmark);

        bookmarks = bootstrap.getIOInterface().getAllBookmarks();
        System.out.println(bookmarks.size());

        bootstrap.getIOInterface().save();
        bootstrap.getIOInterface().close();

        bootstrap = new Bootstrap();
        bookmarks = bootstrap.getIOInterface().getAllBookmarks();

        System.out.println(bookmarks.size());
    }
}
