package com.bookmarkanator.io;

import java.io.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.xml.*;

public class FileIO implements BKIOInterface
{
    private FileContext context;
    private String bookmarksFileLocation;
    private GlobalSettings globalSettings;
    private ClassLoader classLoader;

    @Override
    public void init(String config)
        throws Exception
    {
        FileInputStream fin = new FileInputStream(new File(config));
        validateXML(fin);
        fin = new FileInputStream(new File(config));

        loadBookmarks(fin);
        fin.close();
        bookmarksFileLocation = config;
    }

    @Override
    public void init(String config, GlobalSettings globalSettings, ClassLoader classLoader)
        throws Exception
    {
        this.globalSettings = globalSettings;
        this.classLoader = classLoader;
        init(config);
    }

    @Override
    public void save()
        throws Exception
    {
        FileOutputStream fout = new FileOutputStream(bookmarksFileLocation);
        BookmarksXMLWriter writer = new BookmarksXMLWriter(context, fout);
        writer.write();
        fout.flush();
        fout.close();
    }

    @Override
    public void save(String config)
        throws Exception
    {
        FileOutputStream fout = new FileOutputStream(new File(config));
        BookmarksXMLWriter writer = new BookmarksXMLWriter(context, fout);
        writer.write();
        fout.flush();
        fout.close();
    }

    @Override
    public void close()
    {
        //close any open file sources.
    }

    private void loadBookmarks(InputStream inputStream)
        throws Exception
    {
        context = new FileContext();
        context.setBKIOInterface(this);
        BookmarksXMLParser parser = new BookmarksXMLParser(context, inputStream, globalSettings, this.classLoader);
        parser.parse();
    }

    private void validateXML(InputStream inputStream)
        throws Exception
    {
        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/BookmarksStructure.xsd");
        XMLValidator.validate(inputStream, xsd);
    }

    @Override
    public ContextInterface getContext()
    {
        if (this.context == null)
        {
            this.context = new FileContext();
            this.context.setBKIOInterface(this);
        }

        return context;
    }

}
