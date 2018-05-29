package com.bookmarking.settings;

import java.io.*;
import java.util.*;
import com.bookmarking.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.settings.types.*;
import org.apache.logging.log4j.*;

public class FileSettingsIO implements SettingsIOInterface
{
    private static final Logger logger = LogManager.getLogger(FileSettingsIO.class.getCanonicalName());

    private Settings settings;

    @Override
    public Settings init(Settings settings)
        throws Exception
    {
        logger.info("- Init FileSettingsIO");
        this.settings = settings;

        // Locate the settings file based on settings being brought in, and check if it exists.
        File fileInUse = null;
        File primarySettingsFile = locatPrimarySettingsFile();
        File secondarySettingsFile = locatSecondarySettingsFile();
        File fallbackSettingsFile = locatFallbackSettingsFile();

        if (primarySettingsFile != null)
        {
            logger.info("- Using primary file setting of \""+primarySettingsFile+"\"");
            fileInUse = primarySettingsFile;
        }
        else if (secondarySettingsFile != null)
        {
            logger.info("- Using secondary file setting of \""+secondarySettingsFile+"\"");
            fileInUse = secondarySettingsFile;
        }
        else if (fallbackSettingsFile != null)
        {
            logger.info("- Using fallback file setting of \""+fallbackSettingsFile+"\"");
            fileInUse = fallbackSettingsFile;
        }

        // If settings file is still null then no existing settings file can be found... Create one by using the settings in order of importance.
        if (fileInUse==null)
        {
            logger.info("- Settings file doesn't exist. Creating now...");
            if (hasSettings(Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SETTINGS_FILE_LOCATION_KEY, Defaults.DEFAULT_SETTINGS_FILE_NAME_KEY))
            {
                fileInUse = getFile(
                    Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SETTINGS_FILE_LOCATION_KEY, Defaults.DEFAULT_SETTINGS_FILE_NAME_KEY);
                ensureFileExists(fileInUse);
                logger.info("-- Using primary setting location of \""+fileInUse+"\"");
            }
            else if (hasSettings(
                Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SECONDARY_SETTINGS_FILE_LOCATION_KEY, Defaults.DEFAULT_SECONDARY_SETTINGS_FILE_NAME_KEY))
            {
                fileInUse = getFile(
                    Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SECONDARY_SETTINGS_FILE_LOCATION_KEY, Defaults.DEFAULT_SECONDARY_SETTINGS_FILE_NAME_KEY);
                ensureFileExists(fileInUse);
                logger.info("-- Using secondary setting location \""+fileInUse+"\"");
            }
            else
            {// No settings have been found, use the fallback settings to create a file.
                fileInUse = getFile(Defaults.FALLBACK_SETTINGS_DIRECTORY, Defaults.FALLBACK_SETTGINS_FILE_NAME);
                ensureFileExists(fileInUse);
                logger.info("-- Using fallback setting location \""+fileInUse+"\"");
            }

            logger.info("-- Created settings file at \""+fileInUse+"\n");
        }

        // Read settings file in...
        FileSync<Settings> fileSync = new FileSync<>(new SettingsXMLWriter(),new SettingsXMLParser(), fileInUse);
        FileService.use().addFile(fileSync, Defaults.FILE_SYNC_CONTEXT);

        fileSync.readFromDisk();

        // Get parsed settings file.
        Settings parsedSettings = fileSync.getObject();
        Objects.requireNonNull(parsedSettings);

        parsedSettings.importSettings(settings);

        // Write the settings out again with defaults merged in.
        fileSync.writeToDisk();
        logger.info("- Done.");
        return settings;
    }

    @Override
    public void prepExit()
        throws Exception
    {
        FileService.use().getFileSync(Defaults.FILE_SYNC_CONTEXT).writeToDisk();
    }

    @Override
    public void exit()
    {
        // Nothing to do here?
    }

    private File locatPrimarySettingsFile()
        throws Exception
    {
        File file = getFile(Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SETTINGS_FILE_LOCATION_KEY, Defaults.DEFAULT_SETTINGS_FILE_NAME_KEY);

        if (file != null && file.exists())
        {
            return file;
        }

        return null;
    }

    private File locatSecondarySettingsFile()
        throws Exception
    {
        File file = getFile(
            Defaults.FILE_SETTINGS_GROUP_KEY, Defaults.DEFAULT_SECONDARY_SETTINGS_FILE_LOCATION_KEY, Defaults.DEFAULT_SECONDARY_SETTINGS_FILE_NAME_KEY);

        if (file != null && file.exists())
        {
            return file;
        }

        return null;
    }

    private File locatFallbackSettingsFile()
        throws Exception
    {
        File file = getFile(Defaults.FALLBACK_SETTINGS_DIRECTORY, Defaults.FALLBACK_SETTGINS_FILE_NAME);

        if (file != null && file.exists())
        {
            return file;
        }

        return null;
    }

    /**
     * Create file if it doesn't exist.
     *
     * @param file The file to check for existance.
     * @return true if created or false if it already exists
     */
    private boolean ensureFileExists(File file)
        throws Exception
    {
        Objects.requireNonNull(file);

        if (file.exists())
        {
            return false;
        }
        else
        {
            boolean result = file.createNewFile();

            if (file.exists() && result)
            {
                return true;
            }
            else
            {
                throw new Exception("Couldn't create file \"" + file.getCanonicalPath() + "\"");
            }
        }
    }

    private boolean hasSettings(String groupKey, String directorySettingKey, String fileNameSettingKey)
    {
        FileSetting fileSetting = (FileSetting) settings.getSetting(groupKey, directorySettingKey);
        StringSetting fileName = (StringSetting) settings.getSetting(groupKey, fileNameSettingKey);

        if (fileSetting!=null && fileName!=null)
        {
            return true;
        }

        return false;
    }

    /**
     * Returns a file results when the directory, and settings file name settings send in are located.
     * @param groupKey  The settings group that houses the settings for settings files... Whew.
     * @param directorySettingKey  The directory key to use.
     * @param fileNameSettingKey  The file name key to use.
     * @return  A new file that results from the directory and file name in the settings, or null if either setting is null.
     * @throws Exception
     */
    private File getFile(String groupKey, String directorySettingKey, String fileNameSettingKey)
        throws Exception
    {
        FileSetting fileSetting = (FileSetting) settings.getSetting(groupKey, directorySettingKey);
        StringSetting fileName = (StringSetting) settings.getSetting(groupKey, fileNameSettingKey);

        if (fileSetting == null || fileName == null)
        {
            return null;
        }
        else
        {
            return getFile(fileSetting.getValue(), fileName.getValue());
        }
    }

    /**
     * Adds the name and directory together and returns a new file representing both.
     *
     * @param directory The directory to add this file name to.
     * @param fileName  The file name to add to the directory.
     * @return A new file representing the combination of the directory and file name sent in, or null if either were null.
     * @throws Exception
     */
    private File getFile(File directory, String fileName)
        throws Exception
    {
        if (directory == null || fileName == null)
        {
            return null;
        }

        File res = null;

        if (directory.isDirectory())
        {
            res = new File(directory.getCanonicalPath() + File.separatorChar + fileName);
        }
        else if (directory.isFile())
        {
            res = new File(directory.getParent() + File.separatorChar + fileName);
        }

        return res;
    }
}
