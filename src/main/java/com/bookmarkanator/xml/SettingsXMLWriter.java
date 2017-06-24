package com.bookmarkanator.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import com.bookmarkanator.core.*;
import org.w3c.dom.*;

public class SettingsXMLWriter implements FileWriterInterface<Settings>
{
    @Override
    public void write(Settings settings, OutputStream outputStream)throws Exception
    {
        Document doc = getDocument();

        Element rootElement = doc.createElement(SettingsXMLParser.ROOT_TAG);
        Map<String, Set<SettingItem>> typesMap = settings.getSettingsTypesMap();

        for (String s: typesMap.keySet())
        {
            Set<SettingItem> items = typesMap.get(s);
            if (items!=null)
            {//Only add a setting tag if it has values.
                appendSettings(doc, rootElement,s, items);
            }
        }

        doc.appendChild(rootElement);
        writeOut(doc, outputStream);
    }

    @Override
    public void writeInitial(OutputStream outputStream) throws Exception{
       Document doc = getDocument();

        Element rootElement = doc.createElement(SettingsXMLParser.ROOT_TAG);
        doc.appendChild(rootElement);

        writeOut(doc, outputStream);
    }

    @Override
    public FileSync.FileBackupPolicy getFileBackupPolicy() {
        return FileSync.FileBackupPolicy.NO_BACKUP;
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
            setting.setTextContent(item.getValue());
            settings.appendChild(setting);
        }

        rootElement.appendChild(settings);
    }

    private Document getDocument() throws Exception
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }

    private void writeOut(Document doc, OutputStream outputStream) throws Exception
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);


        StreamResult result = new StreamResult(new OutputStreamWriter(outputStream));
        transformer.transform(source, result);
    }
}
