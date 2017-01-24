package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.util.*;

public class Bootstrap
{
    private static final Logger logger = Logger.getLogger(Bootstrap.class.getName());
    //Default fields
    private static final String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    private static final String DEFAULT_SETTINGS_DIRECTORY = "Bookmark-anator";
    private static final String SETTINGS_XSD_FILE = "/com.bookmarkanator.xml/SettingsStructure.xsd";
    private static final String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";

    //Settings fields related to overriding the default classes.
    //TODO Find a better way to do default settings.
    private static final String MODULE_LOCATIONS_KEY = "module-locations";
    private static final String BOOKMARK_IO_CONFIG_KEY = "bookmark-io-config";
    private static final String BOOKMARK_IO_CLASS_KEY = "bookmark-io-class";
    private static final String MODULE_LOADER_CLASS_KEY = "module-loader-class";
    private static final String BOOKMARKS_PARSER_CLASS_KEY = "bookmarks-parser-class";
    private static final String BOOKMARKS_WRITER_CLASS_KEY = "bookmarks-writer-class";
    private static final String ENCRYPTED_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.EncryptedBookmark";
    private static final String TEXT_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.TextBookmark";
    private static final String WEB_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.WebBookmark";
    private static final String REMINDER_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.ReminderBookmark";
    private static final String SEQUENCE_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.SequenceBookmark";
    private static final String TERMINAL_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.TerminalBookmark";
    private static final String FILE_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.FileBookmark";

    //To override a built in bookmark set the following: <original bookmark classname, overriding bookmark class name>
    //example: <com.bookmarkanator.TextBookmark, List[com.bookmarkanator.NewTextBookmark]>

    //TODO add generics in the settings classes.

    private Settings loadedSettings;
    private Settings defaultSettings;
    private boolean hasClosed;
    private ClassLoader classLoader;
    private BKIOInterface bkioInterface;
    private ModuleLoader moduleLoader;

    public Bootstrap()
        throws Exception
    {
        logger.finest("Logging finest");

        //TODO setup logging framework.
        this.classLoader = this.getClass().getClassLoader();
        hasClosed = false;
        defaultSettings = getDefaultSettings();
        loadedSettings = loadSettings();

        moduleLoader = new ModuleLoader();
        this.classLoader = moduleLoader.addModulesToClasspath(loadedSettings.getSettings(Bootstrap.MODULE_LOCATIONS_KEY), this.getClass().getClassLoader());

        this.bkioInterface = loadBKIOInterface();
    }

    public void acceptException(Exception ex)
    {
        ex.printStackTrace();
    }

    public BKIOInterface getBkioInterface()
    {
        return bkioInterface;
    }

    /**
     * Loads the BKIOInterface class that is specified in the settings file, or the default setting added to the settings file.
     * @return  A BKIOInterface class that was loaded.
     * @throws Exception
     */
    private BKIOInterface loadBKIOInterface()
        throws Exception
    {
        //The class name to use when loading the class
        List<String> bkioClasses = this.loadedSettings.getSettings(BOOKMARK_IO_CLASS_KEY);
        //The list of config strings found in the settings. For example FileIo class requires a file path string to load the bookmarks file.
        List<String> bkioConfigs = this.loadedSettings.getSettings(BOOKMARK_IO_CONFIG_KEY);

        assert bkioClasses != null;
        assert bkioConfigs != null;

        for (int c = 0; c < bkioClasses.size(); c++)
        {//Iterate through the class settings for BKIOInterface classes, and match (by index) the config for that class.
            // Return the first class loaded, initialized using the settings at the index for that class.
            try
            {
                String className = bkioClasses.get(c);
                String config = Util.getItem(bkioConfigs, c);
                BKIOInterface bkio2 = moduleLoader.loadClass(className, BKIOInterface.class, this.classLoader);

                logger.config("Loaded BKIOInterface class: \"" + className + "\" with this config: \""+config+"\"");
                logger.config("Calling init()...");
                bkio2.init(config, loadedSettings, this.classLoader);
                logger.config("Done.");

                return bkio2;
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
            {//Print the error and move on to try loading the next class.
                e.printStackTrace();
            }
        }

        throw new Exception("No valid BKIOInterface classes could be loaded.");
    }



    /**
     * Returns a map with default settings, in case there is no settings file (or an empty one), and also for comparing setting by setting to use
     * the default setting if the setting is not specified in the settings file.
     * @return  A map of settings in the form of <Setting key, List<individual multiple settings>>
     * @throws FileNotFoundException  If the default base directory cannot be accessed.
     */
    private Settings getDefaultSettings()
        throws FileNotFoundException
    {
        Settings res = new Settings();

        res.putSetting(MODULE_LOCATIONS_KEY, getDefaultBaseDirectory());
        res.putSetting(ENCRYPTED_BOOKMARK_CLASS, ENCRYPTED_BOOKMARK_CLASS);
        res.putSetting(REMINDER_BOOKMARK_CLASS, REMINDER_BOOKMARK_CLASS);
        res.putSetting(SEQUENCE_BOOKMARK_CLASS, SEQUENCE_BOOKMARK_CLASS);
        res.putSetting(TERMINAL_BOOKMARK_CLASS, TERMINAL_BOOKMARK_CLASS);
        res.putSetting(TEXT_BOOKMARK_CLASS, TEXT_BOOKMARK_CLASS);
        res.putSetting(WEB_BOOKMARK_CLASS, WEB_BOOKMARK_CLASS);
        res.putSetting(BOOKMARK_IO_CONFIG_KEY, getDefaultBookmarksDirLocation());
        res.putSetting(BOOKMARK_IO_CLASS_KEY, "com.bookmarkanator.io.FileIO");

        return res;
    }

    /**
     * Finds the settings file that is in the users home directory, and loads the settings from it, or if empty adds the settings. If not existent, it creates it.
     * @return  A map of settings in the form of <Setting key, List<individual multiple settings>>
     * @throws FileNotFoundException  If the default base directory cannot be accessed.
     */
    private Settings loadSettings()
        throws Exception
    {
        File settingsFile = getSettingsFile();
        FileInputStream fin = new FileInputStream(settingsFile);
        Settings.validateXML(fin, SETTINGS_XSD_FILE);
        fin.close();

        fin = new FileInputStream(settingsFile);

        Settings res = Settings.parseSettings(fin);
        fin.close();

        boolean needsSaving = res.diffInto(defaultSettings);

        if (needsSaving)
        {
            saveSettingsFile(res, settingsFile);//save changes
        }

        return res;
    }

    private File getSettingsFile()
        throws Exception
    {
        String path = getDefaultSettingsDirectory();
        File file = new File(path);

        if (!file.exists())
        {
            if (!file.getParentFile().exists())
            {//create directory to place file
                file.getParentFile().mkdir();
            }

            if (!file.createNewFile())
            {//create settings file
                throw new IOException("Failed to create file " + file.getCanonicalPath());
            }
        }

        if (file.length() == 0)
        {
            saveSettingsFile(defaultSettings, file);
        }

        return file;
    }

    private String getDefaultSettingsDirectory()
    {
        String directory = getDefaultBaseDirectory();
        return directory + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_FILE_NAME;
    }

    private String getDefaultBookmarksDirLocation()
    {
        String directory = getDefaultBaseDirectory();
        return directory + File.separatorChar + Bootstrap.DEFAULT_BOOKMARKS_FILE_NAME;
    }

    private String getDefaultBaseDirectory()
    {
        String usersHome = System.getProperty("user.home");
        return usersHome + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_DIRECTORY;
    }

    /**
     * Saves specific settings to a specific directory (with the default setting file name)
     * @param settings  The settings to save
     * @param directory  The directory to place the settings file.
     * @throws Exception
     */
    public void saveSettingsFile(Settings settings, File directory)
        throws Exception
    {
        hasClosed = true;
        FileOutputStream fout = new FileOutputStream(directory);
        Settings.writeSettings(settings, fout);
    }

    /**
     * Saves the settings for this class, in the default directory, with the default name.
     * @throws Exception
     */
    public void saveSettingsFile()
        throws Exception
    {
        saveSettingsFile(loadedSettings, new File(getDefaultSettingsDirectory()));
    }

    public Settings getSettings()
    {
        return loadedSettings;
    }

    public ClassLoader getClassLoader()
    {
        return this.classLoader;
    }

    @Override
    protected void finalize()
        throws Throwable
    {
        super.finalize();
        if (!hasClosed)
        {
            saveSettingsFile(loadedSettings, new File(getDefaultSettingsDirectory()));
            bkioInterface.save();
        }
    }
}
