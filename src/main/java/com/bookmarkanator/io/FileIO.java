package com.bookmarkanator.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.xml.*;
import org.apache.commons.io.*;
import org.apache.logging.log4j.*;

/**
 * This class is used solely for reading and writing bookmark files to a file system.
 */
public class FileIO implements BKIOInterface
{
    // Static fields
    private static final Logger logger = LogManager.getLogger(FileIO.class.getCanonicalName());
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

        if (!file.exists() || file.length()==0)
        {// Fill file with default xml tags.
            logger.trace("File \""+file.getCanonicalPath()+"\" was present but empty. Adding initial xml tags.");
            this.save();
        }
        else
        {// Parse existing xml
            logger.trace("Reading file \""+file.getCanonicalPath()+"\" in.");
            FileInputStream fin = new FileInputStream(file);
            validateXML(fin);
            fin = new FileInputStream(file);
            loadBookmarks(fin);
            fin.close();
        }

        // Backup the bookmarks file every time the program starts up.
        createSingleBookmarksBackup();
    }

    @Override
    public void save()
        throws Exception
    {
        logger.trace("Saving file to \""+file.getCanonicalPath()+"\"");
        FileOutputStream fout = new FileOutputStream(file);
        BookmarksXMLWriter writer = new BookmarksXMLWriter(context, fout);
        writer.write();
        fout.flush();
        fout.close();

//        if (Bootstrap.context().isDirty())
//        {
//            createRollingBookmarksBackup(new File(bookmarksFileLocation));
//        }
        logger.trace("Done.");
    }

    @Override
    public void save(String config)
        throws Exception
    {
        logger.info("Calling systemShuttingDown() on all the bookmarks.");
        Set<AbstractBookmark> bks = context.getBookmarks();
        for (AbstractBookmark abs: bks)
        {
            abs.systemShuttingDown();
        }
        logger.trace("Done.");

        logger.trace("Writing bookmarks file to \""+config+"\"");
        FileOutputStream fout = new FileOutputStream(new File(config));
        BookmarksXMLWriter writer = new BookmarksXMLWriter(context, fout);
        writer.write();
        fout.flush();
        fout.close();
        logger.trace("Done.");
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

    private void loadBookmarks(InputStream inputStream)
        throws Exception
    {
        logger.trace("Parsing bookmarks...");
        BookmarksXMLParser parser = new BookmarksXMLParser(context, inputStream);
        parser.parse();
        logger.trace("Done.");

        logger.info("Calling systemInit() on the bookmarks.");
        Set<AbstractBookmark> bks = context.getBookmarks();
        for (AbstractBookmark abs: bks)
        {
            abs.systemInit();
        }
        logger.trace("Done.");
    }

    private void createRollingBookmarksBackup()
    {
        logger.trace("Creating rolling backup file");
        Date date = new Date();
        String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
        String extension = FilenameUtils.getExtension(file.getName());
        File file2 = new File(file.getParent() + File.separator + date.toString() + "-" + fileNameWithOutExt + ".backup." + extension);
        try
        {
            logger.info("Original file: \""+file.getCanonicalPath()+" backup: \""+file2.getCanonicalPath()+"\"");
            Files.copy(file.toPath(), file2.toPath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void createSingleBookmarksBackup()
    {
        logger.trace("Creating single backup file.");
        File existingBackup = new File(file.getParent()+File.separatorChar+DEFAULT_BACKUP_FILE_NAME);
        File tmp = file;
        file = existingBackup;
        try
        {
            logger.info("Original file: \""+tmp.getCanonicalPath()+"\" backup: \""+existingBackup.getCanonicalPath()+"\"");
            this.save();
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        file = tmp;
    }

    private void validateXML(InputStream inputStream)
        throws Exception
    {
        logger.trace("Validating xml");
        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/BookmarksStructure.xsd");
        XMLValidator.validate(inputStream, xsd);
    }
}
