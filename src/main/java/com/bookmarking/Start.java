package com.bookmarking;

import com.bookmarking.bootstrap.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;

/**
 * Main entry point for the bookmarkanator program. Starts the loading process that will dynamically load
 * all parts of the program. It is used to start the program up, and pretty much dies after that.
 */
public class Start
{
    private Settings settings;
    private MainUIInterface mainUIInterface;

    public Start()
    {
        this(null, null);
    }

    public Start(Settings settings)
    {
        this(settings, null);
    }

    public Start(MainUIInterface mainUIInterface)
    {
        this(null, mainUIInterface);
    }

    public Start(Settings settings, MainUIInterface mainUIInterface)
    {
        if (settings!=null)
        {
            this.settings = settings;
        }

        if (mainUIInterface!=null)
        {
            this.mainUIInterface = mainUIInterface;
        }

    }

    public MainInterface init()
        throws Exception
    {
        MainInterface mainInterface = null;

        initSettings();

        SettingsIOInterface settingsIOInterface = loadSettingsIOInterface();
        InitInterface initInterface = loadInitInterface(settingsIOInterface);



        // load modules
        // load remaining settings
        // load io interface

        return mainInterface;
    }

    // ============================================================
    // Private Methods
    // ============================================================

    /**
     * Loads the init interface which will in turn load the IO interface.
     * @return  An init interface that has been initialized.
     * @throws Exception
     */
    private InitInterface loadInitInterface(SettingsIOInterface settingsIOInterface)
        throws Exception
    {
        AbstractSetting settingsIOClass = this.settings.getSetting(Settings.DEFAULT_SETTINGS_GROUP, SettingsIOInterface.class.getCanonicalName());
        ClassSetting setting;

        if (settingsIOClass instanceof ClassSetting)
        {
            setting = (ClassSetting) settingsIOClass;
        }
        else
        {
            throw new Exception("Setting used to specify InitInterface is not of the correct type. Must be of type ClassSetting.");
        }

        InitInterface initInterface = ModuleLoader.use().instantiateClass(setting.getValue().getCanonicalName(), InitInterface.class);

        if (this.mainUIInterface != null)
        {
            initInterface.init(settingsIOInterface, this.mainUIInterface.getInitUIInterface());
        }
        else
        {
            initInterface.init(settingsIOInterface, null);
        }

        return initInterface;
    }

    /**
     * Loads the SettingsIOInterface class specified in the settings. Calls init(Settings) on it and returns.
     * @return SettingsIOInterface  The settings interface that will be used to load further settings.
     * @throws Exception
     */
    private SettingsIOInterface loadSettingsIOInterface()
        throws Exception
    {
        AbstractSetting settingsIOClass = this.settings.getSetting(Settings.DEFAULT_SETTINGS_GROUP, SettingsIOInterface.class.getCanonicalName());
        ClassSetting setting;

        if (settingsIOClass instanceof ClassSetting)
        {
            setting = (ClassSetting) settingsIOClass;
        }
        else
        {
            throw new Exception("Setting used to specify SettingsIOInterface is not of the correct type. Must be of type ClassSetting.");
        }

        SettingsIOInterface settingsIOInterface = ModuleLoader.use().instantiateClass(setting.getValue().getCanonicalName(), SettingsIOInterface.class);

        this.settings = settingsIOInterface.init(this.settings);

        return settingsIOInterface;
    }

    /**
     * Create settings if necessary. Load default settings.
     * @throws Exception
     */
    private void initSettings()
        throws Exception
    {
        if (settings==null)
        {
            settings = getDefaultSettings();
        }

        settings.importSettings(getDefaultSettings());
    }

    private Settings getDefaultSettings()
        throws Exception
    {
        Settings res = new Settings();

        // ---------------------------
        // Default class settings
        // ---------------------------

        ClassSetting classSetting = new ClassSetting(Settings.DEFAULT_SETTINGS_GROUP, IOInterface.class.getCanonicalName(), Bootstrap.class);
        res.putSetting(classSetting);

        classSetting = new ClassSetting(Settings.DEFAULT_SETTINGS_GROUP, MainInterface.class.getCanonicalName(), LocalInstance.class);
        res.putSetting(classSetting);

        // ---------------------------
        // Default file location settings
        // ---------------------------

        return res;
    }
}
