package com.bookmarkanator.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.bookmarkanator.xml.*;
import org.apache.commons.io.*;

public class FileIO implements BKIOInterface
{
    private FileContext context;
    private String bookmarksFileLocation;

    @Override
    public void init(String config)
        throws Exception
    {
        //TODO Add basic bookmark structure if it doesnt' exist.
        File file = new File(config);
        FileInputStream fin = new FileInputStream(file);
        validateXML(fin);
        fin = new FileInputStream(new File(config));

        loadBookmarks(fin);
        fin.close();
        bookmarksFileLocation = config;

        //TODO Make it so that it won't create multiple backup files if there have been no changes.
        //TODO Have a backup file created for the settings?
        //TODO Add a lock so that the user cannot start multiple bookmarkanator programs.
        //Adding a backup file for the bookmarks.
        Date date = new Date();
        String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
        String extension = FilenameUtils.getExtension(file.getName());
        File file2 = new File(file.getParent()+File.separator+date.toString()+"-"+fileNameWithOutExt+".backup."+extension);
        Files.copy(file.toPath(), file2.toPath());
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

}
