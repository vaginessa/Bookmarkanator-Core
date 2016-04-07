package com.bookmarkanator.writers;

import java.io.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.settings.*;

public class Writer
{
    public void writeSettings(Settings settings, File file)
        throws Exception
    {
        StringBuilder sb= new StringBuilder();
        FileOutputStream fout = new FileOutputStream(file);
        BufferedOutputStream bout = new BufferedOutputStream(fout);

        bout.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>".getBytes());
        settings.toXML(sb, "");
        bout.write(sb.toString().getBytes());

        bout.flush();
        fout.flush();

        bout.close();
        fout.close();
    }

    public void writeBookmark(Bookmark bookmark, File file)
        throws Exception
    {
        StringBuilder sb= new StringBuilder();
        FileOutputStream fout = new FileOutputStream(file);
        BufferedOutputStream bout = new BufferedOutputStream(fout);

        bout.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>".getBytes());
        bookmark.toXML(sb, "");
        bout.write(sb.toString().getBytes());

        bout.flush();
        fout.flush();

        bout.close();
        fout.close();
    }
}
