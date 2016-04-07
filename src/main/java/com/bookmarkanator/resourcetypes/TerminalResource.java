package com.bookmarkanator.resourcetypes;

/**
 * Represents a resource that will can be called from the command prompt or terminal.
 * <p>
 * This class enables custom commands to be mapped, and to show up as system resources when the user is creating new bookmarks.
 * <p>
 * <p>
 * For instance a bookmark could be added that calls a grep command with specific flags, or other parameters. When the user creates a bookmark they
 * will only need to specify the text inserted into the bookmark, and be placed inbetween the preCommand, and postCommand strings.
 * </p>
 */
public class TerminalResource extends BasicResource
{
    private String preCommand;
    private String postCommand;

    public String getPreCommand()
    {
        return preCommand;
    }

    public void setPreCommand(String preCommand)
    {
        this.preCommand = preCommand;
    }

    public String getPostCommand()
    {
        return postCommand;
    }

    public void setPostCommand(String postCommand)
    {
        this.postCommand = postCommand;
    }

    @Override
    public String execute()
        throws Exception
    {
        return getText();
    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs + "<terminal-resource index-within-bookmark=\"");
        sb.append(getIndexWithinBookmark());
        sb.append("\">");
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
        sb.append(prependTabs + "</terminal-resource>");
    }
}
