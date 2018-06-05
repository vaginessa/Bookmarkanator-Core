package com.bookmarking.bootstrap;

import java.io.*;
import java.net.*;
import java.util.*;
import com.bookmarking.*;
import com.bookmarking.file.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.settings.types.*;
import com.bookmarking.ui.*;
import com.bookmarking.ui.defaults.*;
import com.bookmarking.update.*;
import org.apache.logging.log4j.*;

/**
 * Main entry point for the bookmarkanator program. Starts the loading process that will dynamically load
 * all parts of the program.
 */
public class Bootstrap
{
    // ============================================================
    // Static Fields
    // ============================================================

    private static final Logger logger = LogManager.getLogger(Bootstrap.class.getCanonicalName());

    // ============================================================
    // Fields
    // ============================================================

    private Settings settings;
    private MainUIInterface mainUIInterface;
    private SettingsIOInterface settingsIOInterface;
    private IOInterface ioInterface;
    private MainInterface mainInterface;

    public Bootstrap()
    {
        this(null, null);
    }

    public Bootstrap(Settings settings)
    {
        this(settings, null);
    }

    public Bootstrap(MainUIInterface mainUIInterface)
    {
        this(null, mainUIInterface);
    }

    public Bootstrap(Settings settings, MainUIInterface mainUIInterface)
    {
        if (settings != null)
        {
            this.settings = settings;
        }
        else
        {
            this.settings = new Settings();
        }

        if (mainUIInterface != null)
        {
            this.mainUIInterface = mainUIInterface;
        }
        else
        {// Default to logging everything out.
            this.mainUIInterface = new ConsoleUI();
        }

    }

    // ============================================================
    // Methods
    // ============================================================

    public MainInterface init()
        throws Exception
    {
        logger.info("--------------------------------------------------------------");
        logger.info("Bootstrap init");
        logger.info("--------------------------------------------------------------");

        // Load default settings, and then load settings IO interface from settings.
        this.settingsIOInterface = loadSettingsIOInterface();

        // Load io interface.
        this.ioInterface =  loadIOInterface();

        this.mainInterface = loadMainInterface();

        return this.mainInterface;
    }

    public MainInterface init(Settings settings)
        throws Exception
    {
        this.settings = settings;
        return init();
    }

    public Settings getSettings()
    {
        return settings;
    }

    public MainUIInterface getMainUIInterface()
    {
        return mainUIInterface;
    }

    public SettingsIOInterface getSettingsIOInterface()
    {
        return settingsIOInterface;
    }

    public IOInterface getIoInterface()
    {
        return ioInterface;
    }

    public MainInterface getMainInterface()
    {
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
        logger.info("- Loading SettingsIOInterface");
        getOrCreateSettings();

        // Uses default settingsIOClass name or one supplied when starting up.
        AbstractSetting settingsIOClass = this.settings.getSetting(Defaults.DEFAULT_CLASSES_GROUP, SettingsIOInterface.class.getCanonicalName());

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

        logger.info("- Done loading settings IO interface.");
        return settingsIOInterface;
    }

    /**
     * Loads the init interface which will in turn load the IO interface.
     *
     * @return An init interface that has been initialized.
     * @throws Exception
     */
    private IOInterface loadIOInterface()
        throws Exception
    {
        logger.info("- Loading IOInterface");
        AbstractSetting IOInterfaceClass = this.settings.getSetting(Defaults.DEFAULT_CLASSES_GROUP, IOInterface.class.getCanonicalName());
        ClassSetting setting;

        if (IOInterfaceClass instanceof ClassSetting)
        {
            setting = (ClassSetting) IOInterfaceClass;
        }
        else
        {
            throw new Exception("Setting used to specify IOInterface class is not of the correct type. Must be of type ClassSetting.");
        }

        IOInterface ioInterface = ModuleLoader.use().instantiateClass(setting.getValue().getCanonicalName(), IOInterface.class);

        if (this.mainUIInterface != null && this.mainUIInterface.getIOUIInterface()!=null)
        {
            ioInterface.init(this.settingsIOInterface, this.mainUIInterface.getIOUIInterface());
        }
        else
        {
            ioInterface.init(settingsIOInterface);
        }

        logger.info("- Done loading IO interface.");
        return ioInterface;
    }

    private MainInterface loadMainInterface()
        throws Exception
    {
        logger.info("- Loading MainInterface");

        // Uses default MainInterface class name or one supplied when starting up.
        AbstractSetting mainInterfaceClass = this.settings.getSetting(Defaults.DEFAULT_CLASSES_GROUP, MainInterface.class.getCanonicalName());

        // This should not happen but lets check anyway...
        Objects.requireNonNull(mainInterfaceClass, "MainInterfaceClass is not present in settings.");

        ClassSetting setting;

        if (mainInterfaceClass instanceof ClassSetting)
        {
            setting = (ClassSetting) mainInterfaceClass;
        }
        else
        {
            throw new Exception("Setting used to specify MainInterface class is not of the correct type. Must be of type ClassSetting.");
        }

        MainInterface mainInterface = ModuleLoader.use()
            .instantiateClass(setting.getValue().getCanonicalName(), MainInterface.class);

        logger.info("- Done loading main interface.");
        return mainInterface;
    }

    /**
     * Create settings if necessary. Load default settings.
     *
     * @throws Exception
     */
    private void getOrCreateSettings()
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

        // Main interface defaults.
        ClassSetting classSetting = new ClassSetting(Defaults.DEFAULT_CLASSES_GROUP, MainInterface.class.getCanonicalName(), LocalInstance.class);
        res.putSetting(classSetting);

        // FileSettingsIO defaults
        classSetting = new ClassSetting(Defaults.DEFAULT_CLASSES_GROUP, SettingsIOInterface.class.getCanonicalName(), FileSettingsIO.class);
        res.putSetting(classSetting);

        FileSetting fileSetting = new FileSetting(Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SETTINGS_FILE_LOCATION_KEY, new File("."));
        res.putSetting(fileSetting);

        StringSetting stringSetting = new StringSetting(Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SETTINGS_FILE_NAME_KEY, "settings.xml");
        res.putSetting(stringSetting);

        fileSetting = new FileSetting(
            Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SECONDARY_SETTINGS_FILE_LOCATION_KEY, new File(System.getProperty("user.home")+File.separatorChar+"Bookmarkanator"));
        res.putSetting(fileSetting);

        stringSetting = new StringSetting(Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SECONDARY_SETTINGS_FILE_NAME_KEY, "settings.xml");
        res.putSetting(stringSetting);

        // Updater default class.
        classSetting = new ClassSetting(Defaults.DEFAULT_CLASSES_GROUP, UpdaterInterface.class.getCanonicalName(), WebUpdater.class);
        res.putSetting(classSetting);

        // Updater repository config - Zero will be the first repository it will try.
        URLSetting urlSetting = new URLSetting(WebUpdater.WEB_UPDATER_REPOSITORIES_GROUP_KEY, String.valueOf(0), new URL("http://localhost:8080/bookmakanator/update"));
        res.putSetting(urlSetting);

        stringSetting = new StringSetting(WebUpdater.UPDAT_CONFIG_SETTINGS_GROUP_KEY, String.valueOf(0), "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><updateConfigEntry><currentResource>file:.</currentResource><currentVersion>0.0.0-2</currentVersion><resourceKey>desktop</resourceKey></updateConfigEntry>");
        res.putSetting(stringSetting);

        // IOInterface defaults.
        classSetting = new ClassSetting(Defaults.DEFAULT_CLASSES_GROUP, IOInterface.class.getCanonicalName(), FileIO.class);
        res.putSetting(classSetting);

        return res;
    }
}
