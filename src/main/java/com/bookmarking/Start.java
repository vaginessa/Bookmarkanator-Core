package com.bookmarking;

import java.util.*;
import com.bookmarking.bootstrap.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.update.*;
import org.apache.logging.log4j.*;

/**
 * Main entry point for the bookmarkanator program. Starts the loading process that will dynamically load
 * all parts of the program. It is used to start the program up, and pretty much dies after that.
 */
public class Start
{
    // ============================================================
    // Static Fields
    // ============================================================

    private static final Logger logger = LogManager.getLogger(Start.class.getCanonicalName());

    /**
     * The group that is used to specify implementing classes, or overriding classes.
     * <p>
     * For example you can only have a single IOInterface implementation loaded at a time. So you would add an entry here specifying the key as the
     * interface class, and the value as the implementation you want to load for that interface.
     * <p>
     * If you want to replace a class with another one, say class 'A' with class 'B', you would add an entry with key 'A' and the value would be
     * 'B'. When the module loader is asked for class 'A' it would check this value, and return class 'B' instead.
     */
    public static final String DEFAULT_CLASSES_GROUP = "default-classes";

    // ============================================================
    // Fields
    // ============================================================

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
        if (settings != null)
        {
            this.settings = settings;
        }

        if (mainUIInterface != null)
        {
            this.mainUIInterface = mainUIInterface;
        }

    }

    public MainInterface init()
        throws Exception
    {
        MainInterface mainInterface = null;

        SettingsIOInterface settingsIOInterface = loadSettingsIOInterface();

        UpdateServiceInterface updateServiceInterface = loadUpdaterInterface();

        InitInterface ioInterface =  loadInitInterface(settingsIOInterface);


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
     * Loads the SettingsIOInterface class specified in the settings. Calls init(Settings) on it and returns.
     *
     * @return SettingsIOInterface  The settings interface that will be used to load further settings.
     * @throws Exception
     */
    private SettingsIOInterface loadSettingsIOInterface()
        throws Exception
    {
        initSettings();

        // Uses default settingsIOClass name or one supplied when starting up Start...
        AbstractSetting settingsIOClass = this.settings.getSetting(Start.DEFAULT_CLASSES_GROUP, SettingsIOInterface.class.getCanonicalName());

        // This should not happen but lets check anyway...
        Objects.requireNonNull(settingsIOClass, "SettingsIOClass is not present in settings.");

        ClassSetting setting;

        if (settingsIOClass instanceof ClassSetting)
        {
            setting = (ClassSetting) settingsIOClass;
        }
        else
        {
            throw new Exception("Setting used to specify SettingsIOInterface class is not of the correct type. Must be of type ClassSetting.");
        }

        // Load the settingsIO class and merge it with the current (default) settings.
        SettingsIOInterface settingsIOInterface = ModuleLoader.use()
            .instantiateClass(setting.getValue().getCanonicalName(), SettingsIOInterface.class);

        this.settings = settingsIOInterface.init(this.settings);

        return settingsIOInterface;
    }


    private UpdaterInterface loadUpdaterInterface()
        throws Exception
    {
        AbstractSetting updaterClass = this.settings.getSetting(Start.DEFAULT_CLASSES_GROUP, UpdaterInterface.class.getCanonicalName());

        // This should not happen because of default settings but lets check anyway...
        Objects.requireNonNull(updaterClass, "UpdatesInterface class setting is not present in settings.");

        ClassSetting setting;

        if (updaterClass instanceof ClassSetting)
        {
            setting = (ClassSetting) updaterClass;
        }
        else
        {
            throw new Exception("Setting used to specify SettingsIOInterface class is not of the correct type. Must be of type ClassSetting.");
        }

        UpdaterInterface updateServiceInterface = ModuleLoader.use()
            .instantiateClass(setting.getValue().getCanonicalName(),UpdaterInterface.class);

        Set<UpdateConfigEntry> updates = updateServiceInterface.checkForUpdates(this.settings);
        updateServiceInterface.performUpdates(updates);

        return updateServiceInterface;
    }

    /**
     * Loads the init interface which will in turn load the IO interface.
     *
     * @return An init interface that has been initialized.
     * @throws Exception
     */
    private IOInterface loadInitInterface(SettingsIOInterface settingsIOInterface)
        throws Exception
    {
        AbstractSetting initInterfaceClass = this.settings.getSetting(Start.DEFAULT_CLASSES_GROUP, IOInterface.class.getCanonicalName());
        ClassSetting setting;

        if (initInterfaceClass instanceof ClassSetting)
        {
            setting = (ClassSetting) initInterfaceClass;
        }
        else
        {
            throw new Exception("Setting used to specify IOInterface class is not of the correct type. Must be of type ClassSetting.");
        }

        IOInterface ioInterface = ModuleLoader.use().instantiateClass(setting.getValue().getCanonicalName(), IOInterface.class);

        if (this.mainUIInterface != null && this.mainUIInterface.getInitUIInterface()!=null)
        {
            ioInterface.init(settingsIOInterface, this.mainUIInterface.getInitUIInterface().getIOUIInterface());
        }
        else
        {
            ioInterface.init(settingsIOInterface);
        }

        return ioInterface;
    }


    /**
     * Create settings if necessary. Load default settings.
     *
     * @throws Exception
     */
    private void initSettings()
        throws Exception
    {
        if (settings == null)
        {
            settings = getDefaultSettings();
        }

        settings.importSettings(getDefaultSettings());
    }

    /**
     * Default settings. Here it is mostly used to set default interface implementations in order to find the correct classes to use
     * for each particular part of the program.
     *
     * @return A settings object containing default settings.
     * @throws Exception
     */
    private Settings getDefaultSettings()
        throws Exception
    {
        Settings res = new Settings();

        ClassSetting classSetting = new ClassSetting(Start.DEFAULT_CLASSES_GROUP, InitInterface.class.getCanonicalName(), Bootstrap.class);
        res.putSetting(classSetting);

        classSetting = new ClassSetting(Start.DEFAULT_CLASSES_GROUP, MainInterface.class.getCanonicalName(), LocalInstance.class);
        res.putSetting(classSetting);

        classSetting = new ClassSetting(Start.DEFAULT_CLASSES_GROUP, SettingsIOInterface.class.getCanonicalName(), SettingsIO.class);
        res.putSetting(classSetting);

        return res;
    }
}
