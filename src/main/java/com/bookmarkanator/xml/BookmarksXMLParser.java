package com.bookmarkanator.xml;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.io.*;
import org.w3c.dom.*;
import org.w3c.dom.ls.*;

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

    //Date format
    public static final String DATE_FORMAT_STRING = "EEE, d MMM yyyy HH:mm:ss Z";

    //Variables
    private ContextInterface contextInterface;
    private InputStream inputStream;
    private Document document;


    public BookmarksXMLParser(ContextInterface contextInterface, InputStream xmlIn)
    {
        this.contextInterface = contextInterface;
        this.inputStream = xmlIn;//The calling program must close the stream.
    }

    public void parse()
        throws Exception
    {
        contextInterface.setAlwaysClean(true);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        document = builder.parse(inputStream);
        Map<String, Class> loadedClasses = new HashMap<>();

        Node docNodeRoot = document.getDocumentElement();//reportRunParameters tag
        if (!docNodeRoot.getNodeName().equals(BookmarksXMLParser.BOOKMARKS_TAG))
        {
            throw new Exception("Unexpected element encountered as root node \"" + docNodeRoot.getNodeName() + "\"");
        }

        NodeList nl = docNodeRoot.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            if (n.getNodeName().equals(BookmarksXMLParser.BLOCK_TAG))
            {
                try
                {
                    Node classNameNode = n.getAttributes().getNamedItem(BookmarksXMLParser.CLASS_ATTRIBUTE);
                    String className = classNameNode.getTextContent();

                         SettingItem settingItem = GlobalSettings.use().getSettings().getSetting(Bootstrap.OVERRIDDEN_CORE_CLASSES, className);

                        if (settingItem != null)
                        {//Override class name specified in bookmark file with one specified in the settings file.
                            String replacementClassName = settingItem.getValue();

                            if (replacementClassName!=null && !replacementClassName.isEmpty())
                            {
                                System.out
                                    .println("Overriding bookmark class name \"" + className + "\" with this class name \"" + replacementClassName +
                                        "\" from the settings file.");
                                className = replacementClassName;
                            }
                        }


                    //TODO Somethings not right with the settings loading. It should not be overriding bookmarks with the same bookmark class name.

                    className = className.trim();

                    Class clazz = loadedClasses.get(classNameNode.getTextContent());
                    AbstractBookmark abs = null;
                    if (clazz == null)
                    {
                        abs = ModuleLoader.use().loadClass(className, AbstractBookmark.class);
                        loadedClasses.put(className, abs.getClass());
                    }

                    //add all bookmarks of this type
                    parseBookmark(n, abs);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        contextInterface.setAlwaysClean(false);
    }

    private void parseBookmark(Node node, AbstractBookmark abstractBookmark)
        throws Exception
    {
        NodeList nl = node.getChildNodes();
        AbstractBookmark abs;

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);
            if (!n.getNodeName().startsWith("#"))
            {
                abs = abstractBookmark.getNew();
                parseBookmarkDetails(n, abs);
                contextInterface.addBookmark(abs);
            }
        }
    }

    private void parseBookmarkDetails(Node node, AbstractBookmark abstractBookmark)
        throws Exception
    {
        NodeList nl = node.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            switch (n.getNodeName())
            {
                case BookmarksXMLParser.NAME_TAG:
                    abstractBookmark.setName(n.getTextContent());
                    break;
                case BookmarksXMLParser.ID_TAG:
                    abstractBookmark.setId(UUID.fromString(n.getTextContent()));
                    break;
                case BookmarksXMLParser.TEXT_TAG:
                    abstractBookmark.setData(n.getTextContent());
                    break;
                case BookmarksXMLParser.TAGS_TAG:
                    abstractBookmark.setTags(getTags(n));
                    break;
                case BookmarksXMLParser.CREATION_DATE_TAG:
                    abstractBookmark.setCreationDate(getDate(n.getTextContent()));
                    break;
                case BookmarksXMLParser.LAST_ACCESSED_DATE_TAG:
                    abstractBookmark.setLastAccessedDate(getDate(n.getTextContent()));
                    break;
                case BookmarksXMLParser.CONTENT_TAG:
                    abstractBookmark.setSettings(getContent(n));
                    break;
                default:
                    if (!n.getNodeName().startsWith("#"))
                    {
                        throw new Exception("Unexpected element encountered \"" + n.getNodeName() + "\"");
                    }
            }
        }
        //load all the basic bookmark information
    }

    private Date getDate(String dateString)
        throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat(BookmarksXMLParser.DATE_FORMAT_STRING);
        return formatter.parse(dateString);
    }

    private Set<String> getTags(Node node)
    {
        Set<String> results = new HashSet<>();

        NodeList nl = node.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);
            if (n.getNodeName().equals(BookmarksXMLParser.TAG_TAG))
            {
                results.add(n.getTextContent());
            }
        }
        return results;
    }

    /**
     * The content represents any data that the individual bookmark chooses to store here. It doesn't have to be in xml form.
     * It can be in any format because this method ignores everything between the two content tags, and simply returns the
     * raw data.
     *
     * @param node The content node to parse bookmark settings from.
     * @return A string containing the settings specific to this bookmark.
     */
    private String getContent(Node node)
    {
        DOMImplementationLS ls = (DOMImplementationLS) document.getImplementation();
        LSSerializer ser = ls.createLSSerializer();
        StringBuilder sb = new StringBuilder();
        NodeList nl = node.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);
            sb.append(ser.writeToString(n));
        }

        return sanitizeXMLString(sb.toString());
    }

    /**
     * This method removes the annoying <?xml version="1.0" encoding="UTF-16"?> that the LSSerializer places on it's
     * string verison of the xml.
     *
     * @param xmlString A string containing xml version ... strings.
     * @return A string without xml version ... strings in it.
     */
    private String sanitizeXMLString(String xmlString)
    {
        return xmlString.replaceAll("[<]{1}[?]{1}.*[?]{1}[>]{1}\\s", "");
    }

}
