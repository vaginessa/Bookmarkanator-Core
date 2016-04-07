package com.bookmarkanator.resourcetypes;

/**
 * Represents text processed from a file resource.
 *
 * Users can define this class in the settings file for various kinds of file formats. For more control over file filtering use the CustomFileFilter class.
 *
 */
public class FileFilterResource extends BasicResource
{
    private String commentIdentifier;
    private String keyValueSeparator;
    private String keyValuePairSeparator;
    private String escapeString;

    public String getCommentIdentifier()
    {
        return commentIdentifier;
    }

    public void setCommentIdentifier(String commentIdentifier)
    {
        this.commentIdentifier = commentIdentifier;
    }

    public String getKeyValueSeparator()
    {
        return keyValueSeparator;
    }

    public void setKeyValueSeparator(String keyValueSeparator)
    {
        this.keyValueSeparator = keyValueSeparator;
    }

    public String getKeyValuePairSeparator()
    {
        return keyValuePairSeparator;
    }

    public void setKeyValuePairSeparator(String keyValuePairSeparator)
    {
        this.keyValuePairSeparator = keyValuePairSeparator;
    }

    public String getEscapeString()
    {
        return escapeString;
    }

    public void setEscapeString(String escapeString)
    {
        this.escapeString = escapeString;
    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs+"<file-filter-resource>");
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
        sb.append(prependTabs+"\t<comment-identifier>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+getCommentIdentifier());
        sb.append("\n");
        sb.append(prependTabs+"\t</comment-identifier>");
        sb.append("\n");
        sb.append(prependTabs+"\t<key-value-separator>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+ getKeyValueSeparator());
        sb.append("\n");
        sb.append(prependTabs+"\t</key-value-separator>");
        sb.append("\n");
        sb.append(prependTabs+"\t<key-value-pair-separator>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+ getKeyValuePairSeparator());
        sb.append("\n");
        sb.append(prependTabs+"\t</key-value-pair-separator>");
        sb.append("\n");
        sb.append(prependTabs+"\t<escape-string>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+ getEscapeString());
        sb.append("\n");
        sb.append(prependTabs+"\t</escape-string>");
        sb.append("\n");
        sb.append(prependTabs+"</file-filter-resource>");
    }
}
