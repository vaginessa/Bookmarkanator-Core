package com.bookmarkanator.parsers;

import com.bookmarkanator.bookmarks.Bookmark;
import com.bookmarkanator.bookmarks.Bookmarks;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookmarkParser
{
    private enum Tags
    {
        root("root"),
        bookmarks("bookmarks"),
        bookmark("bookmark"),
        name("name"),
        description("description"),
        owner("bookmark-owner"),
        childbookmark("child-bookmark"),
        dates("dates"),
        unknown("_unknown_");

        private String tag;
        private static Map<String, Tags> tagsMap;

        static {
            tagsMap = new HashMap<>();
            for (Tags tag: Tags.values())
            {
                tagsMap.put(tag.tagText(), tag);
            }
        }

        public String tagText()
        {
            return this.tag;
        }

        public static Map<String, Tags> getTagsMap()
        {
            return tagsMap;
        }

        Tags(String tag)
        {
            this.tag = tag;
        }
    }

    private XMLStreamReader reader;
    private Stack<Tags> stateStack;
    private Stack<StringBuilder> charsStack;
    private Bookmarks bookmarks;
    private Bookmark currentBookmark;


    public Bookmarks parse(File file)
        throws Exception
    {
        Reader in = new FileReader(file);
        try
        {
            return parse(in);
        }
        finally
        {
            in.close();
        }
    }

    public Bookmarks parse(Reader in)
            throws Exception
    {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        reader = xif.createXMLStreamReader(in);
        stateStack = new Stack<>();
        charsStack = new Stack<>();
        stateStack.push(Tags.root);

        while (reader.hasNext())
        {
            int event = reader.next();

            switch (event)
            {
                case XMLStreamConstants.START_ELEMENT:
                    startTag(reader.getLocalName());
                    break;

                case XMLStreamConstants.CHARACTERS:
                    charsStack.peek().append(reader.getText());
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    endTag();
                    break;
            }
        }

        return bookmarks;
    }

    public void startTag(String currentTag) throws Exception {
        Tags prevTag = stateStack.peek();
        Tags state = Tags.unknown;
        String attr;

        switch (prevTag)
        {
            case root:
                state =match(state, Tags.bookmarks, currentTag);
                if (bookmarks==null)
                {
                    bookmarks = new Bookmarks();
                }
                else
                {
                    throw new Exception("Improperly nested bookmarks tags encountered");
                }
                break;
            case bookmarks:
                state =match(state, Tags.bookmark, currentTag);
                currentBookmark = new Bookmark();
                attr = ParserUtil.getStartElementAttribute(reader, "uuid");
                if (attr==null)
                {
                    throw new Exception("Bookmark uuid attribute missing");
                }
                currentBookmark.setTagUUID(UUID.fromString(attr));

                attr = ParserUtil.getStartElementAttribute(reader, "sharing");
                if (attr==null)
                {
                    throw new Exception("Bookmark sharing attribute missing");
                }
                currentBookmark.setSharing(Integer.parseInt(attr));

                attr = ParserUtil.getStartElementAttribute(reader, "tags");
                if (attr==null)
                {
                    throw new Exception("Bookmark tags attribute missing");
                }
                String[] t = attr.split(",");
                for (String s: t)
                {
                    currentBookmark.addTag(s);
                }

                break;
            case bookmark:
                state =match(state, Tags.name, currentTag);
                state =match(state, Tags.description, currentTag);
                state =match(state, Tags.owner, currentTag);
                state =match(state, Tags.childbookmark, currentTag);
                state =match(state, Tags.dates, currentTag);

                if (state==Tags.childbookmark)
                {
                    attr = ParserUtil.getStartElementAttribute(reader, "index");
                    if (attr==null)
                    {
                        throw new Exception("Child bookmark index attribute missing");
                    }
                    int a = Integer.parseInt(attr);
                    attr = ParserUtil.getStartElementAttribute(reader, "uuid");
                    if (attr==null)
                    {
                        throw new Exception("Child bookmark uuid attribute missing");
                    }
                    currentBookmark.addChildBookmark(UUID.fromString(attr), a);
                }
                else if (state==Tags.dates)
                {
                    attr = ParserUtil.getStartElementAttribute(reader, "created");
                    if (attr==null)
                    {
                        throw new Exception("Bookmark dates element created attribute missing");
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    currentBookmark.setCreatedDate(sdf.parse(attr));

                    attr = ParserUtil.getStartElementAttribute(reader, "last-accessed");
                    if (attr==null)
                    {
                        throw new Exception("Bookmark dates element last accessed attribute missing");
                    }

                    currentBookmark.setCreatedDate(sdf.parse(attr));
                }
                break;
        }

        if (state == Tags.unknown)
        {
            throw new RuntimeException("Unknown [" + prevTag + "][" + currentTag + "]");
        }

        stateStack.push(state);
        charsStack.push(new StringBuilder());
    }


    public void endTag()
    {
        Tags prevTag = stateStack.pop();
        String text = charsStack.pop().toString();

        switch (prevTag)
        {
            case name:
                currentBookmark.setName(text);
            break;
            case description:
                currentBookmark.setDescription(text);
                break;
            case owner:
                currentBookmark.setOwnerID(UUID.fromString(text));
            break;
            case bookmark:
                bookmarks.addBookmark(currentBookmark);
            break;


        }
    }

    public static Tags match(Tags unchangedState, Tags state, String tag)
    {
        Tags theTag = Tags.getTagsMap().get(tag);

        if (theTag!=null && theTag==state)
        {
            return state;
        }

        return unchangedState;
    }
}
