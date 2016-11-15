package com.bookmarkanator;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import org.junit.*;

public class TestBookmark
{
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

    private List<AbstractBookmark> getBookmarks()
    {
        List<AbstractBookmark> bks = new ArrayList<>();

        AbstractBookmark b = new TextBookmark(null);
        b.setName("Text Bookmark 1");
        b.setId(UUID.randomUUID());
        b.setText("This is text of Bookmark 1");
        b.addTag("Text");
        b.addTag("Bookmark");
        b.addTag("1");

        bks.add(b);

        b = new TextBookmark(null);
        b.setName("Text Bookmark 2");
        b.setId(UUID.randomUUID());
        b.setText("This is text of Bookmark 2");
        b.addTag("Text");
        b.addTag("Bookmark");
        b.addTag("2");

        bks.add(b);

        b = new TextBookmark(null);
        b.setName("Another Bookmark");
        b.setId(UUID.randomUUID());
        b.setText("I'm another bookmark!");
        b.addTag("Text");
        b.addTag("Bookmark");
        b.addTag("Another!");

        bks.add(b);

        b = new TextBookmark(null);
        b.setName("Bob");
        b.setId(UUID.randomUUID());
        b.setText("Is Awesome");
        b.addTag("Bookmark");
        b.addTag("Another!");

        bks.add(b);

        b = new WebBookmark(null);
        b.setName("A");
        b.setId(UUID.randomUUID());
        b.setText("B");
        b.addTag("1024");
        b.addTag("2055");

        bks.add(b);

        return bks;
    }
}
