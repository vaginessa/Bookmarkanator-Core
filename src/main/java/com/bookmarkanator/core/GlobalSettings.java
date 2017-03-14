package com.bookmarkanator.core;

import java.io.*;
import com.bookmarkanator.util.*;

public class GlobalSettings
{
    private static GlobalSettings globalSettings;
    public static String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    public static String DEFAULT_SETTINGS_DIRECTORY_NAME = "Settings";

    private Settings settings;
    private File file;
    //The default for this class is to store settings in the users home directory. So only the folder name, and settings file name are necessary.

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
        throws IOException
    {
        this.file = file;
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
            fin = new FileInputStream(file);
            settings = Settings.parseSettings(fin, this.getClass().getClassLoader());
            fin.close();
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (fin != null)
            {
                fin.close();
            }
        }
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
