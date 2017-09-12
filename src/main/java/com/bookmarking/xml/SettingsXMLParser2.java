package com.bookmarking.xml;

import java.io.*;
import javax.xml.parsers.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.settings.*;
import org.w3c.dom.*;

public class SettingsXMLParser2 implements FileReaderInterface<Settings>
{
    // Tags
    public static final String SETTINGS_ELEMENT = "settings";
    public static final String GROUP_ELEMENT = "group";
    public static final String FILE_ELEMENT = "file";
    public static final String CLASS_ELEMENT = "class";
    public static final String STRING_ELEMENT = "string";
    public static final String FLOAT_ELEMENT = "float";
    public static final String INTEGER_ELEMENT = "integer";
    public static final String BOOLEAN_ELEMENT = "boolean";
    public static final String SETTING_ELEMENT = "setting";
    public static final String KEY_ELEMENT = "key";
    public static final String VALUE_ELEMENT = "value";

    // Attributes
    public static final String GROUP_NAME_ATTRIBUTE = "name";
    public static final String DEFAULT_GROUP_ATTRIBUTE = "default";

    private Document document;
    private Settings settings;

    public SettingsXMLParser2()
    {
        this.settings = new Settings();
    }

    @Override
    public Settings parse(InputStream inputStream)
        throws Exception
    {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        document = builder.parse(inputStream);

        // Settings element
        Node docNodeRoot = document.getDocumentElement();
        if (!docNodeRoot.getNodeName().equals(SettingsXMLParser2.SETTINGS_ELEMENT))
        {
            throw new Exception("Unexpected element encountered as root node \"" + docNodeRoot.getNodeName() + "\"");
        }

        NodeList nl = docNodeRoot.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            if (n.getNodeName().equals(SettingsXMLParser2.GROUP_ELEMENT))
            {
                getGroup(n);
            }
        }
        return settings;
    }

    @Override
    public void setObject(Settings obj)
    {
        this.settings = obj;
    }

    @Override
    public void validate(InputStream inputStream)
        throws Exception
    {
        // TODO fix schema so it can validate the settings file.
        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/SettingsStructure.xsd");
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


    private void getGroup(Node groupNode)
        throws Exception
    {
        NodeList nl = groupNode.getChildNodes();
        Node groupName = groupNode.getAttributes().getNamedItem(SettingsXMLParser2.GROUP_NAME_ATTRIBUTE);

        String group;

        if (groupName != null)
        {
            group = groupName.getTextContent();
        }
        else
        {
            group = SettingsXMLParser2.DEFAULT_GROUP_ATTRIBUTE;
        }

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node typeElement = nl.item(c);

            if (typeElement.getNodeName().startsWith("#"))
            {
                continue;
            }

            getType(typeElement, group);
        }
    }

    private void getType(Node node, String group)
        throws Exception
    {
        NodeList nl = node.getChildNodes();
        AbstractSetting res = null;

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            if (n.getNodeName().startsWith("#"))
            {
                continue;
            }

            settings.putSetting(getKeyValue(n,group, node.getNodeName()));
        }
    }

    private AbstractSetting getKeyValue(Node node, String group, String typeString)
        throws Exception
    {
        AbstractSetting res = null;

        switch (typeString)
        {
            case FILE_ELEMENT:
                res = getKeyValue(node, new FileSetting(), typeString);
                break;
            case CLASS_ELEMENT:
                res = getKeyValue(node, new ClassSetting(), typeString);
                break;
            case FLOAT_ELEMENT:
                res = getKeyValue(node, new FloatSetting(), typeString);
                break;
            case INTEGER_ELEMENT:
                res = getKeyValue(node, new IntegerSetting(), typeString);
                break;
            case STRING_ELEMENT:
                res = getKeyValue(node, new StringSetting(), typeString);
                break;
            case BOOLEAN_ELEMENT:
                res = getKeyValue(node, new BooleanSetting(), typeString);
                    break;
                default:
                    throw new Exception("Unknown element encountered "+node.getNodeName());
        }

        if (res!=null)
        {
            res.setGroup(group);
        }

        return res;
    }

    private AbstractSetting getKeyValue(Node node, AbstractSetting abstractSetting, String typeString)
        throws Exception
    {
        NodeList nl = node.getChildNodes();

        for (int c = 0; c < nl.getLength(); c++)
        {
            Node n = nl.item(c);

            if (n.getNodeName().startsWith("#"))
            {
                continue;
            }

            switch (n.getNodeName())
            {
                case KEY_ELEMENT:
                    abstractSetting.setKey(n.getTextContent());
                    break;
                case VALUE_ELEMENT:
                    switch (typeString)
                    {
                        case FILE_ELEMENT:
                            abstractSetting.setValue(new File(n.getTextContent()));
                            break;
                        case CLASS_ELEMENT:
                            abstractSetting.setValue(Class.forName(n.getTextContent()));
                            break;
                        case FLOAT_ELEMENT:
                            abstractSetting.setValue(Float.parseFloat(n.getTextContent()));
                            break;
                        case INTEGER_ELEMENT:
                            abstractSetting.setValue(Integer.parseInt(n.getTextContent()));
                            break;
                        case STRING_ELEMENT:
                            abstractSetting.setValue(n.getTextContent());
                            break;
                        case BOOLEAN_ELEMENT:
                            abstractSetting.setValue(Boolean.parseBoolean(n.getTextContent()));
                            break;
                        default:
                            throw new Exception("Unknown element encountered "+node.getNodeName());
                    }
                    break;
                default:
                    throw new Exception("Unknown element encountered "+node.getNodeName());
            }
        }
        return abstractSetting;
    }
}