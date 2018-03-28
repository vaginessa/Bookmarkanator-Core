package com.bookmarking;

import java.io.*;
import java.util.*;
import com.bookmarking.file.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.update.*;
import org.apache.logging.log4j.*;

/**
 * Main entry point for the bookmarkanator program. Starts the loading process that will dynamically load
 * all parts of the program.
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
    public static final String DEFAULT_CLASSES_GROUP = "default-classes-group";
    public static final String IMPLEMENTING_CLASSES_GROUP = "implementing-classes-group";

    // ============================================================
    // Fields
    // ============================================================

    private Settings settings;
    private MainUIInterface mainUIInterface;
    private SettingsIOInterface settingsIOInterface;
    private UpdaterInterface updaterInterface;
    private IOInterface ioInterface;
    private MainInterface mainInterface;

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
        logger.debug("Start class constructor. MainUIInterface = \n"+mainUIInterface+"\n. Settings =  \n"+settings+"\n");
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
        logger.info("--------------------------------------------------------------");
        logger.info("Start init");
        logger.info("--------------------------------------------------------------");

        // Load default settings, and then load settings IO interface from settings.
        this.settingsIOInterface = loadSettingsIOInterface();

        // Check for and apply updates
        this.updaterInterface = loadUpdaterInterface();

        // Load io interface.
        this.ioInterface =  loadIOInterface();

        this.mainInterface = loadMainInterface();

        return this.mainInterface;
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

        logger.info("- Done.");
        return settingsIOInterface;
    }

    private UpdaterInterface loadUpdaterInterface()
        throws Exception
    {
        logger.info("- Loading updater interface");
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

        if (mainUIInterface!=null && mainUIInterface.getUpdateUIInterface()!=null)
        {
            updateServiceInterface.setUpdateUIInterface(mainUIInterface.getUpdateUIInterface());
        }

        // At this point if the UpdateUIInterface is present it will have already been given the chance to have a say on the updates so just do them.
        updateServiceInterface.performUpdates(updateServiceInterface.checkForUpdates(this.settings));

        logger.info("- Done.");
        return updateServiceInterface;
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
        AbstractSetting IOInterfaceClass = this.settings.getSetting(Start.DEFAULT_CLASSES_GROUP, IOInterface.class.getCanonicalName());
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

        logger.info("- Done.");
        return ioInterface;
    }

    private MainInterface loadMainInterface()
        throws Exception
    {
        logger.info("- Loading MainInterface");

        // Uses default MainInterface class name or one supplied when starting up Start...
        AbstractSetting mainInterfaceClass = this.settings.getSetting(Start.DEFAULT_CLASSES_GROUP, MainInterface.class.getCanonicalName());

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

        logger.info("- Done.");
        return mainInterface;
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

        ClassSetting classSetting = new ClassSetting(Start.DEFAULT_CLASSES_GROUP, MainInterface.class.getCanonicalName(), LocalInstance.class);
        res.putSetting(classSetting);

        // Setting the default to FileSettingsIO
        classSetting = new ClassSetting(Start.DEFAULT_CLASSES_GROUP, SettingsIOInterface.class.getCanonicalName(), FileSettingsIO.class);
        res.putSetting(classSetting);

        FileSetting fileSetting = new FileSetting(FileSettingsIO.FILE_SETTINGS_GROUP_KEY, FileSettingsIO.DEFAULT_SETTINGS_FILE_LOCATION_KEY, new File("."));
        res.putSetting(fileSetting);

        StringSetting stringSetting = new StringSetting(FileSettingsIO.FILE_SETTINGS_GROUP_KEY, FileSettingsIO.DEFAULT_SETTINGS_FILE_NAME_KEY, "settings.xml");
        res.putSetting(stringSetting);

        fileSetting = new FileSetting(FileSettingsIO.FILE_SETTINGS_GROUP_KEY, FileSettingsIO.DEFAULT_SECONDARY_SETTINGS_FILE_LOCATION_KEY, new File(System.getProperty("user.home")+File.separatorChar+"Bookmarkanator"));
        res.putSetting(fileSetting);

        stringSetting = new StringSetting(FileSettingsIO.FILE_SETTINGS_GROUP_KEY, FileSettingsIO.DEFAULT_SECONDARY_SETTINGS_FILE_NAME_KEY, "settings.xml");
        res.putSetting(stringSetting);

        classSetting = new ClassSetting(Start.DEFAULT_CLASSES_GROUP, UpdaterInterface.class.getCanonicalName(), WebUpdater.class);
        res.putSetting(classSetting);

        classSetting = new ClassSetting(Start.DEFAULT_CLASSES_GROUP, IOInterface.class.getCanonicalName(), FileIO.class);
        res.putSetting(classSetting);

        return res;
    }
}
