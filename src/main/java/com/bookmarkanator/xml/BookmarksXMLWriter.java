package com.bookmarkanator.xml;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;
import org.w3c.dom.*;

public class BookmarksXMLWriter implements FileWriterInterface<AbstractContext>
{
    private AbstractContext abstractContext;
    private OutputStream out;

    private void addBlocks(Element element,Document document, AbstractContext abstractContext)
        throws Exception
    {
        Map<String, Element> blocks = new HashMap<>();

        Set<AbstractBookmark> bookmarks = abstractContext.getBookmarks();

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

    @Override
    public void write(AbstractContext context, OutputStream out)
        throws Exception
    {
        this.out = out;
        this.abstractContext = context;

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(BookmarksXMLParser.BOOKMARKS_TAG);

        rootElement.setAttribute(BookmarksXMLParser.XML_VERSION_ATTRIBUTE,BookmarksXMLParser.CURRENT_VERSION);

        addBlocks(rootElement, doc, abstractContext);

        doc.appendChild(rootElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new OutputStreamWriter(out));
        transformer.transform(source, result);
    }

    @Override
    public void writeInitial(OutputStream outputStream)
        throws Exception
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(BookmarksXMLParser.BOOKMARKS_TAG);

        rootElement.setAttribute(BookmarksXMLParser.XML_VERSION_ATTRIBUTE,BookmarksXMLParser.CURRENT_VERSION);
        doc.appendChild(rootElement);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new OutputStreamWriter(outputStream));
        transformer.transform(source, result);
    }

    @Override
    public FileSync.FileBackupPolicy getFileBackupPolicy()
    {
        return FileSync.FileBackupPolicy.NO_BACKUP;
    }
}
