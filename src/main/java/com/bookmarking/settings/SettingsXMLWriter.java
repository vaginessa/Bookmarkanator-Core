package com.bookmarking.settings;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.settings.types.*;
import org.w3c.dom.*;

public class SettingsXMLWriter implements FileWriterInterface<Settings>
{
    @Override
    public void write(Settings settings, OutputStream outputStream)
        throws Exception
    {
        Document doc = getDocument();

        Element rootElement = doc.createElement(SettingsXMLParser.SETTINGS_ELEMENT);
        doc.appendChild(rootElement);

        // <Group name, SettingsGroup>
        Map<String, SettingsGroup> groupsMap = settings.getGroups();

        for (String s : groupsMap.keySet())
        {
            SettingsGroup group = groupsMap.get(s);
            writeGroup(doc, rootElement, group, s);
        }

        writeOut(doc, outputStream);
    }

    @Override
    public void writeInitial(OutputStream outputStream)
        throws Exception
    {
        Document doc = getDocument();

        Element rootElement = doc.createElement(SettingsXMLParser.SETTINGS_ELEMENT);
        doc.appendChild(rootElement);

        writeOut(doc, outputStream);
    }

    @Override
    public FileSync.FileBackupPolicy getFileBackupPolicy()
    {
        return FileSync.FileBackupPolicy.NO_BACKUP;
    }

    public void writeGroup(Document doc, Element rootElement, SettingsGroup group, String groupName)
        throws Exception
    {
        Element groupElement = doc.createElement(SettingsXMLParser.GROUP_ELEMENT);
        groupElement.setAttribute(SettingsXMLParser.GROUP_NAME_ATTRIBUTE, groupName);

        Map<String, AbstractSetting> settingMap = group.getSettingsMap();
        Map<String, Map<String, AbstractSetting>> typesMap = new HashMap<>();

        for (String key : settingMap.keySet())
        {
            AbstractSetting settingItem = settingMap.get(key);
            String type;

            if (settingItem instanceof FileSetting)
            {
                type = SettingsXMLParser.FILE_ELEMENT;
            }
            else if (settingItem instanceof OverridingClassSetting)
            {
                type = SettingsXMLParser.OVERRIDING_CLASS_ELEMENT;
            }
            else if (settingItem instanceof InterfaceSelectSetting)
            {
                type = SettingsXMLParser.INTERFACE_CLASS_ELEMENT;
            }
            else if (settingItem instanceof ClassSetting)
            {
                type = SettingsXMLParser.CLASS_ELEMENT;
            }
            else if (settingItem instanceof DoubleSetting)
            {
                type = SettingsXMLParser.DOUBLE_ELEMENT;
            }
            else if (settingItem instanceof IntegerSetting)
            {
                type = SettingsXMLParser.INTEGER_ELEMENT;
            }
            else if (settingItem instanceof StringSetting)
            {
                type = SettingsXMLParser.STRING_ELEMENT;
            }
            else if (settingItem instanceof BooleanSetting)
            {
                type = SettingsXMLParser.BOOLEAN_ELEMENT;
            }
            else if (settingItem instanceof URLSetting)
            {
                type = SettingsXMLParser.URL_ELEMENT;
            }
            else
            {
                throw new Exception("Unknown setting type encountered: "+settingItem.getClass().getCanonicalName());
            }

            Map<String, AbstractSetting> map = typesMap.get(type);

            if (map==null)
            {
                map = new HashMap<>();
                typesMap.put(type, map);
            }

            map.put(settingItem.getKey(), settingItem);
        }

        List<String> typesList = new ArrayList<>(typesMap.keySet());
        Collections.sort(typesList);

        for (String type : typesList)
        {
            Element typeElement = doc.createElement(type);
            groupElement.appendChild(typeElement);

            Map<String, AbstractSetting> map = typesMap.get(type);
            Objects.requireNonNull(map);

            List<String> keysList = new ArrayList<>(map.keySet());
            Collections.sort(keysList);

            for (String key: keysList)
            {
                AbstractSetting abstractSetting = map.get(key);
                appendSetting(doc, typeElement, abstractSetting);
            }
        }

        rootElement.appendChild(groupElement);
    }

    private void appendSetting(Document doc, Element element, AbstractSetting abstractSetting)
    {
        Element setting = doc.createElement(SettingsXMLParser.SETTING_ELEMENT);
        element.appendChild(setting);

        Element key = doc.createElement(SettingsXMLParser.KEY_ELEMENT);
        key.setTextContent(abstractSetting.getKey());
        setting.appendChild(key);

        if (abstractSetting.getValue()!=null && !abstractSetting.getValue().toString().trim().isEmpty())
        {
            Element value = doc.createElement(SettingsXMLParser.VALUE_ELEMENT);

            if (abstractSetting.getValue() instanceof Class)
            {
                value.setTextContent(((Class)abstractSetting.getValue()).getCanonicalName());
            }
            else
            {
                value.setTextContent(abstractSetting.getValue().toString());
            }

            setting.appendChild(value);
        }
    }

    private Document getDocument()
        throws Exception
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }

    private void writeOut(Document doc, OutputStream outputStream)
        throws Exception
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new OutputStreamWriter(outputStream));
        transformer.transform(source, result);
    }
}
