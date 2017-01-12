package com.bookmarkanator.io;

import java.io.*;
import com.bookmarkanator.xml.*;

public class FileIO implements BKIOInterface
{
    //Note: Default file locations are called from the users home directory.
    //TODO Move the default bookmark settings to the settings gotten from the settings file.
    public static final String defaultBookmarksFileName = "bookmarks.xml";
    public static final String defaultBookmarksDirectory = "Bookmark-anator";
    private FileContext context;

    @Override
    public void init()
        throws Exception
    {
        FileInputStream fin = new FileInputStream(getDefaultBookmarksFile());
        validateXML(fin);
        fin = new FileInputStream(
            getDefaultBookmarksFile());//need to open the file stream again because for some reason the validator closes the stream when it validates the xml.

        loadStandardBookmarks(fin);
        fin.close();

//        loadModules(getDefaultBookmarksFile());
    }

    @Override
    public void init(String config)
        throws Exception
    {
        FileInputStream fin = new FileInputStream(new File(config));
        validateXML(fin);
        fin = new FileInputStream(new File(config));

        loadStandardBookmarks(fin);
        fin.close();
    }

    @Override
    public void save()
        throws Exception
    {
        FileOutputStream fout = new FileOutputStream(getDefaultBookmarksFile());
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

    private void loadStandardBookmarks(InputStream inputStream)
        throws Exception
    {
        context = new FileContext();
        context.setBKIOInterface(this);
        BookmarksXMLParser parser = new BookmarksXMLParser(context, inputStream);
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

    private File getDefaultBookmarksFile()
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

        //TODO handle empty bookmarks file.

        return file;
    }
}
