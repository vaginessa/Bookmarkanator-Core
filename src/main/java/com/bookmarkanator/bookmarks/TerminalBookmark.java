package com.bookmarkanator.bookmarks;

import java.io.*;
import java.util.*;
import org.apache.commons.exec.*;
import org.apache.logging.log4j.*;
import org.zeroturnaround.exec.*;

public class TerminalBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(TerminalBookmark.class.getCanonicalName());
    private static String secretKey;
    String command;
    private long timeOut = ExecuteWatchdog.INFINITE_TIMEOUT;
    private int substitueExitValue;

    //    private ExecuteWatchdog watchdog;
    @Override
    public boolean setSecretKey(String secretKey)
    {
        if (TerminalBookmark.secretKey == null && secretKey != null)
        {
            TerminalBookmark.secretKey = secretKey;
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
    public Set<String> getSearchWords()
    {
        return null;
    }

    @Override
    public void systemInit()
    {

    }

    @Override
    public HandleData canHandle(String content)
    {
        return null;
    }

    @Override
    public void systemShuttingDown()
    {

    }

    @Override
    public String getTypeName()
    {
        return "Terminal";
    }

    @Override
    public List<String> getTypeLocation()
    {
        return null;
    }

    @Override
    public String runAction(String actionCommand)
        throws Exception
    {

        new ProcessExecutor().destroyOnExit().command(command.split("~~")).redirectOutput(new LogOutputStream()
        {
            @Override
            protected void processLine(String line, int logLevel)
            {
                System.out.println(line.toString());
            }
        }).execute();

        //        CommandLine cmdLine = new CommandLine(command);
        //
        //        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        //
        //        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeOut);
        //        Executor executor = new DefaultExecutor();
        //        executor.setExitValue(substitueExitValue);
        //        executor.setWatchdog(watchdog);
        //        executor.execute(cmdLine, resultHandler);

        //        String[] env = {"PATH=/bin:/usr/bin/"};
        ////        ProcessBuilder pb = new ProcessBuilder(command, env);
        //
        //        Process prs = Runtime.getRuntime().exec(command, env);
        //
        ////        pb.redirectErrorStream(true);
        //        try {
        //
        ////            Process prs = pb.start();
        //            Thread inThread = new Thread(new In(prs.getInputStream()));
        //            inThread.start();
        //            Thread.sleep(2000);
        //            OutputStream writeTo = prs.getOutputStream();
        //            writeTo.write("Output:\n".getBytes());
        //            writeTo.flush();
        //            writeTo.close();
        //
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        return "";
    }

    @Override
    public void destroy()
        throws Exception
    {
        //        if (watchdog!=null)
        //        {
        //            watchdog.destroyProcess();
        //        }
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new TerminalBookmark();
    }

    @Override
    public String getContent()
    {
        return command;
    }

    @Override
    public void setContent(String content)
    {
        this.command = content;
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        return this.getId().compareTo(o.getId());
    }

    class In implements Runnable
    {
        private InputStream is;

        public In(InputStream is)
        {
            this.is = is;
        }

        @Override
        public void run()
        {
            byte[] b = new byte[1024];
            int size = 0;
            try
            {
                while (is.available() > 0)
                {
                    System.err.println(new String(b));
                }
                is.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
