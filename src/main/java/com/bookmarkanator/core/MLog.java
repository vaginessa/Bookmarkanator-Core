package com.bookmarkanator.core;

import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

/**
 * This is a wrapper for the Java Logger class
 */
public class MLog extends SecurityManager
{
    private static Logger logger;
    private static MLog mLog;
    private ConsoleHandler2 consoleHandler;

    //Instance methods

    public void setDisplayLevel(Level level)
    {
        consoleHandler.setLevel(level);
    }

    public static void setLogLevel(Level level)
    {
        MLog.use().setDisplayLevel(level);
    }

    //Logging methods
    private MLog logAll(String message)
    {
        logger.log(Level.ALL, message);
        return this;
    }

    private MLog logSevere(String message)
    {
        logger.log(Level.SEVERE, message);
        return this;
    }

    private MLog logWarn(String message)
    {
        logger.log(Level.WARNING, message);
        return this;
    }

    private MLog logInfo(String message)
    {
        logger.log(Level.INFO, message);
        return this;
    }

    private MLog logConfig(String message)
    {
        logger.log(Level.CONFIG, message);
        return this;
    }

    private MLog logFine(String message)
    {
        logger.log(Level.FINE, message);
        return this;
    }

    private MLog logFiner(String message)
    {
        logger.log(Level.FINER, message);
        return this;
    }

    private MLog logFinest(String message)
    {
        logger.log(Level.FINEST, message);
        return this;
    }

    private MLog logOff(String message)
    {
        logger.log(Level.OFF, message);
        return this;
    }

    private Logger getTheLogger()
    {
        return logger;
    }

    public void switchLogger()
    {
        String className = getCallingClassName();
        MLog.logger = Logger.getLogger(this.getClass().getCanonicalName());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.FINEST);
        consoleHandler = new ConsoleHandler2(className);
        consoleHandler.setLevel(Level.FINEST);
        consoleHandler.setFormatter(new CustomFormatter());

        logger.addHandler(consoleHandler);
    }

    private String getCallingClassName()
    {
        String className = this.getClassContext()[4].getName();//.getClass().getCanonicalName();

        String[] split = className.split("\\.");
        String res = "";

        for (int c=0;c<split.length;c++)
        {
            String s = split[c];

            if (c==split.length-1)
            {
                res = res.concat(s);
            }
            else
            {
                if (!s.isEmpty())
                {
                    res = res.concat(""+s.charAt(0));
                    res = res.concat(".");
                }
            }
        }

        return res;
    }

    //Static methods

    public static Logger getLogger()
    {
        return MLog.use().getTheLogger();
    }

    public static MLog all(String message)
    {
        return use().logAll(message);
    }

    public static MLog severe(String message)
    {
        return use().logSevere(message);
    }

    public static MLog warn(String message)
    {
        return use().logWarn(message);
    }

    public static MLog info(String message)
    {
        return use().logInfo(message);
    }

    public static MLog config(String message)
    {
        return use().logConfig(message);
    }

    public static MLog fine(String message)
    {
        return use().logFine(message);
    }

    public static MLog finer(String message)
    {
        return use().logFiner(message);
    }

    public static MLog finest(String message)
    {
        return use().logFinest(message);
    }

    public static MLog off(String message)
    {
        return use().logOff(message);
    }

    public static MLog use()
    {
        if (MLog.mLog == null)
        {
            MLog.mLog = new MLog();
        }

        MLog.mLog.switchLogger();
        return MLog.mLog;
    }
}

/**
 * Overridden ConsoleHandler class.
 *
 * Overridden so as to print output to the standard output stream and not the error stream.
 */
class ConsoleHandler2 extends ConsoleHandler
{
    private String className;
    public ConsoleHandler2(String s)
    {
        super();
        className = s;
        setOutputStream(System.out);
    }

    /**
     * Publish a <tt>LogRecord</tt>.
     * <p>
     * The logging request was made initially to a <tt>Logger</tt> object,
     * which initialized the <tt>LogRecord</tt> and forwarded it here.
     * <p>
     * @param  record  description of the log event. A null record is
     *                 silently ignored and is not published
     */
    @Override
    public void publish(LogRecord record) {
        record.setSourceClassName(this.className);
        super.publish(record);
        flush();
    }
}

/**
 * Overridden Formatter class.
 *
 * Overridden for the purpose of changing the output format of the log messages.
 */
class CustomFormatter extends Formatter
{
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    @Override
    public String format(LogRecord record)
    {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append(" - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }
}
