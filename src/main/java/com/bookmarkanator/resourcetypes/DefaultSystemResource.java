package com.bookmarkanator.resourcetypes;

import java.awt.*;
import java.io.*;
import java.net.*;

/**
 * This class represents a generic Desktop.getDesktop().[service] that can be called from java.
 * These resources can be overridden in the settings system entry for the current operating system. In which case operating on a bookmark of this type
 * would call the overridden settings instead of the default java impelementation.
 */
public class DefaultSystemResource extends TerminalResource
{
    public static final int RESOURCE_TYPE_DEFAULT_WEB_BROWSER = 0;
    public static final int RESOURCE_TYPE_DEFAULT_FILE_BROWSER = 1;
    public static final int RESOURCE_TYPE_DEFAULT_FILE_EDITOR = 2;

    private int type;

    public DefaultSystemResource(int type)
    {
        this.type = type;
    }

    @Override
    public String execute()
        throws Exception
    {
        if (getType() == DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER)
        {
            Desktop.getDesktop().browse(new URI(getText()));
        }
        else if (getType() == DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR)
        {
            Desktop.getDesktop().edit(new File(getText()));
        }
        else if (getType() == DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER)
        {
            Desktop.getDesktop().open(new File(getText()));
        }
        else
        {
            throw new Exception("Cannot identify default system resource type: " + getType());
        }
        return getText();
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        switch (type)
        {
            case DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER:
                webToXML(sb, prependTabs);
                break;
            case DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR:
                fileEditorToXML(sb, prependTabs);
                break;
            case DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER:
                fileBrowserToXML(sb, prependTabs);
                break;
            default:
                super.toXML(sb, prependTabs);
        }
    }

    private void webToXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs + "<web index-within-bookmark=\"");
        sb.append(getIndexWithinBookmark());
        sb.append("\">");
        writeGuts(sb, prependTabs);
        sb.append(prependTabs + "</web>");
    }

    private void fileEditorToXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs + "<file-editor index-within-bookmark=\"");
        sb.append(getIndexWithinBookmark());
        sb.append("\">");
        writeGuts(sb, prependTabs);
        sb.append(prependTabs + "</file-editor>");
    }

    private void fileBrowserToXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs + "<file-browser index-within-bookmark=\"");
        sb.append(getIndexWithinBookmark());
        sb.append("\">");
        writeGuts(sb, prependTabs);
        sb.append(prependTabs + "</file-browser>");
    }

    private void writeGuts(StringBuilder sb, String prependTabs)
    {
        sb.append("\n");
        sb.append(prependTabs + "\t<name>");
        sb.append("\n");
        sb.append(prependTabs + "\t\t" + getName());
        sb.append("\n");
        sb.append(prependTabs + "\t</name>");
        sb.append("\n");
        sb.append(prependTabs + "\t<text>");
        sb.append("\n");
        sb.append(prependTabs + "\t\t" + getText());
        sb.append("\n");
        sb.append(prependTabs + "\t</text>");
        sb.append("\n");
        sb.append(prependTabs + "\t<pre-command>");
        sb.append("\n");
        sb.append(prependTabs + "\t\t" + getPreCommand());
        sb.append("\n");
        sb.append(prependTabs + "\t</pre-command>");
        sb.append("\n");
        sb.append(prependTabs + "\t<post-command>");
        sb.append("\n");
        sb.append(prependTabs + "\t\t" + getPostCommand());
        sb.append("\n");
        sb.append(prependTabs + "\t</post-command>");
        sb.append("\n");
    }
}
