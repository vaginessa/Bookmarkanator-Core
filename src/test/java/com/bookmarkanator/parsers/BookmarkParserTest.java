package com.bookmarkanator.parsers;

import com.bookmarkanator.bookmarks.Bookmark;
import com.bookmarkanator.bookmarks.Bookmarks;
import com.bookmarkanator.writers.Writer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

/**
 * Created by micah on 4/9/16.
 */
public class BookmarkParserTest {

    private File file;
    @Before
    public void setUp()
            throws Exception
    {
        file = new File("src/test/java/com/bookmarkanator/parsers/bookmark_parser_test.xml");
    }

    @After
    public void tearDown()
            throws Exception
    {
//        file.delete();
    }

    @Test
    public void testParseMethod()
            throws Exception
    {
        System.out.println("Enter testParseMethod in SystemResourceParserTest");
        Writer sw = new Writer();
        Bookmarks bookarks = generateTestBookmarks();
        sw.writeBookmarks(bookarks, file);

        System.out.println("Exit testParseMethod in SystemResourceParserTest");
    }

    private Bookmarks generateTestBookmarks()
    {
        Bookmarks bookmarks = new Bookmarks();

        Bookmark bookmark = new Bookmark();
        bookmark.setName("My awesome bookmark!");
        bookmark.setDescription("A bookmark about how awesome I am!!!");
        bookmark.setOwnerID(UUID.randomUUID());

        bookmarks.addBookmark(bookmark);

        bookmark = new Bookmark();
        bookmark.setName("Another bookmark.");
        bookmark.setDescription("A bookmark describing how awesome this other bookmark is.");
        bookmark.setOwnerID(UUID.randomUUID());
        bookmark.addTag("hello");
        bookmark.addTag("abc");
        bookmark.addTag("123");
        bookmark.addChildBookmark(UUID.randomUUID(), 8);
        bookmark.addChildBookmark(UUID.randomUUID(), 9);
        bookmark.addChildBookmark(UUID.randomUUID(), 13);

        bookmarks.addBookmark(bookmark);

        return bookmarks;
    }
}
