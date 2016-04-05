package com.bookmarkanator.resourcetypes;

/**
 * Represents text processed from a file resource.
 */
public class FileReaderResource extends StringResource
{
    private String commentIdentifier;
    private String keyValueIdentifier;
    private String fileSeparator;
    private String escapeCharacter;

    public String getCommentIdentifier()
    {
        return commentIdentifier;
    }

    public void setCommentIdentifier(String commentIdentifier)
    {
        this.commentIdentifier = commentIdentifier;
    }

    public String getKeyValueIdentifier()
    {
        return keyValueIdentifier;
    }

    public void setKeyValueIdentifier(String keyValueIdentifier)
    {
        this.keyValueIdentifier = keyValueIdentifier;
    }

    public String getFileSeparator()
    {
        return fileSeparator;
    }

    public void setFileSeparator(String fileSeparator)
    {
        this.fileSeparator = fileSeparator;
    }

    public String getEscapeCharacter()
    {
        return escapeCharacter;
    }

    public void setEscapeCharacter(String escapeCharacter)
    {
        this.escapeCharacter = escapeCharacter;
    }
}
