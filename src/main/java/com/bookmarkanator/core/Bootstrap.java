package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.bookmarkanator.bookmarks.*;
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
    //TODO Go through all keys and types for settings values and improve them to be more obvious. Also possibly make a new type of settings object.
    //TODO Explicitly set settings for overriding class names.
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

    //Note: To override a built in bookmark set the following: <original bookmark classname, overriding bookmark class name>
    //example: <com.bookmarkanator.TextBookmark, List[com.bookmarkanator.NewTextBookmark]>

    private boolean hasClosed;
    private ClassLoader classLoader;
    private BKIOInterface bkioInterface;
    private ModuleLoader moduleLoader;
    private Set<Class<? extends AbstractBookmark>> bookmarkClassesFound;

    public Bootstrap()
        throws Exception
    {
        this.classLoader = this.getClass().getClassLoader();
        hasClosed = false;

        GlobalSettings.use().setFile(new File(getDefaultSettingsDirectory()));
        Settings defaultSettings = getDefaultSettings();
        GlobalSettings.use().getSettings().importSettings(defaultSettings);

        this.moduleLoader = new ModuleLoader();
        this.classLoader = moduleLoader.addModulesToClasspath(((SettingsItemList)GlobalSettings.use().getSettings().getSetting(Bootstrap.MODULE_LOCATIONS_KEY)).getItems(), this.getClass().getClassLoader());
        this.bookmarkClassesFound = moduleLoader.getBookmarkClassesFound();

        this.bkioInterface = loadBKIOInterface();
    }

    public BKIOInterface getBkioInterface()
    {
        return bkioInterface;
    }

    public Set<Class<? extends AbstractBookmark>> getBookmarkClassesFound()
    {
        return bookmarkClassesFound;
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
        List<String> bkioClasses = ((SettingsItemList)GlobalSettings.use().getSettings().getSetting(BOOKMARK_IO_CLASS_KEY)).getItems();
        //The list of config strings found in the settings. For example FileIo class requires a file path string to load the bookmarks file.
        List<String> bkioConfigs = ((SettingsItemList)GlobalSettings.use().getSettings().getSetting(BOOKMARK_IO_CONFIG_KEY)).getItems();

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
                bkio2.init(config, GlobalSettings.use().getSettings(), this.classLoader);
                logger.config("Done.");

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
     * @return  A map of settings in the form of <Setting key, List<individual multiple settings>>
     * @throws FileNotFoundException  If the default base directory cannot be accessed.
     */
    private Settings getDefaultSettings()
        throws Exception
    {
        Settings res = new Settings();

        SettingsItemList list = new SettingsItemList(MODULE_LOCATIONS_KEY);
        list.addItem(getDefaultBaseDirectory());
        list.setType("bootstrap");
        res.putSetting(list);

        SettingItem item = new SettingItem(ENCRYPTED_BOOKMARK_CLASS);
        item.setValue(ENCRYPTED_BOOKMARK_CLASS);
        item.setType("bootstrap");
        res.putSetting(item);

        item = new SettingItem(REMINDER_BOOKMARK_CLASS);
        item.setValue(REMINDER_BOOKMARK_CLASS);
        item.setType("bootstrap");
        res.putSetting(item);

        item = new SettingItem(SEQUENCE_BOOKMARK_CLASS);
        item.setValue(SEQUENCE_BOOKMARK_CLASS);
        item.setType("bootstrap");
        res.putSetting(item);

        item = new SettingItem(TERMINAL_BOOKMARK_CLASS);
        item.setValue(TERMINAL_BOOKMARK_CLASS);
        item.setType("bootstrap");
        res.putSetting(item);

        item = new SettingItem(TEXT_BOOKMARK_CLASS);
        item.setValue(TEXT_BOOKMARK_CLASS);
        item.setType("bootstrap");
        res.putSetting(item);

        item = new SettingItem(WEB_BOOKMARK_CLASS);
        item.setValue(WEB_BOOKMARK_CLASS);
        item.setType("bootstrap");
        res.putSetting(item);

        list = new SettingsItemList(BOOKMARK_IO_CONFIG_KEY);
        list.addItem(getDefaultBookmarksDirLocation());
        list.setType("bootstrap");
        res.putSetting(list);

        list = new SettingsItemList(BOOKMARK_IO_CLASS_KEY);
        list.addItem("com.bookmarkanator.io.FileIO");
        list.setType("bootstrap");
        res.putSetting(list);

        return res;
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

    public Settings getSettings()
    {
        return GlobalSettings.use().getSettings();
    }

    public void saveSettingsFile()
        throws Exception
    {
        GlobalSettings.use().writeToDisk();
    }

    public ClassLoader getClassLoader()
    {
        return this.classLoader;
    }

    public static Class loadBookmarkClass(String className, ClassLoader classLoader) throws Exception
    {
        Class clazz = classLoader.loadClass(className);
        Class sub = clazz.asSubclass(AbstractBookmark.class);
        System.out.println("Loaded bookmark class: \"" + className + "\".");
        return sub;
    }

    public static AbstractBookmark instantiateBookmarkClass(Class clazz,ContextInterface contextInterface ) throws Exception
    {
        return (AbstractBookmark) clazz.getConstructor(ContextInterface.class).newInstance(contextInterface);
    }

}
