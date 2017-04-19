package com.bookmarkanator.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.xml.*;
import org.apache.commons.io.*;

public class FileIO implements BKIOInterface
{
    private ContextInterface context;
    private String bookmarksFileLocation;

    @Override
    public void init(String config)
        throws Exception
    {
        //TODO Add basic bookmark structure if it doesnt' exist. Or if file is empty or corrupt mark it as bad, create a new one, and move on.
        //TODO Figure out what to do about the bookmarks.xml file getting deleted if the program has an error. Possibly create a temporary file to read from while it is running?
        this.context = this.getContext();
        bookmarksFileLocation = config;
        File file = new File(config);

        if (!file.exists())
        {
//            file.createNewFile();
            this.save();
        }
        else
        {
            FileInputStream fin = new FileInputStream(file);
            validateXML(fin);
            fin = new FileInputStream(new File(config));
            loadBookmarks(fin);
            fin.close();
        }

        //TODO Have a backup file created for the settings?
        //TODO Add a lock so that the user cannot start multiple bookmarkanator programs.
        //Adding a backup file for the bookmarks.

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

        if (Bootstrap.context().isDirty())
        {
            createBookmarksBackup(new File(bookmarksFileLocation));
        }
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
        BookmarksXMLParser parser = new BookmarksXMLParser(context, inputStream);
        parser.parse();
    }

    private void createBookmarksBackup(File file)
        throws IOException
    {
        Date date = new Date();
        String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
        String extension = FilenameUtils.getExtension(file.getName());
        File file2 = new File(file.getParent()+File.separator+date.toString()+"-"+fileNameWithOutExt+".backup."+extension);
        Files.copy(file.toPath(), file2.toPath());
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
