package com.bookmarkanator.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import com.bookmarkanator.core.*;
import org.w3c.dom.*;

public class SettingsXMLWriter
{
    private OutputStream out;
    private Settings<String, String> settings;

    public SettingsXMLWriter(Settings settings, OutputStream out)
    {
        this.settings = settings;
        this.out = out;
    }

    public void write()throws Exception
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement(SettingsXMLParser.SETTINGS_TAG);

        for (String s: settings.keySet())
        {
            List<String> l = settings.getSettings(s);
            if (l!=null && !l.isEmpty())
            {//Only add a setting tag if it has values.
                appendSettings(doc, rootElement, s, l);
            }
        }

        doc.appendChild(rootElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);


        StreamResult result = new StreamResult(new OutputStreamWriter(out));
        transformer.transform(source, result);
    }

    public void appendSettings(Document doc, Element settingsElement,String key, List<String> values)
    {
        Element setting = doc.createElement(SettingsXMLParser.SETTING_TAG);
        Element keyElement= doc.createElement(SettingsXMLParser.KEY_TAG);
        keyElement.setTextContent(key);
        setting.appendChild(keyElement);

        for (String value: values)
        {
            Element valueTag = doc.createElement(SettingsXMLParser.VALUE_TAG);
            valueTag.setTextContent(value);
            setting.appendChild(valueTag);
        }

        settingsElement.appendChild(setting);
    }
}
