package com.bookmarkanator.io;

public class DatabaseIO implements BKIOInterface {



    @Override
    public void init(String config)
        throws Exception
    {
        //connects to the database
    }

    @Override
    public void save()
    {
        //writes any unsaved data to the database (might no be necessary if the database context object does this by default)
    }

    @Override
    public void save(String config)
        throws Exception
    {

    }

    @Override
    public void close()
    {
        //closes the database connection
    }

    @Override
    public ContextInterface getContext()
    {
        return null;
    }
}
