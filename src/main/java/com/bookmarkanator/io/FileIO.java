package com.bookmarkanator.io;

import java.io.*;
import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
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
    public static String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";
    public static String DEFAULT_BACKUP_FILE_NAME = "bookmarks.backup.xml";

    // Fields
    private AbstractContext context;
    private File file;

    // ============================================================
    // Methods
    // ============================================================

    @Override
    public void init(String config)
        throws Exception
    {
        logger.trace("Entering init method with config \""+config+"\"");
        //TODO Figure out what to do about the bookmarks.xml file getting deleted if the program has an error. Possibly create a temporary file to read from while it is running?
        this.context = this.getContext();

        if (config == null || config.trim().isEmpty())
        {//
            logger.trace("No file location sent in. Inferring location from settings file used.");
            File settingsFile = GlobalSettings.use().getFile();
            Objects.requireNonNull(settingsFile, "Settings file is not set.");

            String parent = settingsFile.getParent();

            String bookmarkFileName = parent + File.separatorChar + DEFAULT_BOOKMARKS_FILE_NAME;
            logger.trace("Bookmarks file inferred from settings file is \""+bookmarkFileName+"\"");
            file = new File(bookmarkFileName);
        }
        else
        {// Using file sent in.
            file = new File(config);
        }

        FileSync<AbstractContext> fileSync = new FileSync<>(new BookmarksXMLWriter(), new BookmarksXMLParser(), file);
        FileService.use().addFile(fileSync, FILE_IO_KEY);
        loadBookmarks();
    }

    @Override
    public void save()
        throws Exception
    {
        FileSync<AbstractContext> fileSync = FileService.use().getFile(FILE_IO_KEY);
        fileSync.setObjectToWrite(context);
        fileSync.writeToDisk();
    }

    @Override
    public void save(String config)
        throws Exception
    {
        FileSync<AbstractContext> fileSync = FileService.use().getFile(FILE_IO_KEY);
        File file = new File(config);
        fileSync.setFile(file);
        fileSync.setObjectToWrite(context);
        fileSync.writeToDisk();
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

    // ============================================================
    // Private Methods
    // ============================================================

    private void loadBookmarks()
        throws Exception
    {
        FileSync<AbstractContext> fileSync = FileService.use().getFile(FILE_IO_KEY);
        fileSync.injectParsingObject(this.context);
        fileSync.readFromDisk();
        this.context = fileSync.getParsedObject();

        logger.info("Calling systemInit() on the bookmarks.");
        Set<AbstractBookmark> bks = context.getBookmarks();
        for (AbstractBookmark abs: bks)
        {
            abs.systemInit();
        }
        logger.trace("Done.");
    }


}
