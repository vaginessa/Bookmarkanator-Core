package com.bookmarkanator.resourcetypes;

import com.bookmarkanator.interfaces.*;

/**
 * Represents a text only resource in a bookmark. These can be placed in the settings file for the current operating system, and used as constants.
 *
 * <p>
 *     BasicResources are resources that can be defined in a settings file; which will add them to the list of available resource types to add to
 *     bookmarks. When added to a bookmark they become the actual data of the bookmark, and are only needed in the settings file when a different
 *     type is created. The settings file really is like a list of shortcuts, and custom commands.
 * </p>
 */
public class BasicResource implements XMLWritable
{
    private String name;//the display name of this resource.
    private String text;
    private int indexWithinBookmark;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public int getIndexWithinBookmark()
    {
        return indexWithinBookmark;
    }

    public void setIndexWithinBookmark(int indexWithinBookmark)
    {
        this.indexWithinBookmark = indexWithinBookmark;
    }

    public String execute()
        throws Exception
    {
        return getText();
    }

    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs+"<basic-resource index-within-bookmark=\"");
        sb.append(indexWithinBookmark);
        sb.append("\">");
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
