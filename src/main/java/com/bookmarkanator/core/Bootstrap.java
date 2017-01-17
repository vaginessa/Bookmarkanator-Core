package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.util.*;
import com.bookmarkanator.xml.*;

public class Bootstrap
{
    private static final Logger logger = Logger.getLogger(Bootstrap.class.getName());
    //Default fields
    private static final String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    private static final String DEFAULT_SETTINGS_DIRECTORY = "Bookmark-anator";
    private static final String SETTINGS_XSD = "/com.bookmarkanator.xml/SettingsStructure.xsd";
    private static final String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";

    //Settings fields related to overriding the default classes.
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
    //To override a built in bookmark set the following <original bookmark classname, overriding bookmark class name>
    //example: <com.bookmarkanator.TextBookmark, List[com.bookmarkanator.NewTextBookmark]>

    private GlobalSettings loadedSettings;
    private GlobalSettings defaultSettings;
    private boolean hasClosed;
    private ClassLoader classLoader;
    private BKIOInterface bkioInterface;

    public Bootstrap()
        throws Exception
    {
        logger.finest("Logging finest");

        //TODO setup logging framework.
        this.classLoader = this.getClass().getClassLoader();
        hasClosed = false;
        defaultSettings = getDefaultSettings();
        loadedSettings = loadSettings();

        addModulesToClasspath();

        this.bkioInterface = loadBKIOInterface();
    }

    private void addModulesToClasspath() throws Exception
    {
        List<String> jarLocations = loadedSettings.getSettings(Bootstrap.MODULE_LOCATIONS_KEY);

        if (jarLocations != null && !jarLocations.isEmpty())
        {
            ModuleLoader md = ModuleLoader.use();

            for (String s : jarLocations)
            {
                File f = new File(s);

                if (f.exists() || f.canRead())
                {
                    md.addDirectory(f);
                    logger.config("Adding directory \""+f.getCanonicalPath()+"\" to class loader paths.");
                }
                else
                {
                    logger.config("Attempted to add \""+s+"\" to the classloader but this file either doesn't exist or cannot be accessed.");
                }
            }
            this.classLoader = md.addJarsToClassloader(this.getClass().getClassLoader());
        }
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
        List<String> bkioClasses = this.loadedSettings.getSettings(BOOKMARK_IO_CLASS_KEY);
        List<String> bkioConfigs = this.loadedSettings.getSettings(BOOKMARK_IO_CONFIG_KEY);

        assert bkioClasses != null;
        assert bkioConfigs != null;

        for (int c = 0; c < bkioClasses.size(); c++)
        {//Iterate through the class settings for BKIOInterface classes, and match (by index) the config for that class.
            try
            {
                String className = bkioClasses.get(c);
                String config = Util.getItem(bkioConfigs, c);

                Class clazz = this.classLoader.loadClass(className);

                Class sub = clazz.asSubclass(BKIOInterface.class);
                BKIOInterface bkio = (BKIOInterface) sub.newInstance();
                logger.config("Loaded BKIOInterface class: \"" + className + "\" with this config: \""+config+"\"");
                logger.config("Calling init()...");
                bkio.init(config, loadedSettings, this.classLoader);
                logger.config("Done.");
                return bkio;
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
    private GlobalSettings getDefaultSettings()
        throws FileNotFoundException
    {
        GlobalSettings res = new GlobalSettings();
        List<String> list = new ArrayList<>();

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
    private GlobalSettings loadSettings()
        throws Exception
    {
        File settingsFile = getSettingsFile();
        FileInputStream fin = new FileInputStream(settingsFile);
        validateXML(fin);
        fin.close();

        fin = new FileInputStream(settingsFile);

        SettingsXMLParser parser = new SettingsXMLParser(fin);
        GlobalSettings res = parser.parse();
        fin.close();

        boolean needsSaving = false;

        for (String s : defaultSettings.keySet())
        {//ensure default settings are in place if other settings are missing.

            List<String> l = res.getSettings(s);
            if (l == null)
            {
                res.putSettings(s, defaultSettings.getSettings(s));
                needsSaving = true;
            }
        }
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

    private void validateXML(InputStream inputStream)
        throws Exception
    {
        InputStream xsd = this.getClass().getResourceAsStream(SETTINGS_XSD);
        XMLValidator.validate(inputStream, xsd);
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

    public void saveSettingsFile(GlobalSettings settings, File directory)
        throws Exception
    {
        hasClosed = true;

        FileOutputStream fout = new FileOutputStream(directory);
        SettingsXMLWriter writer = new SettingsXMLWriter(settings, fout);
        writer.write();
        fout.flush();
        fout.close();
        //write settings to file, and close file
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
