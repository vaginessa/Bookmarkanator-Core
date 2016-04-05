package com.bookmarkanator.resourcetypes;

/**
 * Represents a resource that will can be called from the command prompt or terminal.
 */
public class SystemResource extends StringResource
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
}
