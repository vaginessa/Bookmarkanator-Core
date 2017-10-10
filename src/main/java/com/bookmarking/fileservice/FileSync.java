package com.bookmarking.fileservice;

import java.io.*;
import java.util.*;
import com.bookmarking.file.*;
import org.apache.commons.io.*;
import org.apache.logging.log4j.*;

/**
 * A class used to read in, validate, and write out information to files on the file system.
 *
 * @param <T> The type of object that is supplied to the reader and writer interfaces.
 */
public class FileSync<T>
{
    private static final Logger logger = LogManager.getLogger(FileSync.class.getCanonicalName());

    // ============================================================
    // Enums
    // ============================================================

    public enum FileBackupPolicy
    {
        SINGLE_BACKUP, TIMESTAMP_BACKUP, NO_BACKUP
    }

    public enum InvalidFilePolicy
    {
        markBadAndContinue, thowError,
    }

    public enum MissingFilePolicy
    {
        createNew, thowError,
    }

    // ============================================================
    // Fields
    // ============================================================

    private FileWriterInterface<T> fileWriter;
    private FileReaderInterface<T> fileReader;
    private File file;
    private T obj;

    // ============================================================
    // Constructors
    // ============================================================

    public FileSync(FileWriterInterface<T> fileWriter, FileReaderInterface<T> fileReader, File file)
    {
        Objects.requireNonNull(fileWriter);
        Objects.requireNonNull(fileReader);
        Objects.requireNonNull(file);

        this.fileWriter = fileWriter;
        this.fileReader = fileReader;
        this.file = file;
    }

    // ============================================================
    // Public methods
    // ============================================================

    /**
     * Get the file that will be written or read
     *
     * @return The file referenced by this FileSync object.
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Set the file that will be written or read
     */
    public void setFile(File file)
    {
        this.file = file;
    }

    /**
     * Gets the object parsed if the parser creates it's own result object. If not it needs to be supplied
     * with an object to fill with data using the injectParsingObject method.
     *
     * @return The parsed object.
     */
    public T getObject()
    {
        return obj;
    }

    /**
     * @param obj An object to fill when parsed if the parser doesn't create it's own object.
     */
    public void injectParsingObject(T obj)
    {
        fileReader.setObject(obj);
    }

    /**
     * Writes the file to disk using the supplied writer.
     *
     * @throws Exception
     */
    public void writeToDisk()
        throws Exception
    {
        FileOutputStream fout;

        if (!file.exists())
        {
            boolean fileCreated = file.createNewFile();

            if (fileCreated)
            {
                fout = new FileOutputStream(file);

                writeInitial(fout);
            }
            else
            {
                logger.error("Could not create file.");
            }
        }

        handleBackup(file, fileWriter.getFileBackupPolicy());

        fout = new FileOutputStream(file);

        try
        {
            fileWriter.write(obj, fout);
        }
        finally
        {
            if (fout.getChannel().isOpen())
            {
                fout.flush();
                fout.close();
            }
        }
    }

    /**
     * Reads a file from disk using the supplied parser
     *
     * @throws Exception
     */
    public void readFromDisk()
        throws Exception
    {

        if (file.exists())
        {
            if (file.length() == 0)
            {// Write initial xml if file contains nothing
                FileOutputStream fout = new FileOutputStream(file);
                try
                {
                    fileWriter.writeInitial(fout);
                }
                finally
                {
                    if (fout.getChannel().isOpen())
                    {
                        fout.close();
                    }
                }
            }

            FileInputStream fin = new FileInputStream(file);

            try
            {
                obj = fileReader.getObject();
                fileReader.validate(fin);
                fin.close();

                handleBackup(file, fileReader.getFileBackupPolicy());

                fin = new FileInputStream(file);
                fileReader.parse(fin);
                fin.close();
            }
            catch (Exception e)
            {
                if (fin.getChannel().isOpen())
                {
                    fin.close();
                }

                logger.error(e);
                if (fileReader.getInvalidFilePolicy().equals(InvalidFilePolicy.markBadAndContinue))
                {
                    File newFile = new File(file.getParentFile().getPath() + File.separatorChar + file.getName() + ".bad");

                    // Keeping this here just in case we are unable to rename file on other operating systems.
//                    Files.move(file.toPath(), newFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    logger.info("Attempting to rename bad file... ");
                    if (file.renameTo(newFile))
                    {
                        logger.info("Success! Renamed \""+file.getCanonicalPath()+"\" file to \""+newFile.getCanonicalPath()+"\"");
                    }
                    else
                    {
                        logger.error("Couldn't rename bad file. " + file.getCanonicalPath());
                    }
                }
                else
                {
                    throw e;
                }
            }
            finally
            {
                if (fin.getChannel().isOpen())
                {
                    fin.close();
                }
            }
        }
        else
        {
            logger.info("File " + file.getCanonicalPath() + " doesn't exist.");
            if (fileReader.getMissingFilePolicy() == MissingFilePolicy.createNew)
            {
                boolean madeDirectory = file.getParentFile().mkdirs();
                if (madeDirectory)
                {
                    boolean fileCreated = file.createNewFile();

                    if (fileCreated)
                    {
                        FileOutputStream fout = new FileOutputStream(file);

                        writeInitial(fout);

                        FileInputStream fin = new FileInputStream(file);

                        try
                        {
                            obj = fileReader.parse(fin);
                        }
                        finally
                        {
                            if (fin.getChannel().isOpen())
                            {
                                fin.close();
                            }
                        }
                    }
                }
                else
                {
                    logger.error("Couldn't make directory "+file.getCanonicalPath());
                }
            }
            else
            {
                throw new FileNotFoundException(file.getCanonicalPath());
            }
        }
    }

    public FileWriterInterface<T> getFileWriter()
    {
        return fileWriter;
    }

    public void setFileWriter(FileWriterInterface<T> fileWriter)
    {
        this.fileWriter = fileWriter;
    }

    public FileReaderInterface<T> getFileReader()
    {
        return fileReader;
    }

    public void setFileReader(FileReaderInterface<T> fileReader)
    {
        this.fileReader = fileReader;
    }

    // ============================================================
    // Private methods
    // ============================================================

    private void writeInitial(FileOutputStream fout)
        throws Exception
    {
        try
        {
            fileWriter.writeInitial(fout);
        }
        finally
        {
            if (fout.getChannel().isOpen())
            {
                fout.flush();
                fout.close();
            }
        }
    }

    private void handleBackup(File fileToBackup, FileBackupPolicy policy)
        throws IOException
    {
        if (policy == FileBackupPolicy.SINGLE_BACKUP)
        {
            File backup = singleBackup(fileToBackup);
            if (backup != null)
            {
                logger.info("Backed up file to " + backup.getCanonicalPath());
            }
            else
            {
                logger.error("A backup file could not be created.");
            }
        }
        else if (policy.equals(FileBackupPolicy.TIMESTAMP_BACKUP))
        {
            File backup = timeBackup(fileToBackup);
            if (backup != null)
            {
                logger.info("Backed up file to " + backup.getCanonicalPath());
            }
            else
            {
                logger.error("A backup file could not be created.");
            }
        }
        else
        {
            logger.warn("No backup policy matched.");
        }
    }

    private File singleBackup(File fileToBackup)
        throws IOException
    {
        File tmp = new File(fileToBackup.getCanonicalPath() + ".backup");
        if (!tmp.exists())
        {
            tmp.createNewFile();
        }

        FileUtils.copyFile(fileToBackup, tmp);
        return tmp;
    }

    private File timeBackup(File fileToBackup)
        throws IOException
    {
        String ext = FilenameUtils.getExtension(fileToBackup.getCanonicalPath());
        String name = FilenameUtils.getBaseName(fileToBackup.getCanonicalPath());
        String path = fileToBackup.getPath();

        File tmp = new File(path + File.separatorChar + name + "-" + System.currentTimeMillis() + ext + ".backup");
        if (!tmp.exists())
        {
            tmp.createNewFile();
        }

        FileUtils.copyFile(fileToBackup, tmp);
        return tmp;
    }

    public T getParsedObject()
    {
        return obj;
    }

    public void setObjectToWrite(T obj)
    {
        this.obj = obj;
    }
}
