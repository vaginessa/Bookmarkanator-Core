package com.bookmarkanator.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
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

        addBlocks(rootElement, doc, contextInterface);

        doc.appendChild(rootElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);


        StreamResult result = new StreamResult(new OutputStreamWriter(out));
        transformer.transform(source, result);
    }

    private void addBlocks(Element element,Document document, ContextInterface contextInterface)
    {
        Map<String, Element> blocks = new HashMap<>();

        Set<AbstractBookmark> bookmarks = contextInterface.getBookmarks();

        for (AbstractBookmark bookmark: bookmarks)
        {
            Element e = blocks.get(bookmark.getClass().getCanonicalName());

            if (e==null)
            {
                e = document.createElement(BookmarksXMLParser.BLOCK_TAG);
                blocks.put(bookmark.getClass().getCanonicalName(), e);
            }

            createBookmarkElement(e, document, bookmark);
        }
    }

    private void createBookmarkElement(Element parentElement, Document document, AbstractBookmark bookmark)
    {
        Element bookmarkNode = document.createElement(BookmarksXMLParser.BOOKMARK_TAG);
//        Element

    }
}
