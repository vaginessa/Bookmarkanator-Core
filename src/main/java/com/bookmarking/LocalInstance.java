package com.bookmarking;

import java.util.*;
import com.bookmarking.bootstrap.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.ui.*;

public class LocalInstance implements MainInterface
{
    // ============================================================
    // Fields
    // ============================================================

    private static LocalInstance localInstance;

    // Interfaces
//    private Bootstrap initInterface;
    private IOInterface ioInterface;
    private UIInterface uiInterface;

    // Other
    private MainSettings mainSettings;

    public void init()
        throws Exception
    {
//        initInterface = new Bootstrap();
//        initInterface.init();
    }

    public void init(Settings settings)
        throws Exception
    {
//        initInterface = new Bootstrap();
//        initInterface.init(settings);
    }

//    public void init(InitUIInterface initUIInterface)
//        throws Exception
//    {
//        initInterface = new Bootstrap();
//        initInterface.init();
//        initInterface.setInitUIInterface(initUIInterface);
//    }
//
//    public void init(Settings settings, InitUIInterface initUIInterface)
//        throws Exception
//    {
//        initInterface = new Bootstrap();
//        initInterface.init(settings);
//        initInterface.setInitUIInterface(initUIInterface);
//    }

    @Override
    public void exit()
        throws Exception
    {
        this.saveSettings();
    }

    @Override
    public Start getStartClass()
    {
        return null;
    }

    @Override
    public void setStartClass(Start start)
    {

    }

    //    @Override
//    public InitInterface getInitInterface()
//    {
//        return initInterface;
//    }

    @Override
    public ModuleLoader getModuleLoader()
    {
        return ModuleLoader.use();
    }

    @Override
    public FileService getFileService()
    {
        return FileService.use();
    }

    @Override
    public IOInterface getIOInterface()
    {
//        return this.initInterface.getIOInterface();
        return null;
    }

    @Override
    public MainSettings getSettings()
    {
        if (mainSettings == null)
        {
            MainSettings mSettings = new MainSettings();
            mSettings.setMainSettings((Settings) getFileService().getFileSync(MainInterface.SETTINGS_FILE_CONTEXT).getObject());
            if (this.ioInterface != null)
            {
                mSettings.setIoSettings((Settings) getFileService().getFileSync(IOInterface.SETTINGS_FILE_CONTEXT).getObject());
            }
            this.mainSettings = mSettings;
        }

        return this.mainSettings;
    }

    @Override
    public void setSettings(MainSettings mainSettings)
        throws Exception
    {
        this.mainSettings = mainSettings;
        this.getFileService().getFileSync(MainInterface.SETTINGS_FILE_CONTEXT).setObjectToWrite(mainSettings.getMainSettings());
        if (this.getIOInterface() != null && mainSettings.getIoSettings() != null)
        {
            getIOInterface().setSettings(mainSettings.getIoSettings());
        }
    }

    @Override
    public MainSettings saveSettings()
        throws Exception
    {
        if (mainSettings!=null)
        {
            FileSync<Settings> mainS = getFileService().getFileSync(MainInterface.SETTINGS_FILE_CONTEXT);
            mainS.writeToDisk();
            mainSettings.setMainSettings(mainS.getObject());

            if (getIOInterface()!=null)
            {
                getIOInterface().setSettings(mainSettings.getIoSettings());
                getIOInterface().save();
                this.mainSettings.setIoSettings(getIOInterface().getSettings());
            }
        }

        return this.mainSettings;
    }

    @Override
    public MainSettings saveSettings(MainSettings mainSettings)
        throws Exception
    {
        Objects.requireNonNull(mainSettings);
        this.mainSettings = mainSettings;
        return saveSettings();
    }

    @Override
    public void setUIInterface(UIInterface uiInterface)
        throws Exception
    {
        Objects.requireNonNull(uiInterface);
        this.uiInterface = uiInterface;
    }

    @Override
    public UIInterface getUIInterface()
    {
        return this.uiInterface;
    }

    @Override
    public void undo()
        throws Exception
    {
        // TODO undo IOInterface
        // TODO undo Main Settings changes.
    }

    @Override
    public void redo()
        throws Exception
    {
        // TODO redo IOInterface
        // TODO redo Main Settings changes.
    }

    @Override
    public void clearUndoRedoStack()
        throws Exception
    {
        // TODO Implement clear stack.
    }

    // ============================================================
    // Static Methods
    // ============================================================

    public static LocalInstance use()
        throws Exception
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init();
        }
        return localInstance;
    }

    public static LocalInstance use(Settings settings)
        throws Exception
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init(settings);
        }
        return localInstance;
    }

    public static LocalInstance use(InitUIInterface initUIInterface)
        throws Exception
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
//            localInstance.init(initUIInterface);
        }
        return localInstance;
    }

    public static LocalInstance use(Settings settings, InitUIInterface initUIInterface)
        throws Exception
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
//            localInstance.init(settings, initUIInterface);
        }
        return localInstance;
    }
}
