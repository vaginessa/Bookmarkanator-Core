package com.bookmarking;

import com.bookmarking.bootstrap.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;

/**
 * Main entry point for the bookmarkanator program. Starts the loading process that will dynamically load
 * all parts of the program. It is used to start the program up, and pretty much dies after that.
 */
public class Start
{
    // The group to use when specifying any class that implements an interface. key = interface class name, value = implementing class.
    public static String INTERFACE_IMPLEMENTATIONS_GROUP = "interface-implementations-group";

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
        // TODO how to properly update the code?
        MainInterface mainInterface = null;

        SettingsIOInterface settingsIOInterface = loadSettingsIOInterface();
        // load the main interface class
        // init module loader
        // init io interface
        // set all the interfaces in the main interface and return.


//        InitInterface initInterface = loadInitInterface(settingsIOInterface);

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
        AbstractSetting initInterfaceClass = this.settings.getSetting(Settings.DEFAULT_SETTINGS_GROUP, InitInterface.class.getCanonicalName());
        ClassSetting setting;

        if (initInterfaceClass instanceof ClassSetting)
        {
            setting = (ClassSetting) initInterfaceClass;
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
        initSettings();

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

    /**
     * Default settings. Here it is mostly used to set default interface implementations in order to find the correct classes to use
     * for each particular part of the program.
     * @return  A settings object containing default settings.
     * @throws Exception
     */
    private Settings getDefaultSettings()
        throws Exception
    {
        Settings res = new Settings();

        ClassSetting classSetting = new ClassSetting(Start.INTERFACE_IMPLEMENTATIONS_GROUP, InitInterface.class.getCanonicalName(), Bootstrap.class);
        res.putSetting(classSetting);

        classSetting = new ClassSetting(Start.INTERFACE_IMPLEMENTATIONS_GROUP, MainInterface.class.getCanonicalName(), LocalInstance.class);
        res.putSetting(classSetting);

        classSetting = new ClassSetting(Start.INTERFACE_IMPLEMENTATIONS_GROUP, SettingsIOInterface.class.getCanonicalName(), SettingsIO.class );
        res.putSetting(classSetting);

        return res;
    }
}
