package com.bookmarkanator.bookmarks;

import java.awt.*;
import java.net.*;
import java.util.*;
import java.util.List;
import org.apache.logging.log4j.*;

/**
 * The text of this bookmark would represent a web address.
 */
public class WebBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(WebBookmark.class.getCanonicalName());
    private static String secretKey;
    private URL url;

    @Override
    public boolean setSecretKey(String secretKey)
    {
        if (WebBookmark.secretKey == null && secretKey != null)
        {
            WebBookmark.secretKey = secretKey;
            return true;
        }
        return false;
    }

    @Override
    public void notifyBeforeAction(Object source, String actionString)
    {

    }

    @Override
    public void notifyAfterAction(Object source, String actionString)
    {

    }

    @Override
    public String getTypeName()
    {
        return "Web";
    }

    @Override
    public String runAction(String actionCommand)
        throws Exception
    {
        logger.info("Hey I'm a log!");

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("mac"))
        {
            openMacBrowserWindow(getContent());
        }
        else if (os.contains("win"))
        {
            openWindowsBrowserWindow(getContent());
        }
        else
        {//Not windows or mac, assumed to be linux.
            openLinuxBrowserWindow(getContent());
        }
        return "";
    }

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
    public HandleData canHandle(String content)
    {
        return null;
    }

    @Override
    public void destroy()
        throws Exception
    {

    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("Web");
        return list;
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new WebBookmark();
    }

    @Override
    public String getContent()
    {
        return url.toString();
    }

    @Override
    public void setContent(String content)
        throws MalformedURLException
    {
        url = new URL(content);
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        //TODO IMPLEMENT
        return 0;
    }

    private void openLinuxBrowserWindow(String url)
        throws Exception
    {
        List<String[]> commands = new ArrayList<>();
        String[] s = new String[] {"which", "xdg-open"};
        commands.add(s);

        for (String[] arr : commands)
        {
            if (Runtime.getRuntime().exec(arr).getInputStream().read() != -1)
            {
                Runtime.getRuntime().exec(new String[] {arr[1], url});
            }
        }
    }

    private void openMacBrowserWindow(String url)
        throws Exception
    {
        if (Desktop.isDesktopSupported())
        {
            System.out.println("Desktop IS supported on this platform ");

            if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
            {
                System.out.println("Action BROWSE  IS supported on this platform ");
                Desktop.getDesktop().browse(new URI(url));
            }
            else
            {
                System.out.println("Action BROWSE  ISN'T supported on this platform ");
            }
        }
        else
        {
            System.out.println("Desktop ISN'T supported on this platform ");
        }
    }

    private void openWindowsBrowserWindow(String url)
        throws Exception
    {
        if (Desktop.isDesktopSupported())
        {
            System.out.println("Desktop IS supported on this platform ");

            if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
            {
                System.out.println("Action BROWSE  IS supported on this platform ");
                Desktop.getDesktop().browse(new URI(url));
            }
            else
            {
                System.out.println("Action BROWSE  ISN'T supported on this platform ");
            }
        }
        else
        {
            System.out.println("Desktop ISN'T supported on this platform ");
        }
    }
}
