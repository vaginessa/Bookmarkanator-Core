package com.bookmarkanator.core;

/**
 * This is a wrapper for the Java Logger class
 */
public class MLog extends SecurityManager
{
//    private static Logger logger;
//    private static MLog mLog;
//    private ConsoleHandler2 consoleHandler;
//
//    //Instance methods
//
//    public void setDisplayLevel(Level level)
//    {
//        consoleHandler.setLevel(level);
//    }
//
//    public static void setLogLevel(Level level)
//    {
//        MLog.use().setDisplayLevel(level);
//    }
//
//    //Logging methods
//    private MLog logAll(String message)
//    {
//        logger.log(Level.ALL, message);
//        return this;
//    }
//
//    private MLog logSevere(String message)
//    {
//        logger.log(Level.SEVERE, message);
//        return this;
//    }
//
//    private MLog logWarn(String message)
//    {
//        logger.log(Level.WARNING, message);
//        return this;
//    }
//
//    private MLog logInfo(String message)
//    {
//        logger.log(Level.INFO, message);
//        return this;
//    }
//
//    private MLog logConfig(String message)
//    {
//        logger.log(Level.CONFIG, message);
//        return this;
//    }
//
//    private MLog logFine(String message)
//    {
//        logger.log(Level.FINE, message);
//        return this;
//    }
//
//    private MLog logFiner(String message)
//    {
//        logger.log(Level.FINER, message);
//        return this;
//    }
//
//    private MLog logFinest(String message)
//    {
//        logger.log(Level.FINEST, message);
//        return this;
//    }
//
//    private MLog logOff(String message)
//    {
//        logger.log(Level.OFF, message);
//        return this;
//    }
//
//    private Logger getTheLogger()
//    {
//        return logger;
//    }
//
//    public void switchLogger()
//    {
//        String className = this.getClassContext()[3].getCanonicalName();
//        MLog.logger = Logger.getLogger(className);
//        logger.setUseParentHandlers(false);
//        logger.setLevel(Level.FINEST);
//        logger.setUseParentHandlers(false);
//        consoleHandler = new ConsoleHandler2();
//        consoleHandler.setLevel(Level.FINEST);
//        logger.addHandler(consoleHandler);
//
////        System.out.println("The class name here: "+);
////        System.out.println("a;lskdjf;alskdjf;laksjdf;lkajsdfk "+ MethodHandles.lookup().lookupClass().getDeclaredClasses());
//    }
//
////    private String getCallingClass()
////    {
////     Class[] classes = this.getClassContext();
////        String thisClassName = this.getClass().getCanonicalName();
////     String ret;
////
////        for (Class clazz: classes)
////        {
////            System.out.println(clazz.getCanonicalName()+" "+thisClassName);
////            if (!clazz.getCanonicalName().equals(thisClassName))
////            {
////                System.out.println("REturning "+clazz.getCanonicalName());
////                return clazz.getCanonicalName();
////            }
////        }
////
////        return "??";
////    }
//
//    //Static methods
//
//    public static Logger getLogger()
//    {
//        return MLog.use().getTheLogger();
//    }
//
//    public static MLog all(String message)
//    {
//        return use().logAll(message);
//    }
//
//    public static MLog severe(String message)
//    {
//        return use().logSevere(message);
//    }
//
//    public static MLog warn(String message)
//    {
//        return use().logWarn(message);
//    }
//
//    public static MLog info(String message)
//    {
//        return use().logInfo(message);
//    }
//
//    public static MLog config(String message)
//    {
//        return use().logConfig(message);
//    }
//
//    public static MLog fine(String message)
//    {
//        return use().logFine(message);
//    }
//
//    public static MLog finer(String message)
//    {
//        return use().logFiner(message);
//    }
//
//    public static MLog finest(String message)
//    {
//        return use().logFinest(message);
//    }
//
//    public static MLog off(String message)
//    {
//        return use().logOff(message);
//    }
//
//    public static MLog use()
//    {
//        if (MLog.mLog == null)
//        {
//            MLog.mLog = new MLog();
//        }
//
//        MLog.mLog.switchLogger();
//        return MLog.mLog;
//    }
//}
//
//class ConsoleHandler2 extends ConsoleHandler
//{
//
//    /**
//     * Create a <tt>ConsoleHandler</tt> for <tt>System.err</tt>.
//     * <p>
//     * The <tt>ConsoleHandler</tt> is configured based on
//     * <tt>LogManager</tt> properties (or their default values).
//     *
//     */
//    public ConsoleHandler2() {
//        super();
//        setOutputStream(System.out);
//    }
//
//    /**
//     * Publish a <tt>LogRecord</tt>.
//     * <p>
//     * The logging request was made initially to a <tt>Logger</tt> object,
//     * which initialized the <tt>LogRecord</tt> and forwarded it here.
//     * <p>
//     * @param  record  description of the log event. A null record is
//     *                 silently ignored and is not published
//     */
//    @Override
//    public void publish(LogRecord record) {
//        super.publish(record);
//        flush();
//    }
}
