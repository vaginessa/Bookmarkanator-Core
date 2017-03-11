package com.bookmarkanator.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import com.bookmarkanator.core.*;
import org.w3c.dom.*;

public class SettingsXMLParser
{
    //Tags
    public static final String ROOT_TAG = "root";
    public static final String SETTINGS_TAG = "settings";
    public static final String SETTING_TAG = "setting";
    public static final String VALUE_TAG = "value";

    public static final String TYPE_ATTRIBUTE = "type";
    public static final String KEY_ATTRIBUTE = "key";
    public static final String CLASS_ATTRIBUTE = "class";

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

        String className;

        Node theClass = node.getAttributes().getNamedItem(SettingsXMLParser.CLASS_ATTRIBUTE);
        if (theClass == null)
        {
            className = SettingItem.class.getCanonicalName();
        }
        else
        {
            className = theClass.getTextContent();
            if (className == null)
            {
                className = SettingItem.class.getCanonicalName();
            }
        }

        Class clazz = loadSettingClass(className);
        Objects.requireNonNull(clazz);

        SettingItem settingItem = instantiateClass(clazz, keyText);

        NodeList nl = node.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            if (n.getNodeName().startsWith("#"))
            {
                continue;
            }

            if (n.getNodeName().equals(SettingsXMLParser.VALUE_TAG))
            {
                settingItem.setValue(n.getTextContent());
            }
        }
        return settingItem;
    }

    private Class loadSettingClass(String className)
        throws Exception
    {
        Class clazz = this.classLoader.loadClass(className);
        Class sub = clazz.asSubclass(SettingItem.class);
        System.out.println("Loaded Setting item class: \"" + className + "\".");
        return sub;
    }

    private SettingItem instantiateClass(Class clazz, String key)
        throws Exception
    {
        return (SettingItem) clazz.getConstructor(String.class).newInstance(key);
    }
}
