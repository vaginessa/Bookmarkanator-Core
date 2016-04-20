package com.bookmarkanator.bookmarks;

import java.util.*;
import javax.swing.*;
import com.bookmarkanator.interfaces.*;

public class Tag implements ListableItem
{
    private String text;

    public Tag(String text)
    {
        this.text = text;
    }

    @Override
    public void setLastAccessedDate(Date lastAccessedDate)
    {
        //do nothing
    }

    @Override
    public void execute()
        throws Exception
    {
        System.out.println("Executing tag "+text);
    }

    @Override
    public String getText()
    {
        return text;
    }

    @Override
    public String getName()
    {
        return text;
    }

    @Override
    public String getTypeString()
    {
        return null;
    }

    @Override
    public Icon getIcon()
    {
        return null;
    }
}
