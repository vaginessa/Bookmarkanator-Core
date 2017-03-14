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
    private Settings settings;

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

        Element rootElement = doc.createElement(SettingsXMLParser.ROOT_TAG);
        Map<String, Set<SettingItem>> typesMap = this.settings.getSettingsTypesMap();

        for (String s: typesMap.keySet())
        {
            Set<SettingItem> items = typesMap.get(s);
            if (items!=null)
            {//Only add a setting tag if it has values.
                appendSettings(doc, rootElement,s, items);
            }
        }

        doc.appendChild(rootElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);


        StreamResult result = new StreamResult(new OutputStreamWriter(out));
        transformer.transform(source, result);
    }

    public void appendSettings(Document doc, Element rootElement,String type, Set<SettingItem> items)
        throws Exception
    {
        Element settings = doc.createElement(SettingsXMLParser.SETTINGS_TAG);
        settings.setAttribute(SettingsXMLParser.TYPE_ATTRIBUTE,type );

        for (SettingItem item: items)
        {
            Element setting = doc.createElement(SettingsXMLParser.SETTING_TAG);
            setting.setAttribute(SettingsXMLParser.KEY_ATTRIBUTE, item.getKey());
            setting.setAttribute(SettingsXMLParser.CLASS_ATTRIBUTE, item.getClass().getCanonicalName());
            setting.setTextContent(item.getValue());

            settings.appendChild(setting);
        }

        rootElement.appendChild(settings);
    }
}
