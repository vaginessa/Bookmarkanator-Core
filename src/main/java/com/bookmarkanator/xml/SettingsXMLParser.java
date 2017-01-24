package com.bookmarkanator.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import com.bookmarkanator.core.*;
import org.w3c.dom.*;

public class SettingsXMLParser
{
    //Tags
    public static final String SETTINGS_TAG = "settings";
    public static final String SETTING_TAG = "setting";
    public static final String KEY_TAG = "key";
    public static final String VALUE_TAG = "value";

    //Variables
    private InputStream inputStream;
    private Document document;
    private Settings settings;

    public SettingsXMLParser(InputStream xmlIn)
    {
        this.inputStream = xmlIn;//Note the calling program must close the stream.
        this.settings = new Settings();
    }

    public Settings parse()
        throws Exception
    {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        document = builder.parse(inputStream);

        Node docNodeRoot = document.getDocumentElement();//reportRunParameters tag
        if (!docNodeRoot.getNodeName().equals(SettingsXMLParser.SETTINGS_TAG))
        {
            throw new Exception("Unexpected element encountered as root node \"" + docNodeRoot.getNodeName() + "\"");
        }

        NodeList nl = docNodeRoot.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            if (n.getNodeName().equals(SettingsXMLParser.SETTING_TAG))
            {
               getSetting(n);
            }
        }
        return settings;
    }


    private void getSetting(Node node)
        throws Exception
    {
        List<String> results = new ArrayList<>();
        String key = null;
        NodeList nl = node.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);
            String textContent = n.getTextContent();

            if (n.getNodeName().equals(SettingsXMLParser.KEY_TAG))
            {
                key = textContent;
            }
            else if (n.getNodeName().equals(SettingsXMLParser.VALUE_TAG))
            {
                results.add(textContent);
            }
        }
       if (key!=null && key.trim().isEmpty())
       {
           throw new Exception("Settings key value must not be empty.");
       }

        settings.putSettings(key, results);
    }
}
