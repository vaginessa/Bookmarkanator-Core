package com.bookmarkanator.xml;

import java.io.*;
import javax.xml.parsers.*;
import com.bookmarkanator.core.*;
import org.w3c.dom.*;

public class BookmarksXMLParser
{
    //Tags
    public static final String BOOKMARKS_TAG = "bookmarks";
    public static final String BLOCK_TAG = "block";
    public static final String BOOKMARK_TAG = "bookmark";
    public static final String NAME_TAG = "name";
    public static final String ID_TAG = "id";
    public static final String TEXT_TAG = "text";
    public static final String TAGS_TAG = "tags";
    public static final String TAG_TAG = "tag";
    public static final String CREATION_DATE_TAG = "creationDate";
    public static final String LAST_ACCESSED_DATE_TAG = "lastAccessedDate";
    public static final String CONTENT_TAG = "content";

    //Attributes
    public static final String CLASS_ATTRIBUTE = "class";

    //Variables
    private ContextInterface contextInterface;
    private InputStream inputStream;

    public BookmarksXMLParser(ContextInterface contextInterface, InputStream xmlIn)
    {
        this.contextInterface = contextInterface;
        this.inputStream = xmlIn;//The calling program must close the stream.
    }

    public void parse()
        throws Exception
    {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        Node docNodeRoot = document.getDocumentElement();//reportRunParameters tag
        if (!docNodeRoot.getNodeName().equals(BookmarksXMLParser.BOOKMARKS_TAG))
        {
            throw new Exception("Unexpected element encountered as root node \""+docNodeRoot.getNodeName()+"\"");
        }

        NodeList nl = docNodeRoot.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++) {
            Node n = nl.item(c);
            System.out.println(n.getNodeName());
            switch (n.getNodeName()) {
                case BookmarksXMLParser.BLOCK_TAG:
                    System.out.println("block tag encountered ");
                    Node className = n.getAttributes().getNamedItem(BookmarksXMLParser.CLASS_ATTRIBUTE);

                    switch(className.getTextContent())
                    {
                        case "com.bookmarkanator.bookmarks.TextBookmark":
                            System.out.println("com.bookmarkanator.bookmarks.TextBookmark");
                            break;
                        case "com.bookmarkanator.bookmarks.WebBookmark":
                            System.out.println("com.bookmarkanator.bookmarks.WebBookmark");
                            break;
                    }
                    break;
            }
        }
    }

}
