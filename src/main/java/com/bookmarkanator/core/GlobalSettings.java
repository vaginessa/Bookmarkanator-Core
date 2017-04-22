package com.bookmarkanator.core;

import java.io.*;
import com.bookmarkanator.util.*;
import org.apache.logging.log4j.*;

/**
 * This class is used as a global settings object. It adds features such as loading and saving the settings files. The default for this class is
 * to store settings in the users home directory. So only the folder name, and settings file name are necessary.
 */
public class GlobalSettings
{
    private static final Logger logger = LogManager.getLogger(GlobalSettings.class.getCanonicalName());
    private static GlobalSettings globalSettings;
    public static String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    public static String DEFAULT_SETTINGS_DIRECTORY_NAME = "Settings";

    private Settings settings;
    private File file;

    public GlobalSettings()
    {
        logger.trace("Init global settings.");
        file = null;
        String defaultSettingsLocation = System.getProperty("user.home") + File.separatorChar + DEFAULT_SETTINGS_DIRECTORY_NAME + File.separatorChar + DEFAULT_SETTINGS_FILE_NAME;
        file = new File(defaultSettingsLocation);
        logger.trace("Default settings file: \""+defaultSettingsLocation+"\"");
        settings = new Settings();
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
        throws Exception
    {
        String newFileName = (file != null ? file.getCanonicalPath() : "<unknown>");
        String originalFileName = (this.file != null ? this.file.getCanonicalPath() : "<none set>");

        if (file!=null)
        {
            logger.info("Changing settings file to \""+file.getCanonicalPath()+"\"");

            if (file.isFile())
            {
                if (file.canRead())
                {
                    if (file.canWrite())
                    {
                        this.file = file;
                    }
                    else
                    {
                        logger.warn("Permission to write file \"" +newFileName+"\" is denied. Defaulting to previously set file \""+originalFileName+"\"");
                    }
                }
                else
                {
                    logger.warn("Permission to read file \"" +newFileName+"\" is denied. Defaulting to previously set file \""+originalFileName+"\"");
                }
            }
            else
            {
                logger.warn("File \"" +newFileName+"\" is not a file. Defaulting to previously set file \""+originalFileName+"\"");
            }
        }
        else
        {
            logger.warn("File \"" +newFileName+"\" null. Defaulting to previously set file \""+originalFileName+"\"");
        }
    }

    public void replaceAndLoadSettings(File file)
        throws Exception
    {
        logger.info("Replace and load settings file");
        setFile(file);
        this.clearSettings();
        this.readFromDisk();
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
        logger.trace("Writing settings file to disk.");
        FileOutputStream fout = null;
        try
        {
            file = Util.getOrCreateFile(file);
            fout = new FileOutputStream(file);
            Settings.writeSettings(settings, fout);
        }
        finally
        {
            if (fout != null)
            {
                fout.flush();
                fout.close();
            }
        }
    }

    public void readFromDisk()
        throws Exception
    {
        logger.trace("Reading settings file from disk.");
        FileInputStream fin = null;
        try
        {
            file = Util.getOrCreateFile(file);

            // If the file has nothing in it it won't try to parse it. The settings file should get filled upon saving.
            if (file.length()!=0)
            {
                fin = new FileInputStream(file);
                settings = Settings.parseSettings(fin, this.getClass().getClassLoader());
                fin.close();
            }
        }
        finally
        {
            if (fin != null)
            {
                fin.close();
            }
        }
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
