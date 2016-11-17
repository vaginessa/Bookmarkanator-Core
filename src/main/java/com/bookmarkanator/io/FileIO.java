package com.bookmarkanator.io;

import java.io.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.xml.*;

public class FileIO implements BKIOInterface
{

    public static final String defaultBookmarksFileName = "bookmarks.xml";
    public static final String defaultBookmarksDirectory = "Bookmark-anator";
    private FileContext context;

    @Override
    public void init()
        throws Exception
    {
        FileInputStream fin = new FileInputStream(getDefaultFile());
        validate(fin);
        fin = new FileInputStream(
            getDefaultFile());//need to open the file stream again because for some reason the validator closes the stream when it validates the xml.

        load(fin);
        fin.close();
    }

    @Override
    public void init(String config)
        throws Exception
    {
        FileInputStream fin = new FileInputStream(new File(config));
        validate(fin);
        fin = new FileInputStream(new File(config));

        load(fin);
        fin.close();
    }

    @Override
    public void save()
        throws Exception
    {
        FileOutputStream fout = new FileOutputStream(getDefaultFile());
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
        //close all file sources and stuff like that.
    }

    private void load(InputStream inputStream)
        throws Exception
    {
        context = new FileContext();
        context.setBKIOInterface(this);
        BookmarksXMLParser parser = new BookmarksXMLParser(context, inputStream);
        parser.parse();
    }

    private void validate(InputStream inputStream)
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

    private File getDefaultFile()
        throws IOException
    {
        String usersHome = System.getProperty("user.home");
        String directory = usersHome + File.separatorChar + FileIO.defaultBookmarksDirectory;
        String path = directory + File.separatorChar + FileIO.defaultBookmarksFileName;

        File file = new File(path);

        if (!file.exists())
        {
            if (file.getParentFile().mkdir())
            {
                file.createNewFile();
            }
            else
            {
                throw new IOException("Failed to create directory " + file.getParent());
            }
        }
        return file;
    }
}
