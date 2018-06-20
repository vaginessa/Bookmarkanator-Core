package com.bookmarking.settings;

import java.io.*;
import java.util.*;
import com.bookmarking.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.settings.types.*;
import org.apache.logging.log4j.*;

/**
 * This class is used to read/write settings files. It uses the default settings, or supplied settings to locate the settings file to read in.
 * If there is no settings file it will create one with the supplied settings, or the default settings if none are supplied.
 */
public class FileSettingsIO implements SettingsIOInterface
{
    private static final Logger logger = LogManager.getLogger(FileSettingsIO.class.getCanonicalName());

    private Settings settings;
    private boolean skipUsingFileSystem;

    public FileSettingsIO()
    {
        skipUsingFileSystem = true;
    }

    @Override
    public Settings init(Settings settings)
        throws Exception
    {
        logger.info("- Init FileSettingsIO");
        this.settings = settings;
        this.skipUsingFileSystem = this.settings.getBooleanSetting(Defaults.FILE_IO_SETTINGS_GROUP,Defaults.USE_FILE_SYSTEM);

        if (!skipUsingFileSystem)
        {
            // Locate the settings file based on settings being brought in, and check if it exists.
            File fileInUse;
            File primarySettingsFile = locatPrimarySettingsFile();
            File secondarySettingsFile = locatSecondarySettingsFile();

            if (primarySettingsFile != null)
            {
                logger.info("- Using primary file setting of \"" + primarySettingsFile + "\"");
                fileInUse = primarySettingsFile;
            }
            else if (secondarySettingsFile != null)
            {
                logger.info("- Using secondary file setting of \"" + secondarySettingsFile + "\"");
                fileInUse = secondarySettingsFile;
            }
            else
            {
                throw new FileNotFoundException("No settings file located.");
            }

            // If settings file is still null then no existing settings file can be found... Create one by using the settings in order of importance.
            if (fileInUse == null)
            {
                logger.info("- Settings file doesn't exist. Creating now...");
                if (hasFileSettings(Defaults.DIRECTORIES_GROUP, Defaults.PRIMARY_FILE_LOCATION_KEY, Defaults.SETTINGS_FILE_NAME))
                {
                    fileInUse = getFile(Defaults.DIRECTORIES_GROUP, Defaults.PRIMARY_FILE_LOCATION_KEY, Defaults.SETTINGS_FILE_NAME);
                    ensureFileExists(fileInUse);
                    logger.info("-- Using primary setting location of \"" + fileInUse + "\"");
                }
                else if (hasFileSettings(Defaults.DIRECTORIES_GROUP, Defaults.SECONDARY_FILE_LOCATION_KEY, Defaults.SETTINGS_FILE_NAME))
                {
                    fileInUse = getFile(Defaults.FILE_IO_SETTINGS_GROUP, Defaults.SECONDARY_FILE_LOCATION_KEY, Defaults.SETTINGS_FILE_NAME);
                    ensureFileExists(fileInUse);
                    logger.info("-- Using secondary setting location \"" + fileInUse + "\"");
                }
                else
                {// No settings have been found, use the fallback settings to create a file.
                    fileInUse = getFile(Defaults.PRIMARY_DIRECTORY, Defaults.SETTINGS_FILE_NAME);
                    ensureFileExists(fileInUse);
                    logger.info("-- Using fallback setting location \"" + fileInUse + "\"");
                }

                logger.info("-- Created settings file at \"" + fileInUse + "\n");
            }

            // Read settings file in...
            FileSync<Settings> fileSync = new FileSync<>(new SettingsXMLWriter(), new SettingsXMLParser(), fileInUse);
            FileService.use().addFile(fileSync, Defaults.SETTINGS_FILE_CONTEXT);

            fileSync.readFromDisk();

            // Get parsed settings file.
            Settings parsedSettings = fileSync.getObject();
            Objects.requireNonNull(parsedSettings);

            parsedSettings.importSettings(settings);

            // Write the settings out again with defaults merged in.
            fileSync.writeToDisk();

            FileSetting fileSetting = new FileSetting(Defaults.DIRECTORIES_GROUP, Defaults.SELECTED_FILE_LOCATION_KEY, fileInUse);
            this.settings.putSetting(fileSetting);
        }
        else
        {
            logger.info("Not using file system as indicated by supplied settings");
            FileSetting fileSetting = new FileSetting(Defaults.DIRECTORIES_GROUP, Defaults.SELECTED_FILE_LOCATION_KEY, Defaults.PRIMARY_DIRECTORY);
            this.settings.putSetting(fileSetting);
        }
        logger.info("- Done.");
        return settings;
    }

    @Override
    public Settings getSettings()
    {
        return this.settings;
    }

    @Override
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    @Override
    public void save()
        throws Exception
    {
        if (skipUsingFileSystem)
        {
            FileService.use().getFileSync(Defaults.SETTINGS_FILE_CONTEXT).writeToDisk();
        }
    }

    @Override
    public void prepExit()
        throws Exception
    {
        if (skipUsingFileSystem)
        {
            FileService.use().getFileSync(Defaults.SETTINGS_FILE_CONTEXT).writeToDisk();
        }
    }

    @Override
    public void exit()
    {
        // Nothing to do here?
    }

    private File locatPrimarySettingsFile()
        throws Exception
    {
        File file = this.settings.getFileSetting(Defaults.DIRECTORIES_GROUP, Defaults.PRIMARY_FILE_LOCATION_KEY);

        if (file != null && file.exists())
        {
            return file;
        }

        return null;
    }

    private File locatSecondarySettingsFile()
        throws Exception
    {
        File file = this.settings.getFileSetting(Defaults.DIRECTORIES_GROUP, Defaults.SECONDARY_FILE_LOCATION_KEY);

        if (file != null && file.exists())
        {
            return file;
        }

        return null;
    }

//    private File locatFallbackSettingsFile()
//        throws Exception
//    {
//        File file = getFile(Defaults.SECONDARY_DIRECTORY, Defaults.SETTINGS_FILE_NAME);
//
//        if (file != null && file.exists())
//        {
//            return file;
//        }
//
//        return null;
//    }

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

    /**
     * Easy method to determine if a complete file setting is present.
     * @param groupKey  The group that houses the settings in question.
     * @param directorySettingKey  Directory setting that will be combined with the file name setting.
     * @param fileNameSettingKey  The file name setting that will be combined with the directory setting to form a complete file location.
     * @return  True if both of the settings are not null.
     */
    private boolean hasFileSettings(String groupKey, String directorySettingKey, String fileNameSettingKey)
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
