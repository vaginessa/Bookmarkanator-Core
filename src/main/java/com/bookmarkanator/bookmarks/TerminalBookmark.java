package com.bookmarkanator.bookmarks;

import java.io.*;
import java.util.*;
import org.apache.commons.exec.*;
import org.apache.logging.log4j.*;

public class TerminalBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(TerminalBookmark.class.getCanonicalName());

    String command;
    private long timeOut = ExecuteWatchdog.INFINITE_TIMEOUT;
    private int substitueExitValue;
//    private ExecuteWatchdog watchdog;

    @Override
    public Set<String> getSearchWords()
    {
        return null;
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
    public void runAction()
        throws Exception
    {
//
//        new ProcessExecutor().command(command)
//            .redirectOutput(new LogOutputStream() {
//
//                @Override
//                protected void processLine(String line, int logLevel)
//                {
//                    System.out.println(line.toString());
//                }
//            })
//            .execute();


//        CommandLine cmdLine = CommandLine.parse(command);
//
//        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
//
//        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeOut);
//        Executor executor = new DefaultExecutor();
//        executor.setExitValue(substitueExitValue);
//        executor.setWatchdog(watchdog);
//        executor.execute(cmdLine, resultHandler);
        List<String> commands = new ArrayList<String>();
        commands.add("ls");
        commands.add("-all");
        commands.add("|");
        commands.add("grep");
        commands.add("a");
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        try {

            Process prs = pb.start();
            Thread inThread = new Thread(new In(prs.getInputStream()));
            inThread.start();
            Thread.sleep(2000);
            OutputStream writeTo = prs.getOutputStream();
            writeTo.write("oops\n".getBytes());
            writeTo.flush();
            writeTo.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
    class In implements Runnable {
        private InputStream is;

        public In(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            byte[] b = new byte[1024];
            int size = 0;
            try {
                while ((size = is.read(b)) != -1) {
                    System.err.println(new String(b));
                }
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
