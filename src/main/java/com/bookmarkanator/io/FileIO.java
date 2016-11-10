package com.bookmarkanator.io;

import java.io.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.xml.*;

public class FileIO implements BKIOInterface {

    private FileContext context;
    @Override
    public void init()
        throws Exception
    {
        String usersHome = System.getProperty("user.home");
        FileInputStream fin = new FileInputStream(new File(usersHome));
        validate(fin);
        fin = new FileInputStream(new File(usersHome));

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
    public void save()throws Exception
    {
        String usersHome = System.getProperty("user.home");
        FileOutputStream fout = new FileOutputStream(new File(usersHome));
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
        FileContext context = new FileContext();
        context.setBKIOInterface(this);
        return context;
    }
}
