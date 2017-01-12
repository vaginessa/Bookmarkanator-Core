package com.bookmarkanator.io;

public class JSONFileIO implements BKIOInterface
{
    @Override
    public void init()
        throws Exception
    {
        //TODO: Implement.
    }

    @Override
    public void init(String config)
        throws Exception
    {

    }

    @Override
    public void save()
    {

    }

    @Override
    public void save(String config)
        throws Exception
    {

    }

    @Override
    public void close()
    {

    }

    /**
     * Returns a regular FileContext object but when the context object is to be saved or loaded all the default com.bookmarkanator.xml is converted into json.
     *
     * @return
     */
    @Override
    public ContextInterface getContext()
    {
        return null;
    }
}
