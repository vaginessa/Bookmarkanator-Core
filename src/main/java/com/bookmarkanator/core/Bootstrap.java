package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import com.bookmarkanator.xml.*;

public class Bootstrap
{
    //Default fields
    private static final String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    private static final String DEFAULT_SETTINGS_DIRECTORY = "Bookmark-anator";
    private static final String SETTINGS_XSD = "/com.bookmarkanator.xml/SettingsStructure.xsd";

    //Settings fields
    private static final String MODULE_LOCATIONS = "module-locations";
    private static final String ENCRYPTED_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.EncryptedBookmark";
    private static final String TEXT_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.TextBookmark";
    private static final String WEB_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.WebBookmark";
    private static final String REMINDER_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.ReminderBookmark";
    private static final String SEQUENCE_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.SequenceBookmark";
    private static final String TERMINAL_BOOKMARK_CLASS = "com.bookmarkanator.bookmarks.TerminalBookmark";
    //To override a built in bookmark set the following <original bookmark classname, overriding bookmark class name>
    //example: <com.bookmarkanator.TextBookmark, List[com.bookmarkanator.NewTextBookmark]>

    private Map<String, List<String>> loadedSettings;

    public Bootstrap()
        throws Exception
    {
        File settingsFile = getSettingsFile();
        loadedSettings = loadSettings(settingsFile);

        List<String> jarLocations = loadedSettings.get(Bootstrap.MODULE_LOCATIONS);
        if (jarLocations != null && !jarLocations.isEmpty())
        {
            ModuleLoader md = ModuleLoader.use();
            for (String s : jarLocations)
            {
                md.addDirectory(new File(s));
            }
            ClassLoader classLoader = md.addJarsToClassloader(this.getClass().getClassLoader());

            //TODO use classloader to search for interfaces to load.
        }

    }

    private Map<String, List<String>> getDefaultSettings()
        throws FileNotFoundException
    {
        Map<String, List<String>> res = new HashMap<>();
        List<String> list = new ArrayList<>();

        list.add(getDefaultBaseDirectory());
        res.put(MODULE_LOCATIONS, list);

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

        return res;
    }

    //Load loadedSettings from loadedSettings file at default location.
    //if no file use default loadedSettings

    //Load modules from location specified in the loadedSettings

    //Load bookmarks from location specified in the loadedSettings

    private Map<String, List<String>> loadSettings(File settingsFile)
        throws Exception
    {
        FileInputStream fin = new FileInputStream(settingsFile);
        validateXML(fin);
        fin = new FileInputStream(settingsFile);

        SettingsXMLParser parser = new SettingsXMLParser(fin);

        return parser.parse();
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

            if (file.createNewFile())
            {//Create new settings file and write the settings into it.
//                file.createNewFile();
                FileOutputStream fout = new FileOutputStream(file);
                SettingsXMLWriter writer = new SettingsXMLWriter(getDefaultSettings(), fout);
                writer.write();
                fout.flush();
                fout.close();
            }
            else
            {
                throw new IOException("Failed to create directory " + file.getParent());
            }
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

    private String getDefaultBaseDirectory()
    {
        String usersHome = System.getProperty("user.home");
        return usersHome + File.separatorChar + Bootstrap.DEFAULT_SETTINGS_DIRECTORY;
    }
}
