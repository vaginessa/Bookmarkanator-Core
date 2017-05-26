package com.bookmarkanator.xml;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;
import org.w3c.dom.*;

public class BookmarksXMLWriter
{
    private ContextInterface contextInterface;
    private OutputStream out;

    public BookmarksXMLWriter(ContextInterface contextInterface, OutputStream outputStream)
    {
        this.contextInterface = contextInterface;
        this.out = outputStream;
    }

    public void write()throws Exception
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(BookmarksXMLParser.BOOKMARKS_TAG);

        rootElement.setAttribute(BookmarksXMLParser.XML_VERSION_ATTRIBUTE,BookmarksXMLParser.CURRENT_VERSION);

        addBlocks(rootElement, doc, contextInterface);

        doc.appendChild(rootElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);


        StreamResult result = new StreamResult(new OutputStreamWriter(out));
        transformer.transform(source, result);
    }

    private void addBlocks(Element element,Document document, ContextInterface contextInterface)
        throws Exception
    {
        Map<String, Element> blocks = new HashMap<>();

        Set<AbstractBookmark> bookmarks = contextInterface.getBookmarks();

        for (AbstractBookmark bookmark: bookmarks)
        {
            Element e = blocks.get(bookmark.getClass().getCanonicalName());

            if (e==null)
            {
                e = document.createElement(BookmarksXMLParser.BLOCK_TAG);
                e.setAttribute(BookmarksXMLParser.CLASS_ATTRIBUTE, bookmark.getClass().getCanonicalName());
                blocks.put(bookmark.getClass().getCanonicalName(), e);
            }

            appendBookmarkElement(e, document, bookmark);
        }

        for (String s: blocks.keySet())
        {
            element.appendChild(blocks.get(s));
        }
    }

    private void appendBookmarkElement(Element parentElement, Document document, AbstractBookmark bookmark)
        throws Exception
    {
        Element bookmarkNode = document.createElement(BookmarksXMLParser.BOOKMARK_TAG);

        Element bookmarkName = document.createElement(BookmarksXMLParser.NAME_TAG);
        bookmarkName.setTextContent(bookmark.getName());
        bookmarkNode.appendChild(bookmarkName);

        Element bookmarkId = document.createElement(BookmarksXMLParser.ID_TAG);
        bookmarkId.setTextContent(bookmark.getId().toString());
        bookmarkNode.appendChild(bookmarkId);

//        Element bookmarkText = document.createElement(BookmarksXMLParser.TEXT_TAG);
//        bookmarkText.setTextContent(bookmark.getContent());
//        bookmarkNode.appendChild(bookmarkText);

        Element bookmarkCreationDate = document.createElement(BookmarksXMLParser.CREATION_DATE_TAG);
        bookmarkCreationDate.setTextContent(getDateString(bookmark.getCreationDate()));
        bookmarkNode.appendChild(bookmarkCreationDate);

        Element bookmarkLastAccessedDate = document.createElement(BookmarksXMLParser.LAST_ACCESSED_DATE_TAG);
        bookmarkLastAccessedDate.setTextContent(getDateString(bookmark.getLastAccessedDate()));
        bookmarkNode.appendChild(bookmarkLastAccessedDate);

        Element bookmarTags = document.createElement(BookmarksXMLParser.TAGS_TAG);

        appendBookmarkTagsElements(bookmarTags, document, bookmark);
        bookmarkNode.appendChild(bookmarTags);

        String content = bookmark.getContent();
        if (content!=null)
        {
            Element contentTag = document.createElement(BookmarksXMLParser.CONTENT_TAG);
//            contentTag.setTextContent(URLEncoder.encode(bookmark.getContent(), "UTF-8"));
            contentTag.setTextContent(bookmark.getContent());
            bookmarkNode.appendChild(contentTag);
        }

        parentElement.appendChild(bookmarkNode);
    }

    private void appendBookmarkTagsElements(Element bookmarkTagsElement, Document document, AbstractBookmark bookmark)
    {
        for (String tag: bookmark.getTags())
        {
            Element tagElement = document.createElement(BookmarksXMLParser.TAG_TAG);
            tagElement.setTextContent(tag);
            bookmarkTagsElement.appendChild(tagElement);
        }
    }

    private String getDateString(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(BookmarksXMLParser.DATE_FORMAT_STRING);
        return formatter.format(date);
    }
}
