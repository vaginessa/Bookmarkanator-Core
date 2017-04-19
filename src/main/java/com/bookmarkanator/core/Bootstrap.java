package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;
import org.apache.logging.log4j.*;

public class Bootstrap
{
    private static final Logger logger = LogManager.getLogger(Bootstrap.class.getCanonicalName());
    private static Bootstrap bootstrap;

    //Default fields
    private static final String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    private static final String DEFAULT_SETTINGS_DIRECTORY = "Bookmark-anator";
    private static final String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";
    private static final String SETTINGS_FILES_CONFIG= "settings-files-config";

    //Settings fields related to overriding the default classes.
    private static final String MODULE_LOCATIONS_KEY = "module-locations";//TODO --- REMOVE THIS, IT JUST MAKES THINGS TO COMPLICATED WITH THE MULTIPLE WORKSPACES
    //Workspaces allow one to have different types of bookmarks, with different settings, and types, separated into different directories.
    public static final String WORKSPACES = "workspaces";//Alternate directories where another settings file, and bookmark.xml file(s) can be found. Each workspace must have a unique name (key).
    public static final String OVERRIDDEN_CORE_CLASSES = "overridden-core-classes";
    public static final String CORE_CLASS_CONFIGS = "core-class-configs";

    private BKIOInterface bkioInterface;

    public Bootstrap()
        throws Exception
    {
        logger.trace("Enter Bootstrap constructor");

        //Configure the global settings object
        GlobalSettings.use().setFile(new File(getDefaultSettingsDirectory()));
        GlobalSettings.use().readFromDisk();

        //TODO If a different workspace is indicated switch to that workspace and load the settings file found there in. Add a way to specify the bkio interface to use for this new workspace?

        GlobalSettings.use().getSettings().importSettings(getDefaultSettings());

        //Track the classes that can be overridden externally...
        ModuleLoader.use().addClassToTrack(AbstractBookmark.class);
        ModuleLoader.use().addClassToTrack(BKIOInterface.class);
        ModuleLoader.use().addClassToTrack(ContextInterface.class);

        Set<SettingItem> moduleLocations = GlobalSettings.use().getSettings().getByType(Bootstrap.MODULE_LOCATIONS_KEY);

        if (moduleLocations!=null)
        {//Add jars and then locate tracked classes
            ModuleLoader.use().addModulesToClasspath(Settings.extractValues(moduleLocations));
        }
        else
        {//Locate the tracked classes
            ModuleLoader.use().addModulesToClasspath();
        }

        this.bkioInterface = loadBKIOInterface();
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

    // ============================================================
    // Private Methods
    // ============================================================

    /**
     * Loads the BKIOInterface class that is specified in the settings file, or the default setting added to the settings file.
     * @return A BKIOInterface class that was loaded.
     * @throws Exception
     */
    private BKIOInterface loadBKIOInterface()
        throws Exception
    {
        Set<Class> classes = ModuleLoader.use().getClassesLoaded(BKIOInterface.class);

        for (Class clazz: classes)
        {//Iterate through bkio classes found, selecting the correct one based on settings.
            try
            {
                SettingItem configSetting = GlobalSettings.use().getSettings().getSetting(Bootstrap.CORE_CLASS_CONFIGS, clazz.getCanonicalName());
                String config = null;

                if (configSetting!=null)
                {
                    config = configSetting.getValue();
                }

                BKIOInterface bkio2 = ModuleLoader.use().loadClass(clazz.getCanonicalName(), BKIOInterface.class);

                System.out.println("Loaded BKIOInterface class: \"" + clazz + "\" with this config: \""+config+"\"");
                System.out.println("Calling init()...");
                bkio2.init(config);
                System.out.println("Done.");

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

        SettingItem settingItem = new SettingItem(FileIO.class.getCanonicalName());
        settingItem.setValue(getDefaultBookmarksDirLocation());
        settingItem.setType(Bootstrap.CORE_CLASS_CONFIGS);
        res.putSetting(settingItem);

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

    // ============================================================
    // Static Methods
    // ============================================================

    public static BKIOInterface IOInterface()
    {
        return Bootstrap.use().bkioInterface;
    }

    public static ContextInterface context()
        throws Exception
    {
        return Bootstrap.use().bkioInterface.getContext();
    }

    public static Bootstrap use()
    {
        if (bootstrap==null)
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
