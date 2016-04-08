package com.bookmarkanator.parsers;

import javax.xml.stream.XMLStreamReader;

/**
 * Created by micah on 4/7/16.
 */
public class ParserUtil {

    public static String limitWhiteSpace(String string)
    {
        return string.replaceAll("\\n( )*", " ");
    }

    public static String getStartElementAttributes(XMLStreamReader reader)
    {
        int a = reader.getAttributeCount();

        String attrs = "";

        for (int c = 0; c < a; c++)
        {
            if (reader.getAttributePrefix(c).trim().isEmpty())
            {
                attrs = attrs + " " + reader.getAttributeLocalName(c) + "=\"" + reader.getAttributeValue(c) +
                        "\"";
            }
            else
            {
                attrs = attrs + " " + reader.getAttributePrefix(c) + ":" + reader.getAttributeLocalName(c) + "=\"" + reader.getAttributeValue(c) +
                        "\"";
            }
        }
        return attrs;
    }

    /**
     * Gets the specific attribute associated with the current element.
     */
    public static String getStartElementAttribute(XMLStreamReader reader, String attrName)
    {
        int a = reader.getAttributeCount();
        String s;

        for (int c = 0; c < a; c++)
        {
            s = reader.getAttributePrefix(c).trim() + reader.getAttributeLocalName(c);
            if (!s.isEmpty())
            {
                if (s.equals(attrName))
                {
                    return reader.getAttributeValue(c);
                }
            }
        }
        return null;
    }

    public static String getEndElement(XMLStreamReader reader)
    {
        return ("</" + reader.getLocalName() + ">");
    }

    public static String getStartElement(XMLStreamReader reader)
    {
        return ("<" + reader.getLocalName() + getStartElementAttributes(reader) + ">").trim();
    }
}
