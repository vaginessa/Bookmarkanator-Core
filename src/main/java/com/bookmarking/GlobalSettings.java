package com.bookmarking;

import java.io.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.settings.*;
import com.bookmarking.xml.*;
import org.apache.logging.log4j.*;

/**
 * This class is used as a global settings object. It adds features such as loading and saving the settings files. The default for this class is
 * to store settings in the users home directory. So only the folder name, and settings file name are necessary.
 */
public class GlobalSettings
{
    private static final Logger logger = LogManager.getLogger(GlobalSettings.class.getCanonicalName());
    private static final String GLOBAL_SETTINGS_KEY = "GLOBAL_SETTINGS";
    private static GlobalSettings globalSettings;
    public static String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    public static String DEFAULT_SETTINGS_DIRECTORY_NAME = "Settings";

    private Settings settings;

    public GlobalSettings()
    {
        logger.trace("Init global settings.");

        String defaultSettingsLocation =
            System.getProperty("user.home") + File.separatorChar + DEFAULT_SETTINGS_DIRECTORY_NAME + File.separatorChar + DEFAULT_SETTINGS_FILE_NAME;
        File file = new File(defaultSettingsLocation);
        FileSync<Settings> fileSync = new FileSync<>(new SettingsXMLWriter2(), new SettingsXMLParser2(), file);
        FileService.use().addFile(fileSync, GLOBAL_SETTINGS_KEY);
        logger.trace("Default settings file: \"" + defaultSettingsLocation + "\"");
        settings = new Settings();
    }

    public File getFile()
    {
        FileSync<Settings> fileSync = FileService.use().getFile(GLOBAL_SETTINGS_KEY);
        return fileSync.getFile();
    }

    public void setFile(File file)
        throws Exception
    {
        FileSync<Settings> fileSync = FileService.use().getFile(GLOBAL_SETTINGS_KEY);
        fileSync.setFile(file);
    }

    public Settings getSettings()
    {
        return settings;
    }

    public void setSettings(Settings settings)
    {
        logger.info("Setting settings object ");
        this.settings = settings;
    }

    public void writeToDisk()
        throws Exception
    {
        FileSync<Settings> fileSync = FileService.use().getFile(GLOBAL_SETTINGS_KEY);
        fileSync.setObjectToWrite(settings);
        fileSync.writeToDisk();
    }

    public void readFromDisk()
        throws Exception
    {
        FileSync<Settings> fileSync = FileService.use().getFile(GLOBAL_SETTINGS_KEY);

        fileSync.readFromDisk();
        this.settings = fileSync.getParsedObject();
    }

    public void clearSettings()
    {
        this.settings = new Settings();
    }

    public static GlobalSettings use()
    {
        if (globalSettings == null)
        {
            globalSettings = new GlobalSettings();
        }
        return globalSettings;
    }
}
