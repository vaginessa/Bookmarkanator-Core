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
        file = null;
        String abc = System.getProperty("user.home") + File.separatorChar + DEFAULT_SETTINGS_DIRECTORY_NAME + File.separatorChar + DEFAULT_SETTINGS_FILE_NAME;
        file = new File(abc);

        settings = new Settings();
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
        throws Exception
    {
        String originalFileName = (this.file != null ? this.file.getCanonicalPath() : "<none set>");
        String newFileName = (file != null ? file.getCanonicalPath() : "<unknown>");

        if (file!=null)
        {
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
        this.settings = settings;
    }

    public void writeToDisk()
        throws Exception
    {
        FileOutputStream fout = null;
        try
        {
            file = Util.getOrCreateFile(file);
            fout = new FileOutputStream(file);
            Settings.writeSettings(settings, fout);
        }
        catch (Exception e)
        {
            throw e;
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
        FileInputStream fin = null;
        try
        {
            file = Util.getOrCreateFile(file);
            if (file.length()!=0)
            {
                fin = new FileInputStream(file);
                settings = Settings.parseSettings(fin, this.getClass().getClassLoader());
                fin.close();
            }
        }
        catch (Exception e)
        {
            logger.error(e);
            //TODO Mark current setting file bad.
            //TODO Create new settings file
            //TODO save current settings to it.
            //TODO Continue on with program
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
