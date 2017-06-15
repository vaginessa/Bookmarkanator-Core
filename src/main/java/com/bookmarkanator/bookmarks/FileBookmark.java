package com.bookmarkanator.bookmarks;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import com.bookmarkanator.util.*;
import org.apache.logging.log4j.*;

public class FileBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(FileBookmark.class.getCanonicalName());
    private static String secretKey;
    String content;

    @Override
    public Set<String> getSearchWords()
    {
        return null;
    }

    @Override
    public void systemInit()
    {

    }

    @Override
    public void systemShuttingDown()
    {

    }

    @Override
    public String getTypeName()
    {
        return "File";
    }

    @Override
    public List<String> getTypeLocation()
    {
        return null;
    }

    @Override
    public String runAction(String commandString)
        throws Exception
    {
        File file = new File(getContent());
        open(file);
        return "";
    }

    @Override
    public void notifyBeforeAction(AbstractBookmark source, String actionString)
    {

    }

    @Override
    public void notifyAfterAction(AbstractBookmark source, String actionString)
    {

    }
    @Override
    public HandleData canHandle(String content)
    {
        return null;
    }
    @Override
    public boolean setSecretKey(String secretKey)
    {
        if (FileBookmark.secretKey == null && secretKey!=null)
        {
            FileBookmark.secretKey = secretKey;
            return true;
        }
        return false;
    }
    @Override
    protected String runTheAction(String action)
        throws Exception
    {
        return null;
    }

    @Override
    public void destroy()
        throws Exception
    {
        //do nothing
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new FileBookmark();
    }

    @Override
    public String getContent()
        throws Exception
    {
        return content;
    }

    @Override
    public void setContent(String content)
        throws Exception
    {
        this.content = content;
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        return this.getId().compareTo(o.getId());
    }

    /**
     * Opens a file system file in a system specific way.
     * <p/>
     * Note: Copied this code from https://stackoverflow.com/questions/7024031/java-open-a-file-windows-mac
     *
     * @param file The file to open.
     * @return True if successful.
     */
    public static boolean open(File file)
    {
        try
        {
            if (OSDetector.isWindows())
            {
                Runtime.getRuntime().exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", file.getAbsolutePath()});
                return true;
            }
            else if (OSDetector.isLinux() || OSDetector.isMac())
            {
                Runtime.getRuntime().exec(new String[] {"/usr/bin/open", file.getAbsolutePath()});
                return true;
            }
            else
            {
                // Unknown OS, try with desktop
                if (Desktop.isDesktopSupported())
                {
                    Desktop.getDesktop().open(file);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            return false;
        }
    }
}
