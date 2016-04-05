package com.bookmarkanator.resourcetypes;

/**
 * Represents a text only resource.
 */
public class StringResource
{
    private String text;//Simply represents text.
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
}