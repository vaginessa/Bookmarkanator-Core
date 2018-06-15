package com.bookmarking;

import java.util.*;
import com.bookmarking.bootstrap.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.ui.*;

/**
 * The local instance is the interface implementation intended for use on a local system such as a users desktop computer.
 */
public class LocalInstance implements MainInterface
{
    // ============================================================
    // Fields
    // ============================================================

    private static LocalInstance localInstance;

    // Interfaces
    private Bootstrap initInterface;
    private UIInterface uiInterface;

    // Other
    private MainSettings mainSettings;

    public void init()
        throws Exception
    {
        initInterface = new Bootstrap();
        initInterface.init();
    }

    public void init(Settings settings)
        throws Exception
    {
        initInterface = new Bootstrap();
        initInterface.init(settings);
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
    public Bootstrap getStartClass()
    {
        return null;
    }

    @Override
    public void setStartClass(Bootstrap bootstrap)
    {

    }

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
        return this.initInterface.getIoInterface();
    }

    @Override
    public MainSettings getSettings()
    {
        if (mainSettings == null)
        {
            MainSettings mSettings = new MainSettings();
            mSettings.setMainSettings((Settings) getFileService().getFileSync(Defaults.SETTINGS_FILE_CONTEXT).getObject());
            if (this.getIOInterface() != null)
            {
                mSettings.setIoSettings((Settings) getFileService().getFileSync(Defaults.SETTINGS_FILE_CONTEXT).getObject());
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
        this.getFileService().getFileSync(Defaults.SETTINGS_FILE_CONTEXT).setObjectToWrite(mainSettings.getMainSettings());
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
            FileSync<Settings> mainS = getFileService().getFileSync(Defaults.SETTINGS_FILE_CONTEXT);
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

//    public static LocalInstance use(InitUIInterface initUIInterface)
//        throws Exception
//    {
//        if (localInstance == null)
//        {
//            localInstance = new LocalInstance();
//            localInstance.init(initUIInterface);
//        }
//        return localInstance;
//    }
//
//    public static LocalInstance use(Settings settings, InitUIInterface initUIInterface)
//        throws Exception
//    {
//        if (localInstance == null)
//        {
//            localInstance = new LocalInstance();
//            localInstance.init(settings, initUIInterface);
//        }
//        return localInstance;
//    }
}
