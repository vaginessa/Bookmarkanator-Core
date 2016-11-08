package com.bookmarkanator.io;

import com.bookmarkanator.core.*;

public class JSONFileIO implements BKIOInterface
{
    @Override
    public void init()
        throws Exception
    {

    }

    @Override
    public void init(String mode)
        throws Exception
    {

    }

    @Override
    public void save()
    {

    }

    @Override
    public void close()
    {

    }

    /**
     * Returns a regular FileContext object but when the context object is to be saved or loaded all the default xml is converted into json.
     * @return
     */
    @Override
    public ContextInterface getContext()
    {
        return null;
    }
}
