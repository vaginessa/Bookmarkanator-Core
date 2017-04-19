package com.bookmarkanator.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import com.bookmarkanator.core.*;
import org.w3c.dom.*;
import org.w3c.dom.ls.*;

public class SettingsXMLParser
{
    //Tags
    public static final String ROOT_TAG = "root";
    public static final String SETTINGS_TAG = "settings";
    public static final String SETTING_TAG = "setting";
    public static final String VALUE_TAG = "value";

    public static final String TYPE_ATTRIBUTE = "type";
    public static final String KEY_ATTRIBUTE = "key";
//    public static final String CLASS_ATTRIBUTE = "class";

    //Variables
    private InputStream inputStream;
    private Document document;
    private Settings settings;
    private ClassLoader classLoader;

    public SettingsXMLParser(InputStream xmlIn, ClassLoader classLoader)
    {
        this.inputStream = xmlIn;//Note the calling program must close the stream.
        this.settings = new Settings();
        this.classLoader = classLoader;
    }

    public Settings parse()
        throws Exception
    {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        document = builder.parse(inputStream);

        Node docNodeRoot = document.getDocumentElement();//reportRunParameters tag
        if (!docNodeRoot.getNodeName().equals(SettingsXMLParser.ROOT_TAG))
        {
            throw new Exception("Unexpected element encountered as root node \"" + docNodeRoot.getNodeName() + "\"");
        }

        NodeList nl = docNodeRoot.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            if (n.getNodeName().equals(SettingsXMLParser.SETTINGS_TAG))
            {
                getSettings(n);
            }
        }
        return settings;
    }

    private void getSettings(Node node)
        throws Exception
    {
        NodeList nl = node.getChildNodes();
        Node type = node.getAttributes().getNamedItem(SettingsXMLParser.TYPE_ATTRIBUTE);

        String typeString = null;

        if (type != null)
        {
            typeString = type.getTextContent();
        }

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            if (n.getNodeName().startsWith("#"))
            {
                continue;
            }

            SettingItem item = getSetting(n);
            if (item == null)
            {
                continue;
            }
            item.setType(typeString);
            settings.putSetting(item);
        }
    }

    private SettingItem getSetting(Node node)
        throws Exception
    {
        Node key = node.getAttributes().getNamedItem(SettingsXMLParser.KEY_ATTRIBUTE);
        Objects.requireNonNull(key);

        String keyText = key.getTextContent();
        Objects.requireNonNull(keyText);

        SettingItem settingItem = new SettingItem(keyText);
        settingItem.setValue(getContent(node));

        return settingItem;
    }

//    private Class loadSettingClass(String className)
//        throws Exception
//    {
//        Class clazz = this.classLoader.loadClass(className);
//        Class sub = clazz.asSubclass(SettingItem.class);
//        System.out.println("Loaded Setting item class: \"" + className + "\".");
//        return sub;
//    }

//    private SettingItem instantiateClass(Class clazz, String key)
//        throws Exception
//    {
//        return (SettingItem) clazz.getConstructor(String.class).newInstance(key);
//    }

    /**
     * The content represents any data that the individual bookmark chooses to store here. It doesn't have to be in xml form.
     * It can be in any format because this method ignores everything between the two content tags, and simply returns the
     * raw data.
     * @param node  The content node to parse bookmark settings from.
     * @return  A string containing the settings specific to this bookmark.
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
     * @param xmlString  A string containing xml version ... strings.
     * @return  A string without xml version ... strings in it.
     */
    private String sanitizeXMLString(String xmlString)
    {
        return xmlString.replaceAll("[<]{1}[?]{1}.*[?]{1}[>]{1}\\s", "");
    }
}
