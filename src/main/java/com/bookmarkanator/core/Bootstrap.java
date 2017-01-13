package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.util.*;
import com.bookmarkanator.xml.*;

public class Bootstrap
{
    //Default fields
    private static final String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    private static final String DEFAULT_SETTINGS_DIRECTORY = "Bookmark-anator";
    private static final String SETTINGS_XSD = "/com.bookmarkanator.xml/SettingsStructure.xsd";

    //Settings fields
    private static final String MODULE_LOCATIONS_KEY = "module-locations";
    private static final String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";
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

    private Map<String, List<String>> loadedSettings;
    private Map<String, List<String>> defaultSettings;
    private boolean hasClosed;
    private ClassLoader classLoader;

    public Bootstrap()
        throws Exception
    {

        //TODO setup logging framework.
        this.classLoader = this.getClass().getClassLoader();
        hasClosed = false;
        defaultSettings = getDefaultSettings();
        loadedSettings = loadSettings();

        List<String> jarLocations = loadedSettings.get(Bootstrap.MODULE_LOCATIONS_KEY);
        if (jarLocations != null && !jarLocations.isEmpty())
        {
            ModuleLoader md = ModuleLoader.use();
            for (String s : jarLocations)
            {
                md.addDirectory(new File(s));
            }
            this.classLoader = md.addJarsToClassloader(this.getClass().getClassLoader());

            //TODO use classloader to search for interfaces to load?
        }
        BKIOInterface bkioInterface = getBKIOInterface();

        System.out.println();

    }

    /**
     * Loads the BKIOInterface class that is specified in the settings file, or the default setting added to the settings file.
     * @return  A BKIOInterface class that was loaded.
     * @throws Exception
     */
    private BKIOInterface getBKIOInterface()
        throws Exception
    {
        List<String> bkioClasses = this.loadedSettings.get(BOOKMARK_IO_CLASS_KEY);
        List<String> bkioConfigs = this.loadedSettings.get(BOOKMARK_IO_CONFIG_KEY);

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

                bkio.init(config);
                System.out.println("Loaded BKIOInterface class: \"" + className + "\" with this config: \""+config+"\"");
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
    private Map<String, List<String>> getDefaultSettings()
        throws FileNotFoundException
    {
        Map<String, List<String>> res = new HashMap<>();
        List<String> list = new ArrayList<>();

        list.add(getDefaultBaseDirectory());
        res.put(MODULE_LOCATIONS_KEY, list);

        list = new ArrayList<>();
        list.add(ENCRYPTED_BOOKMARK_CLASS);
        res.put(ENCRYPTED_BOOKMARK_CLASS, list);

        list = new ArrayList<>();
        list.add(REMINDER_BOOKMARK_CLASS);
        res.put(REMINDER_BOOKMARK_CLASS, list);

        list = new ArrayList<>();
        list.add(SEQUENCE_BOOKMARK_CLASS);
        res.put(SEQUENCE_BOOKMARK_CLASS, list);

        list = new ArrayList<>();
        list.add(TERMINAL_BOOKMARK_CLASS);
        res.put(TERMINAL_BOOKMARK_CLASS, list);

        list = new ArrayList<>();
        list.add(TEXT_BOOKMARK_CLASS);
        res.put(TEXT_BOOKMARK_CLASS, list);

        list = new ArrayList<>();
        list.add(WEB_BOOKMARK_CLASS);
        res.put(WEB_BOOKMARK_CLASS, list);

        list = new ArrayList<>();
        list.add(getDefaultBookmarksDirLocation());
        res.put(BOOKMARK_IO_CONFIG_KEY, list);

        list = new ArrayList<>();
        list.add("com.bookmarkanator.io.FileIO");//default bookmark io class.
        res.put(BOOKMARK_IO_CLASS_KEY, list);

        return res;
    }

    /**
     * Finds the settings file that is in the users home directory, and loads the settings from it, or if empty adds the settings. If not existant it creates it.
     * @return  A map of settings in the form of <Setting key, List<individual multiple settings>>
     * @throws FileNotFoundException  If the default base directory cannot be accessed.
     */
    private Map<String, List<String>> loadSettings()
        throws Exception
    {
        File settingsFile = getSettingsFile();
        FileInputStream fin = new FileInputStream(settingsFile);
        validateXML(fin);
        fin.close();

        //TODO handle validation error on empty file.

        fin = new FileInputStream(settingsFile);

        SettingsXMLParser parser = new SettingsXMLParser(fin);
        Map<String, List<String>> res = parser.parse();
        fin.close();

        boolean needsSaving = false;

        for (String s : defaultSettings.keySet())
        {//ensure default settings are in place if other settings are missing.
            List<String> l = res.get(s);
            if (l == null)
            {
                res.put(s, defaultSettings.get(s));
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
            {
                file.getParentFile().mkdir();
            }

            if (!file.createNewFile())
            {
                throw new IOException("Failed to create file " + file.getCanonicalPath());
            }
        }

        if (file.length() == 0)
        {
            saveSettingsFile(defaultSettings, file);
        }

        //TODO handle empty bookmarks file.

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

    public void saveSettingsFile(Map<String, List<String>> settings, File directory)
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
        }
    }
}
