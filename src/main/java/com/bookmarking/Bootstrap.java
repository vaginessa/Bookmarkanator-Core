package com.bookmarking;

import java.io.*;
import java.util.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.io.*;
import org.apache.logging.log4j.*;

/**
 * This class is responsible for doing the initial settings, and class loading.
 */
public class Bootstrap
{
    // Static fields
    private static final Logger logger = LogManager.getLogger(Bootstrap.class.getCanonicalName());
    private static Bootstrap bootstrap;

    // Default names
    private static final String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    private static final String DEFAULT_SETTINGS_DIRECTORY = "Bookmark-anator";

    // Settings field keys
    private static final String MODULE_LOCATIONS_KEY = "module-locations";
    public static final String OVERRIDDEN_CLASSES = "overridden-classes";
    public static final String BKIO_CONFIGS = "bookmark-io-interface-configs";

    // Fields
    private IOInterface IOInterface;
    private File settingsFile;

    public Bootstrap()
        throws Exception
    {
        // Get settings file
        File currentDir = new File(".");
        logger.info("Current directory " + currentDir.getCanonicalPath());
        logger.info("Locating settings file...");
        settingsFile = locateSettingsDirectory();

        // Configure the global settings object
        GlobalSettings.use().setFile(settingsFile);
        GlobalSettings.use().readFromDisk();

        // Add default settings
        GlobalSettings.use().getSettings().importSettings(getDefaultSettings());

        // Track the classes that can be overridden externally...
        ModuleLoader.use().addClassToTrack(AbstractBookmark.class);
        ModuleLoader.use().addClassToTrack(IOInterface.class);

        Set<SettingItem> moduleLocations = GlobalSettings.use().getSettings().getByType(Bootstrap.MODULE_LOCATIONS_KEY);

        if (moduleLocations != null)
        {// Add jars and then locate tracked classes
            ModuleLoader.use().addModulesToClasspath(Settings.extractValues(moduleLocations));
        }
        else
        {// Locate the tracked classes
            ModuleLoader.use().addModulesToClasspath();
        }

        this.IOInterface = loadBKIOInterface();

        // Give bookmark access to the message board
        MessageBoard messageBoard = MessageBoard.use();
        for (AbstractBookmark abs : this.IOInterface.getAllBookmarks())
        {
            messageBoard.setSecretKey(abs);
        }
    }

    // ============================================================
    // Methods
    // ============================================================

    public Settings getSettings()
    {
        return GlobalSettings.use().getSettings();
    }

    public void saveSettingsFile()
        throws Exception
    {
        GlobalSettings.use().writeToDisk();
    }

    public String getSettingsFile()
        throws IOException
    {
        return settingsFile.getCanonicalPath();
    }

    // ============================================================
    // Private Methods
    // ============================================================

    /**
     * Loads the BKIOInterface class that is specified in the settings file, or the default setting added to the settings file.
     *
     * @return A BKIOInterface class that was loaded.
     * @throws Exception
     */
    private IOInterface loadBKIOInterface()
        throws Exception
    {
        Set<Class> classes = ModuleLoader.use().getClassesLoaded(IOInterface.class);

        for (Class clazz : classes)
        {//Iterate through bkio classes found, selecting the correct one based on settings.
            try
            {
                logger.trace("Attempting to load bookmark io interface \"" + clazz.getCanonicalName() + "\"");
                //Attempting to load the config setting for this class
                SettingItem configSetting = GlobalSettings.use().getSettings().getSetting(Bootstrap.BKIO_CONFIGS, clazz.getCanonicalName());
                String config = null;

                if (configSetting != null)
                {
                    config = configSetting.getValue();
                }
                else
                {
                    config = "";
                }

                IOInterface bkio2 = ModuleLoader.use().loadClass(clazz.getCanonicalName(), IOInterface.class);

                logger.info(
                    "Loaded BKIOInterface class: \"" + clazz + "\" with this config: \"" + (config.isEmpty() ? "[no config found]" : config) + "\"");
                logger.info("Calling init()...");
                bkio2.init(config);
                logger.info("Done.");

                return bkio2;
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
            {
                //Print the error and move on to try loading the next class.
                e.printStackTrace();
            }
        }

        throw new Exception("No valid BKIOInterface classes could be loaded.");
    }

    /**
     * Returns a map with default settings, in case there is no settings file (or an empty one), and also for comparing setting by setting to use
     * the default setting if the setting is not specified in the settings file.
     *
     * @return A map of settings in the form of <Setting key, List<individual multiple settings>>
     * @throws FileNotFoundException If the default base directory cannot be accessed.
     */
    private Settings getDefaultSettings()
        throws Exception
    {
        Settings res = new Settings();

        //        SettingItem settingItem = new SettingItem(FileIO.class.getCanonicalName());
        //        settingItem.setValue(getDefaultBookmarkFile());
        //        settingItem.setType(Bootstrap.BKIO_CONFIGS);
        //        res.putSetting(settingItem);

        return res;
    }

    private File locateSettingsDirectory()
        throws Exception
    {
        // Try [current dir]/Settings.xml
        File file = new File(getCurrentDirSettingsFile());

        if (file.exists())
        {
            logger.info("Using settings file at " + file.getCanonicalPath());
            return file;
        }

        // Try [current dir]/Bookmark-anator/Settings.xml
        file = new File(getCurrentDirSettingsFileInBKFolder());

        if (file.exists())
        {
            logger.info("Using settings file at " + file.getCanonicalPath());
            return file;
        }

        // Try [user home]/Bookmark-anator/Settings.xml
        file = new File(getDefaultHomeDirSettingsFileInBKFolder());

        if (file.exists())
        {
            logger.info("Using settings file at " + file.getCanonicalPath());
            return file;
        }

        // Try [user home]/settings/Settings.xml
        file = new File(getDefaultHomeDirSettingsFile());

        if (file.exists())
        {
            logger.info("Using settings file at " + file.getCanonicalPath());
            return file;
        }

        // Default to [user home]/Bookmark-anator/Settings.xml and let the settings engine handle creating the file if necessary.
        file = new File(getDefaultHomeDirSettingsFileInBKFolder());
        logger.info("No settings files exist at this time. Defaulting to " + file.getCanonicalPath());

        return file;
    }

    private String getCurrentDirSettingsFile()
    {
        String directory = System.getProperty("user.dir");
        return directory + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_FILE_NAME;
    }

    private String getCurrentDirSettingsFileInBKFolder()
    {
        String directory = System.getProperty("user.dir");
        return directory + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_DIRECTORY + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_FILE_NAME;
    }

    private String getDefaultHomeDirSettingsFile()
    {
        String directory = System.getProperty("user.home");
        return directory + File.separatorChar + "settings" + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_FILE_NAME;
    }

    private String getDefaultHomeDirSettingsFileInBKFolder()
    {
        String directory = System.getProperty("user.home");
        return directory + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_DIRECTORY + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_FILE_NAME;
    }

    // ============================================================
    // Static Methods
    // ============================================================

    public static IOInterface IOInterface()
    {
        return Bootstrap.use().IOInterface;
    }

    public static IOInterface context()
        throws Exception
    {
        return Bootstrap.use().IOInterface;
    }

    public static Bootstrap use()
    {
        if (bootstrap == null)
        {
            try
            {
                bootstrap = new Bootstrap();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return bootstrap;
    }
}
