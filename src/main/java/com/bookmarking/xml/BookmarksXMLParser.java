package com.bookmarking.xml;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.*;
import com.bookmarking.*;
import com.bookmarking.bookmarks.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import org.apache.logging.log4j.*;
import org.w3c.dom.*;

public class BookmarksXMLParser implements FileReaderInterface<AbstractContext>
{
    private static final Logger logger = LogManager.getLogger(BookmarksXMLParser.class.getCanonicalName());
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
    public static final String XML_VERSION_ATTRIBUTE = "xmlVersion";

    //Date format
    public static final String DATE_FORMAT_STRING = "EEE, d MMM yyyy HH:mm:ss Z";

    // Current Version
    public static final String CURRENT_VERSION = "0.2";
    public static final String BASE_VERSION = "0.1";

    //Variables
    private AbstractContext abstractContext;
    private InputStream inputStream;
    private Document document;
    private String xmlVerison;

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
                abstractContext.addBookmark(abs);
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
                    // Set text as content when parsing files in from now on.
                    //                    abstractBookmark.setContent(URLDecoder.decode(n.getTextContent(),"UTF-8"));
                    abstractBookmark.setContent(n.getTextContent());
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
                    //                    abstractBookmark.setContent(URLDecoder.decode(n.getTextContent(),"UTF-8"));
                    abstractBookmark.setContent(n.getTextContent());
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

    @Override
    public AbstractContext parse(InputStream inputStream)
        throws Exception
    {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        document = builder.parse(inputStream);
        Map<String, Class> loadedClasses = new HashMap<>();

        Node docNodeRoot = document.getDocumentElement();

        if (!docNodeRoot.getNodeName().equals(BookmarksXMLParser.BOOKMARKS_TAG))
        {
            throw new Exception("Unexpected element encountered as root node \"" + docNodeRoot.getNodeName() + "\"");
        }

        Node xmlVersionNode = docNodeRoot.getAttributes().getNamedItem(XML_VERSION_ATTRIBUTE);

        // Getting the xml version number so earlier versions of the data can be parsed.
        if (xmlVersionNode != null)
        {
            xmlVerison = xmlVersionNode.getNodeValue();
            if (xmlVerison == null || xmlVerison.trim().isEmpty())
            {
                xmlVerison = BASE_VERSION;
            }
        }
        else
        {
            xmlVerison = BASE_VERSION;
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

                    SettingItem settingItem = GlobalSettings.use().getSettings().getSetting(Bootstrap.OVERRIDDEN_CLASSES, className);

                    if (settingItem != null)
                    {//Override class name specified in bookmark file with one specified in the settings file.
                        String replacementClassName = settingItem.getValue();

                        if (replacementClassName != null && !replacementClassName.isEmpty())
                        {
                            System.out.println("Overriding bookmark class name \"" + className + "\" with this class name \"" + replacementClassName +
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

        abstractContext.setClean();
        return abstractContext;
    }

    @Override
    public void setObject(AbstractContext obj)
    {
        this.abstractContext = obj;
    }

    @Override
    public void validate(InputStream inputStream)
        throws Exception
    {
        logger.trace("Validating xml");
        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/BookmarksStructure.xsd");
        XMLValidator.validate(inputStream, xsd);
    }

    @Override
    public FileSync.InvalidFilePolicy getInvalidFilePolicy()
    {
        return FileSync.InvalidFilePolicy.markBadAndContinue;
    }

    @Override
    public FileSync.MissingFilePolicy getMissingFilePolicy()
    {
        return FileSync.MissingFilePolicy.createNew;
    }

    @Override
    public FileSync.FileBackupPolicy getFileBackupPolicy()
    {
        return FileSync.FileBackupPolicy.SINGLE_BACKUP;
    }
}
