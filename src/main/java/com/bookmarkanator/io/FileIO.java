package com.bookmarkanator.io;

import java.io.*;
import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.fileservice.*;
import com.bookmarkanator.xml.*;
import org.apache.logging.log4j.*;

/**
 * This class is used solely for reading and writing bookmark files to a file system.
 */
public class FileIO implements BKIOInterface
{
    // Static fields
    private static final Logger logger = LogManager.getLogger(FileIO.class.getCanonicalName());
    private static final String FILE_IO_KEY = "FILE_IO";
    private static final String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";
    private static final String FILE_IO_SETTINGS_KEY = "FILE_IO_SETTINGS";
    private static final String DEFAULT_SETTINGS_FILE_NAME = "file-io-settings.xml";

    // Fields
    private AbstractContext context;
    private File file;
    private Settings settings;

    // ============================================================
    // Methods
    // ============================================================

    @Override
    public void init(String config)
        throws Exception
    {
        logger.trace("Entering init method with config \"" + config + "\"");
        //TODO Figure out what to do about the bookmarks.xml file getting deleted if the program has an error. Possibly create a temporary file to read from while it is running?
        this.context = this.getContext();
        this.settings = this.getSettings();

        if (config == null || config.trim().isEmpty())
        {//
            logger.trace("No file location sent in. Inferring location from settings file used.");
            File settingsFile = GlobalSettings.use().getFile();
            Objects.requireNonNull(settingsFile, "Settings file is not set.");

            String parent = settingsFile.getParent();

            String bookmarkFileName = parent + File.separatorChar + DEFAULT_BOOKMARKS_FILE_NAME;
            logger.trace("Bookmarks file inferred from settings file is \"" + bookmarkFileName + "\"");
            file = new File(bookmarkFileName);
        }
        else
        {// Using file sent in.
            file = new File(config);
        }

        // Create bookmarks file
        FileSync<AbstractContext> fileSync = new FileSync<>(new BookmarksXMLWriter(), new BookmarksXMLParser(), file);
        FileService.use().addFile(fileSync, FILE_IO_KEY);

        // Create file io settings file
        File settingsFile = createSettingsFile(file);
        FileSync<Settings> fileSync2 = new FileSync<>(new SettingsXMLWriter(), new SettingsXMLParser(), settingsFile);
        FileService.use().addFile(fileSync2, FILE_IO_SETTINGS_KEY);

        // Load both files in.
        load();
    }

    @Override
    public void save()
        throws Exception
    {
        FileSync<AbstractContext> fileSync = FileService.use().getFile(FILE_IO_KEY);
        fileSync.setObjectToWrite(context);
        fileSync.writeToDisk();

        FileSync<Settings> fileSync2 = FileService.use().getFile(FILE_IO_SETTINGS_KEY);
        fileSync2.setObjectToWrite(settings);
        fileSync2.writeToDisk();
    }

    @Override
    public void save(String filePath)
        throws Exception
    {
        FileSync<AbstractContext> fileSync = FileService.use().getFile(FILE_IO_KEY);
        File file = new File(filePath);
        fileSync.setFile(file);
        fileSync.setObjectToWrite(context);
        fileSync.writeToDisk();

        // Get settings file from current file location
        File settingsFile = createSettingsFile(file);
        FileSync<Settings> fileSync2 = FileService.use().getFile(FILE_IO_SETTINGS_KEY);
        fileSync2.setFile(settingsFile);
        fileSync2.setObjectToWrite(settings);
        fileSync2.writeToDisk();
    }

    @Override
    public void close()
    {
        //close any open file sources.
    }

    @Override
    public AbstractContext getContext()
    {
        if (this.context == null)
        {
            this.context = new FileContext();
            this.context.setBKIOInterface(this);
        }

        return context;
    }

    @Override
    public Settings getSettings()
    {
        if (settings == null)
        {
            settings = new Settings();
        }
        return settings;
    }

    @Override
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    // ============================================================
    // Private Methods
    // ============================================================

    private void load()
        throws Exception
    {
        // Load bookmarks
        FileSync<AbstractContext> fileSync = FileService.use().getFile(FILE_IO_KEY);
        fileSync.injectParsingObject(this.context);
        fileSync.readFromDisk();
        this.context = fileSync.getParsedObject();

        logger.info("Calling systemInit() on the bookmarks.");
        Set<AbstractBookmark> bks = context.getBookmarks();
        for (AbstractBookmark abs : bks)
        {
            abs.systemInit();
        }
        logger.trace("Done.");

        // Load settings
        FileSync<Settings> fileSync2 = FileService.use().getFile(FILE_IO_SETTINGS_KEY);
        fileSync2.readFromDisk();
        settings = fileSync2.getObject();
    }

    private File createSettingsFile(File input)
    {
        String path = input.getParent();
        return new File(path + File.separatorChar + DEFAULT_SETTINGS_FILE_NAME);
    }
}
