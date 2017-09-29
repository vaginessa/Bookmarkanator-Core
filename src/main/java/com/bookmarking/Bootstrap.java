package com.bookmarking;

import java.io.*;
import java.util.*;
import com.bookmarking.action.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import com.bookmarking.settings.*;
import com.bookmarking.ui.*;
import org.apache.logging.log4j.*;

/**
 * This class is responsible for doing the initial settings, and class loading.
 */
public class Bootstrap
{
    // The user interface that this class can interact with to show status and post messages.
    private BootstrapUIInterface uiInterface;

    // Static fields
    private static final Logger logger = LogManager.getLogger(Bootstrap.class.getCanonicalName());
    private static Bootstrap bootstrap;

    // Default names
    private static final String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    private static final String DEFAULT_SETTINGS_DIRECTORY = "Bookmarkanator";

    // Settings field keys
    private static final String MODULE_LOCATIONS_KEY = "module-locations";
    public static final String OVERRIDDEN_CLASSES = "overridden-classes";
    public static final String DEFAULT_CLASSES_GROUP_NAME = "default-classes";
    public static final String BKIO_CONFIGS = "bookmark-io-interface-configs";
    public static String IO_INTERFACE_KEY = IOInterface().getClass().getCanonicalName();

    // Fields
    private IOInterface IOInterface;
    private File settingsFile;
    private Saver saver;

    public void init()
        throws Exception
    {
        Bootstrap.bootstrap = this;
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
        ModuleLoader.use().addClassToTrack(AbstractAction.class);
        ModuleLoader.use().addClassToTrack(FileReaderInterface.class);
        ModuleLoader.use().addClassToTrack(FileWriterInterface.class);

        Set<AbstractSetting> moduleLocations = GlobalSettings.use().getSettings().getByGroupAndtype(Bootstrap.MODULE_LOCATIONS_KEY, File.class);

        Set<File> jarLocations = new HashSet<>();
        for (AbstractSetting abstractSetting : moduleLocations)
        {
            jarLocations.add((File) abstractSetting.getValue());
        }

        if (moduleLocations != null)
        {// Add jars and then locate tracked classes
            ModuleLoader.use().addModulesToClasspath(jarLocations);
        }
        else
        {// Locate the tracked classes
            ModuleLoader.use().addModulesToClasspath();
        }

        this.IOInterface = loadIOInterface();

        // Give bookmark access to the message board
        MessageBoard messageBoard = MessageBoard.use();
        for (AbstractBookmark abs : this.IOInterface.getAllBookmarks())
        {
            messageBoard.setSecretKey(abs);
        }

        System.out.println("Begin saving thread.");
        saver = Saver.use(this);
        saver.start();
    }

    public void exit()
    {
        saver.quit();
    }

    // ============================================================
    // Methods
    // ============================================================

    public BootstrapSettings getSettings()
    {
        BootstrapSettings res = new BootstrapSettings();
        res.setMainSettings(GlobalSettings.use().getSettings());
        res.setIoSettings(this.getIOInterface().getSettings());
        return res;
    }

    public void setSettings(BootstrapSettings bootstrapSettings)
    {
        GlobalSettings.use().setSettings(bootstrapSettings.getMainSettings());
        getIOInterface().setSettings(bootstrapSettings.getIoSettings());
    }

    public IOInterface getIOInterface()
    {
        return this.IOInterface;
    }

    synchronized public void saveSettingsFile()
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
    private IOInterface loadIOInterface()
        throws Exception
    {
        Set<Class> classes = ModuleLoader.use().getClassesLoaded(IOInterface.class);

        //TODO do this if there is no default, or if the default is not found.

        for (Class clazz : classes)
        {//Iterate through bkio classes found, selecting the correct one based on settings.
            try
            {
                logger.trace("Attempting to load bookmark io interface \"" + clazz.getCanonicalName() + "\"");
                //Attempting to load the config setting for this class
                AbstractSetting configSetting = GlobalSettings.use().getSettings().getSetting(Bootstrap.BKIO_CONFIGS, clazz.getCanonicalName());
                String config = null;

                if (configSetting != null)
                {
                    if (configSetting.getValue() instanceof String)
                    {
                        config = (String) configSetting.getValue();
                    }
                }

                if (config == null)
                {
                    config = "";
                }

                IOInterface bkio2 = ModuleLoader.use().loadClass(clazz.getCanonicalName(), IOInterface.class);

                logger.info(
                    "Loaded BKIOInterface class: \"" + clazz + "\" with this config: \"" + (config.isEmpty() ? "[no config found]" : config) + "\"");
                logger.info("Calling use()...");
                if (uiInterface != null)
                {
                    bkio2.setUIInterface(uiInterface.getIOUIInterface());
                }
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
        //        settingItem.setGroup(Bootstrap.BKIO_CONFIGS);
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

        // Try [user home]/Bookmarkanator/Settings.xml
        file = new File(getDefaultHomeDirSettingsFileInBKFolder());

        if (file.exists())
        {
            logger.info("Using settings file at " + file.getCanonicalPath());
            return file;
        }

        //        // Try [user home]/settings/Settings.xml
        //        file = new File(getDefaultHomeDirSettingsFile());
        //
        //        if (file.exists())
        //        {
        //            logger.info("Using settings file at " + file.getCanonicalPath());
        //            return file;
        //        }

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

    public BootstrapUIInterface getUiInterface()
    {
        return uiInterface;
    }

    public void setUiInterface(BootstrapUIInterface uiInterface)
    {
        this.uiInterface = uiInterface;
    }

    // ============================================================
    // Static Methods
    // ============================================================

    public static IOInterface IOInterface()
    {
        return Bootstrap.use().IOInterface;
    }

    public static Bootstrap use(BootstrapUIInterface uiInterface)
    {
        if (bootstrap == null)
        {
            try
            {
                bootstrap = new Bootstrap();
                bootstrap.setUiInterface(uiInterface);
                bootstrap.init();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return bootstrap;
    }

    public static Bootstrap use()
    {
        if (bootstrap == null)
        {
            try
            {
                bootstrap = new Bootstrap();
                bootstrap.init();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return bootstrap;
    }

}
