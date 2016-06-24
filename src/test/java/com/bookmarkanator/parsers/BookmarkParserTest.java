package com.bookmarkanator.parsers;

import java.io.*;
import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.resourcetypes.*;
import com.bookmarkanator.writers.Writer;
import org.junit.*;

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

        BookmarkParser p = new BookmarkParser();

        Bookmarks parsed = p.parse(file);

        Assert.assertTrue(parsed.equals(bookarks));

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
        bookmark.setLastAccessedDate(new Date(System.currentTimeMillis()+1000000));
        bookmark.addTag("hello");
        bookmark.addTag("abc");
        bookmark.addTag("123");

        Bookmark web = new Bookmark();
        web.setName("yahoo.com");
        DefaultSystemResource dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("yahoo.com");
        dsr.setText("http://www.yahoo.com");
        web.setResource(dsr);
        web.addTag("yahoo");
        web.addTag("internet");
        web.addTag("web");
        web.addTag("social");

        Bookmark web1 = new Bookmark();
        web1.setName("google.com");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("google.com");
        dsr.setText("http://www.google.com");
        web1.setResource(dsr);
        web1.addTag("web");
        web1.addTag("search");
        web1.addTag("google");
        web1.addTag("internet");

        Bookmark web2 = new Bookmark();
        web2.setName("msn");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("msn");
        dsr.setText("http://www.msn.com");
        web2.setResource(dsr);
        web2.addTag("web");
        web2.addTag("msn");
        web2.addTag("social");
        web2.addTag("internet");

        Bookmark web3 = new Bookmark();
        web3.setName("acronymfinder");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("acronymfinder");
        dsr.setText("http://www.acronymfinder.com");
        web3.setResource(dsr);
        web3.addTag("web");
        web3.addTag("acronym");
        web3.addTag("search");

        Bookmark terminal = new Bookmark();
        terminal.setName("pwd");
        TerminalResource tr = new TerminalResource(TerminalResource.OPEN_TERMINAL_ONLY);
        tr.setText("pwd");
        terminal.setResource(tr);
        terminal.addTag("print");
        terminal.addTag("directory");
        terminal.addTag("terminal");
        terminal.addTag("prompt");
        terminal.addTag("run");

        Bookmark terminal2 = new Bookmark();
        terminal2.setName("change java");
        tr = new TerminalResource(TerminalResource.OPEN_TERMINAL_ONLY);
        tr.setText("sudo update-alternatives --config java");
        terminal2.setResource(tr);
        terminal2.addTag("java");
        terminal2.addTag("change");
        terminal2.addTag("version");
        terminal2.addTag("Java 8");

        Bookmark fileOpen = new Bookmark();
        fileOpen.setName("open home");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
        dsr.setText("/home");
        fileOpen.setResource(dsr);
        fileOpen.addTag("file");
        fileOpen.addTag("open");
        fileOpen.addTag("sys home");

        bookmarks.addBookmark(web);
        bookmarks.addBookmark(web1);
        bookmarks.addBookmark(web2);
        bookmarks.addBookmark(web3);
        bookmarks.addBookmark(terminal);
        bookmarks.addBookmark(terminal2);
        bookmarks.addBookmark(fileOpen);

        for (int c = 0; c < 100; c++)
        {
            fileOpen = new Bookmark();
            fileOpen.setName("open home " + c);
            dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
            dsr.setText("/home");
            //            fileOpen.addObserver(this);
            fileOpen.setResource(dsr);
            fileOpen.addTag("file");
            fileOpen.addTag("open");
            fileOpen.addTag("sys home");
            bookmarks.addBookmark(fileOpen);
        }

        Bookmark gitignore = new Bookmark();
        gitignore.setName("gitignore");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR);
        dsr.setName("gitignore");
        dsr.setText("/users/lloyd1/.gitignore_global");
        gitignore.setResource(dsr);
        gitignore.addTag("Bob");

        bookmarks.addBookmark(gitignore);


        bookmarks.addBookmark(bookmark);

        return bookmarks;
    }
}
