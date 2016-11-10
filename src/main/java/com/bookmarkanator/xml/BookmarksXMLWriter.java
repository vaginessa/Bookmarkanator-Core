package com.bookmarkanator.xml;

import java.io.*;
import com.bookmarkanator.core.*;

public class BookmarksXMLWriter
{
    private ContextInterface contextInterface;
    private OutputStream out;

    public BookmarksXMLWriter(ContextInterface contextInterface, OutputStream outputStream)
    {
        this.contextInterface = contextInterface;
        this.out = outputStream;
    }

    public void write()
    {

    }
}
