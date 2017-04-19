package com.bookmarkanator;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;
import org.junit.*;

public class TestBookmark
{
    @Ignore
    @Test
    public void testBookmarkSearch()
        throws Exception
    {
        FileContext context = new FileContext();

        context.addAll(getBookmarks());

        List<AbstractBookmark> results = context.searchAll("Text");

        results = context.searchAll("2055");

        results = context.searchAll("1");

        System.out.println();
    }

    private List<AbstractBookmark> getBookmarks() throws Exception {
        List<AbstractBookmark> bks = new ArrayList<>();

        AbstractBookmark b = new TextBookmark();
        b.setName("Text Bookmark 1");
        b.setId(UUID.randomUUID());
        b.setData("This is text of Bookmark 1");
        b.addTag("Text");
        b.addTag("Bookmark");
        b.addTag("1");

        bks.add(b);

        b = new TextBookmark();
        b.setName("Text Bookmark 2");
        b.setId(UUID.randomUUID());
        b.setData("This is text of Bookmark 2");
        b.addTag("Text");
        b.addTag("Bookmark");
        b.addTag("2");

        bks.add(b);

        b = new TextBookmark();
        b.setName("Another Bookmark");
        b.setId(UUID.randomUUID());
        b.setData("I'm another bookmark!");
        b.addTag("Text");
        b.addTag("Bookmark");
        b.addTag("Another!");

        bks.add(b);

        b = new TextBookmark();
        b.setName("Bob");
        b.setId(UUID.randomUUID());
        b.setData("Is Awesome");
        b.addTag("Bookmark");
        b.addTag("Another!");

        bks.add(b);

        b = new WebBookmark();
        b.setName("A");
        b.setId(UUID.randomUUID());
        b.setData("B");
        b.addTag("1024");
        b.addTag("2055");

        bks.add(b);

        return bks;
    }
}
