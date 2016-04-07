package com.bookmarkanator.resourcetypes;

import com.bookmarkanator.interfaces.*;

/**
 * Represents a text only resource in a bookmark. These can be placed in the settings file for the current operating system, and used as constants.
 */
public class BasicResource implements XMLWritable
{
    private String text;
    private String name;//the display name of this resource.

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String execute()
        throws Exception
    {
        return text;
    }

    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs+"<basic-resource>");
        sb.append("\n");
        sb.append(prependTabs+"\t<name>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+getName());
        sb.append("\n");
        sb.append(prependTabs+"\t</name>");
        sb.append("\n");
        sb.append(prependTabs+"\t<text>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+getText());
        sb.append("\n");
        sb.append(prependTabs+"\t</text>");
        sb.append("\n");
        sb.append(prependTabs+"</basic-resource>");
    }
}
