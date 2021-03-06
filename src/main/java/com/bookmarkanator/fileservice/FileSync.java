package com.bookmarkanator.fileservice;

import java.io.*;
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

    public FileSync(FileWriterInterface fileWriter, FileReaderInterface fileReader, File file)
    {
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
            file.createNewFile();
            fout = new FileOutputStream(file);

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
                fileReader.validate(fin);
                fin.close();

                handleBackup(file, fileReader.getFileBackupPolicy());

                fin = new FileInputStream(file);
                obj = fileReader.parse(fin);
                fin.close();
            }
            catch (Exception e)
            {
                if (fileReader.getInvalidFilePolicy().equals(InvalidFilePolicy.markBadAndContinue))
                {
                    File newFile = new File(file.getPath() + File.separatorChar + file.getName() + ".bad");
                    if (!file.renameTo(newFile))
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
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fout = new FileOutputStream(file);

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
