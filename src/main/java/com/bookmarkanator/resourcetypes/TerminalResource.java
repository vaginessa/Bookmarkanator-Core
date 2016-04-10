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
public class TerminalResource extends BasicResource {
    private String preCommand;
    private String postCommand;

    public String getPreCommand() {
        return preCommand;
    }

    public void setPreCommand(String preCommand) {
        this.preCommand = preCommand;
    }

    public String getPostCommand() {
        return postCommand;
    }

    public void setPostCommand(String postCommand) {
        this.postCommand = postCommand;
    }

    @Override
    public String execute()
            throws Exception {
        return getText();
    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs) {
        sb.append(prependTabs + "<terminal-resource>");
        sb.append("\n");
        sb.append(prependTabs + "\t<name>");
        sb.append(getName());
        sb.append("</name>");
        sb.append("\n");
        sb.append(prependTabs + "\t<text>");
        sb.append(getText());
        sb.append("</text>");
        sb.append("\n");
        sb.append(prependTabs + "\t<pre-command>");
        sb.append(getPreCommand());
        sb.append("</pre-command>");
        sb.append("\n");
        sb.append(prependTabs + "\t<post-command>");
        sb.append(getPostCommand());
        sb.append("</post-command>");
        sb.append("\n");
        sb.append(prependTabs + "</terminal-resource>");
    }

    @Override
    public int hashCode() {
        return super.hashCode() + getPreCommand().hashCode() + getPostCommand().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof DefaultSystemResource) {
                DefaultSystemResource d = (DefaultSystemResource) obj;

                if (d.getName().equals(getName())) {
                    if (d.getText().equals(getText())) {
                        if (d.getPreCommand().equals(getPreCommand())) {
                            if (d.getPostCommand().equals(getPostCommand())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
