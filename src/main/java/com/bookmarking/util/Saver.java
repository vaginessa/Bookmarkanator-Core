package com.bookmarking.util;

import com.bookmarking.*;
import com.bookmarking.error.*;
import com.bookmarking.ui.*;
import org.apache.logging.log4j.*;

public class Saver implements Runnable
{
    private static final Logger logger = LogManager.getLogger(Saver.class.getCanonicalName());

    private static Saver saver;
    private Object obj;

    enum SaverState
    {
        PAUSE,
        RUN,
        QUIT
    }

    private int saveTime;
    private SaverState saverState;

    public Saver(Object obj)
    {
        this.obj = obj;
        saveTime = 50000;
        saverState = SaverState.PAUSE;
    }

    public int getSaveTime()
    {
        return saveTime;
    }

    public void setSaveTime(int saveTime)
    {
        this.saveTime = saveTime;
    }

    public synchronized SaverState getSaverState()
    {
        return saverState;
    }

    public Object getObj()
    {
        return obj;
    }

    public void setObj(Object obj)
    {
        this.obj = obj;
    }

    public synchronized void pause()
    {
        this.saverState = SaverState.PAUSE;
    }

    public synchronized void start()
    {
        synchronized(obj)
        {
            if (saverState.equals(SaverState.PAUSE))
            {
                this.saverState = SaverState.RUN;
                obj.notify();
            }
        }
    }

    public synchronized void quit()
    {
        this.saverState = SaverState.QUIT;
    }

    @Override
    public void run()
    {
        while (!getSaverState().equals(SaverState.QUIT))
        {
            try
            {
                if (getSaverState().equals(SaverState.PAUSE))
                {
                    synchronized (obj)
                    {
                        obj.wait();
                    }
                }

                // Break sleep up into chunks so that the thread can be stopped in a more timely manner.
                for (int c=0;c<10;c++)
                {
                    Thread.sleep((saveTime/10)+10);
                    if (getSaverState().equals(SaverState.QUIT))
                    {
                        return;
                    }
                }

                logger.info("Saving");

                if (LocalInstance.use().getUIInterface()!=null)
                {
                    LocalInstance.use().getUIInterface().notifyUI("Saving");
                    LocalInstance.use().getUIInterface().setUIState(UIStateEnum.BUSY);
                }

                LocalInstance.use().saveSettings();

                if (LocalInstance.use().getUIInterface()!=null)
                {
                    LocalInstance.use().getUIInterface().notifyUI("Done");
                    LocalInstance.use().getUIInterface().setUIState(UIStateEnum.NEUTRAL);
                    LocalInstance.use().getUIInterface().notifyUI("");
                }

            }
            catch (Exception e)
            {
                ErrorHandler.handle(e);
            }
        }
    }

    public static Saver use()
    {
       return use(null);
    }

    public static Saver use(Object obj)
    {
        if (saver == null)
        {
            saver = new Saver(obj);
            Thread t = new Thread(saver);
            t.start();
        }
        return saver;
    }
}
