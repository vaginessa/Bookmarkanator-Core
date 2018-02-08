package com.bookmarking.bootstrap;

import java.io.*;
import java.util.*;
import com.bookmarking.*;
import com.bookmarking.action.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.file.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.util.*;
import org.apache.logging.log4j.*;

/**
 * This class is responsible for doing the initial settings, and class loading.
 */
public class Bootstrap implements InitInterface
{

    // Static fields
    private static final Logger logger = LogManager.getLogger(Bootstrap.class.getCanonicalName());
//    private static Bootstrap bootstrap;

    // Default names



    // Settings keys
    private static final String GLOBAL_SETTINGS_GROUP = "GLOBAL_SETTINGS";

    // Module loader keys
    private static final String MODULE_LOCATIONS_GROUP = "module-locations";

    // General keys
    public static final String DEFAULT_CLASSES_GROUP = "default-classes";
    public static final String INIT_SETTING_KEY = "init-setting";

    // Fields
    private IOInterface ioInterface;
    private File settingsFile;
    private Settings settings;
//    private Saver saver;
    // The user interface that this class can interact with to show status and post messages.
    private InitUIInterface uiInterface;
    private static Version version;

    static
    {
        version = VersionParser.parseJavaVersion("0.6.0-1");
        System.out.println();
    }

    public Bootstrap()
    {
    }

    // ============================================================
    // Methods
    // ============================================================

    public void init(Settings settings) throws Exception
    {
        this.settings = settings;
        init();
    }

    public void init()
        throws Exception
    {
        logger.info("-----------------------------------------------------------------");
        logger.info("Init Bootstrap");
        logger.info("-----------------------------------------------------------------");

//        Bootstrap.bootstrap = this;

        // Search for and load settings file. This determines operating directory.
        initSettings();

        // Use module locations from settings to locate and load modules, as well as
        // tracked classes.
        initModules();

        // TODO Add update check here, and if there is a UI interface ask the UI interface if it can update or not.
        // TODO If the user select not, it won't prompt for the same update again.

        // Load IO interface specified in settings or the default IO interface.
        initIOInterface();

//        System.out.println("Starting saver thread.");
//        saver = Saver.use(this);
//        saver.start();
    }

    @Override
    public void init(SettingsIOInterface settingsIOInterface, InitUIInterface initUIInterface)
        throws Exception
    {

    }

    public void exit()
        throws Exception
    {
//        saver.quit();
        this.saveSettingsFile();
        this.getIOInterface().save();
    }


    public void setInitUIInterface(InitUIInterface initUIInterface)
    {

    }
    public InitUIInterface getInitUIInterface()
    {
        return null;
    }

    @Override
    public Version getVersion()
    {
        return version;
    }

    // ============================================================
    // Methods
    // ============================================================

    protected MainSettings getSettings()
    {
        MainSettings res = new MainSettings();
        res.setMainSettings(settings);
        if (this.getIOInterface()!=null)
        {
            res.setIoSettings(this.getIOInterface().getSettings());
        }
        return res;
    }

    protected void setSettings(MainSettings mainSettings)
    {
        settings = mainSettings.getMainSettings();
        getIOInterface().setSettings(mainSettings.getIoSettings());
    }

    public IOInterface getIOInterface()
    {
        return this.ioInterface;
    }

    protected void saveSettingsFile()
        throws Exception
    {
        FileSync<Settings> fileSync = FileService.use().getFileSync(GLOBAL_SETTINGS_GROUP);
        fileSync.setObjectToWrite(settings);
        fileSync.writeToDisk();
    }

    protected File getSettingsFile()
        throws IOException
    {
        return settingsFile;
    }

    // ============================================================
    // Private Methods
    // ============================================================

    /**
     * Locates and loads settings file. If none exists it will create one.
     * This determines operating directory.
     */
    private void initSettings()
        throws Exception
    {
        logger.trace("Obtaining Main Settings");

        // Get settings file
        File currentDir = new File(".");
        logger.info("Current directory " + currentDir.getCanonicalPath());
        logger.info("Locating settings file...");
        settingsFile = locateSettingsDirectory();

        FileSync<Settings> fileSync = new FileSync<>(new SettingsXMLWriter(), new SettingsXMLParser(), settingsFile);
        FileService.use().addFile(fileSync, MainInterface.SETTINGS_FILE_CONTEXT);

        fileSync.readFromDisk();
        Settings parsedSettings = fileSync.getParsedObject();
        parsedSettings.importSettings(this.settings);
        parsedSettings.importSettings(getDefaultSettings());
        this.settings = parsedSettings;
    }

    /**
     * Obtains module locations and tracked classes from the settings, and
     * attempts to locate and load them.
     */
    private void initModules()
        throws Exception
    {
        // Track the classes that can be overridden externally...
        ModuleLoader.use().addClassToWatch(AbstractBookmark.class);
        ModuleLoader.use().addClassToWatch(IOInterface.class);
        ModuleLoader.use().addClassToWatch(AbstractAction.class);
        ModuleLoader.use().addClassToWatch(FileReaderInterface.class);
        ModuleLoader.use().addClassToWatch(FileWriterInterface.class);
        ModuleLoader.use().addClassToWatch(String.class);

        Set<AbstractSetting> moduleLocations = settings.getByGroupAndtype(Bootstrap.MODULE_LOCATIONS_GROUP, File.class);

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
    }

    /**
     * Load IO interface based on settings entries, and if it cannot be found it loads the default
     * FileIO interface.
     */
    private void initIOInterface()
        throws Exception
    {
        loadIOInterface();

        // Give bookmark access to the message board
        MessageBoard messageBoard = MessageBoard.use();
        for (AbstractBookmark abs : this.ioInterface.getAllBookmarks())
        {
            messageBoard.setSecretKey(abs);
        }
    }

    /**
     * Loads the IOInterface class that is specified in the settings file, or the default FileIO implementation.
     *
     * @return A IOInterface class that was loaded.
     * @throws Exception
     */
    private void loadIOInterface()
        throws Exception
    {
        logger.trace("Searching for default IO interface setting");
        String config = null;
        AbstractSetting defaultIOInterface = settings.getSetting(Bootstrap.DEFAULT_CLASSES_GROUP, IOInterface.class.getCanonicalName());
        Class ioInterfaceFound = null;

        if (defaultIOInterface != null)
        {
            if (defaultIOInterface instanceof ClassSetting)
            {
                ioInterfaceFound = ((ClassSetting) defaultIOInterface).getValue();
            }
            else if (defaultIOInterface instanceof StringSetting)
            {
                ioInterfaceFound = Class.forName(((StringSetting) defaultIOInterface).getValue());
            }
        }

        if (ioInterfaceFound == null)
        {// It is still null so load default FileIO interface
            ioInterfaceFound = FileIO.class;
        }

        Objects.requireNonNull(ioInterfaceFound);

        // Obtain the init setting for this particular IO interface implementation.
        AbstractSetting configSetting = settings.getSetting(ioInterfaceFound.getCanonicalName(), INIT_SETTING_KEY);

        if (configSetting != null)
        {
            config = configSetting.getValue().toString();
        }
        else
        {
            config = "";
        }

        IOInterface bkio2 = ModuleLoader.use().instantiateClass(ioInterfaceFound.getCanonicalName(), IOInterface.class);

        Objects.requireNonNull(bkio2);

        logger.info("Loaded BKIOInterface class: \"" + ioInterfaceFound.getCanonicalName() + "\" with this config: \"" +
            (config.isEmpty() ? "[no config found]" : config) + "\"");

        // Allow the IO interface to have access to the UI interface if present to send init messages/statuses
        if (uiInterface != null)
        {
            bkio2.setUIInterface(uiInterface.getIOUIInterface());
        }

        logger.info("Calling init on IO interface...");

        this.ioInterface = bkio2;

        bkio2.init(config);

        logger.info("Done.");
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

    public InitUIInterface getUiInterface()
    {
        return uiInterface;
    }

    public void setUiInterface(InitUIInterface uiInterface)
    {
        this.uiInterface = uiInterface;
    }

    // ============================================================
    // Static Methods
    // ============================================================

//    public static IOInterface IOInterface()
//    {
//        return Bootstrap.use().ioInterface;
//    }
//
//    public static Bootstrap use(Settings settings)
//    {
//        if (bootstrap == null)
//        {
//            try
//            {
//                bootstrap = new Bootstrap(settings);
//                bootstrap.init();
//            }
//            catch (Exception e)
//            {
//                throw new RuntimeException(e);
//            }
//        }
//        return bootstrap;
//    }
//
//    public static Bootstrap use(BootstrapUIInterface uiInterface)
//    {
//        if (bootstrap == null)
//        {
//            try
//            {
//                bootstrap = new Bootstrap();
//                bootstrap.setUiInterface(uiInterface);
//                bootstrap.init();
//            }
//            catch (Exception e)
//            {
//                throw new RuntimeException(e);
//            }
//        }
//        return bootstrap;
//    }
//
//    public static Bootstrap use()
//    {
//        if (bootstrap == null)
//        {
//            try
//            {
//                bootstrap = new Bootstrap();
//                bootstrap.init();
//            }
//            catch (Exception e)
//            {
//                throw new RuntimeException(e);
//            }
//        }
//        return bootstrap;
//    }

}
