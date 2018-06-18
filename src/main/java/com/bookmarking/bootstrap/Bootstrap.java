package com.bookmarking.bootstrap;

import java.io.*;
import java.util.*;
import com.bookmarking.*;
import com.bookmarking.file.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.settings.types.*;
import com.bookmarking.ui.*;
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

    // ============================================================
    // Constructors
    // ============================================================

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
        if (settings!=null)
        {
            this.settings = settings;
        }
        return init();
    }

    public MainInterface init(Settings settings, MainUIInterface mainUIInterface)
        throws Exception
    {
        this.mainUIInterface = mainUIInterface;
        return this.init(settings);
    }

    public MainInterface init(MainUIInterface mainUIInterface)
        throws Exception
    {
        this.mainUIInterface = mainUIInterface;
        return this.init();
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

        // SettingsIO defaults
        classSetting = new ClassSetting(Defaults.DEFAULT_CLASSES_GROUP, SettingsIOInterface.class.getCanonicalName(), FileSettingsIO.class);
        res.putSetting(classSetting);

        FileSetting fileSetting = new FileSetting(Defaults.DIRECTORIES_GROUP, Defaults.PRIMARY_FILE_LOCATION_KEY, new File(Defaults.PRIMARY_DIRECTORY+File.separator+Defaults.SETTINGS_FILE_NAME));
        res.putSetting(fileSetting);

        fileSetting = new FileSetting(Defaults.DIRECTORIES_GROUP, Defaults.SECONDARY_FILE_LOCATION_KEY, new File(Defaults.SECONDARY_DIRECTORY+File.separator+Defaults.SETTINGS_FILE_NAME));
        res.putSetting(fileSetting);

        // IOInterface defaults.

        classSetting = new ClassSetting(Defaults.DEFAULT_CLASSES_GROUP, IOInterface.class.getCanonicalName(), FileIO.class);
        res.putSetting(classSetting);

        fileSetting = new FileSetting(Defaults.FILE_IO_SETTINGS_GROUP, Defaults.PRIMARY_FILE_LOCATION_KEY, new File(Defaults.PRIMARY_DIRECTORY+File.separator+Defaults.BOOKMARKS_FILE_NAME));
        res.putSetting(fileSetting);

        fileSetting = new FileSetting(Defaults.FILE_IO_SETTINGS_GROUP, Defaults.SECONDARY_FILE_LOCATION_KEY, new File(Defaults.SECONDARY_DIRECTORY+File.separator+Defaults.BOOKMARKS_FILE_NAME));
        res.putSetting(fileSetting);

        return res;
    }
}
