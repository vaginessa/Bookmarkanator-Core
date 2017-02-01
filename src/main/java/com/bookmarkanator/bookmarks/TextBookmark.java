package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.io.*;

public class TextBookmark extends AbstractBookmark
{

    /**
     * This constructor is used to allow custom behaviour or pre-processing to occur relative to the bookmark context object, or for instance
     * to throw an error if more than one bookmark is created, or other such custom behaviours.
     *
     * @param contextInterface The Bookmark context object for the custom bookmark to use.
     */
    public TextBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
    }

    @Override
    public String getTypeName()
    {
        return "Text";
    }

    @Override
    public void action()
        throws Exception
    {
        //do nothing
        System.out.println(this.getText());
    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("");
        return list;
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new TextBookmark(null);
    }

    @Override
    public String getSettings()
    {
        return null;
    }

    @Override
    public void setSettings(String xml)
    {

    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        //TODO IMPLEMENT
        return 0;
    }
}
