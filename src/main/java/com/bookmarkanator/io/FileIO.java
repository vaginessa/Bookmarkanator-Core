package com.bookmarkanator.io;

import com.bookmarkanator.core.*;

public class FileIO implements BKIOInterface {

    @Override
    public void init()
        throws Exception
    {
        //load the settings file and bookmarks files.
    }

    @Override
    public void init(String mode)
        throws Exception
    {
        //load the settings file and bookmarks files.
    }

    @Override
    public void save()
    {
        //save to the settings and bookmarks files.
    }

    @Override
    public void close()
    {
        //close all file sources and stuff like that.
    }

    @Override
    public ContextInterface getContext()
    {
        FileContext context = new FileContext();
        context.setBKIOInterface(this);
        return context;
    }
}
