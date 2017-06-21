package com.bookmarkanator.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class FileSync <T>
{
    private static final Logger logger = LogManager.getLogger(FileSync.class.getCanonicalName());

    private FileWriterInterface<T> fileWriter;
    private FileReaderInterface<T> fileReader;
    private File file;
    private T obj;

    public FileSync(FileWriterInterface fileWriter, FileReaderInterface fileReader, File file) {
        this.fileWriter = fileWriter;
        this.fileReader = fileReader;
        this.file = file;
    }

    public File getFile()
    {
        return file;
    }

    public T getObject()
    {
        return obj;
    }

    public void writeToDisk()
    {
        //save file
        //handle any backups

    }

    public void readFromDisk() throws Exception {

        if (file.exists())
        {
            try
            {
                FileInputStream fin = new FileInputStream(file);
                fileReader.validate(fin);
                fin.close();

                fin = new FileInputStream(file);
                obj = fileReader.parse(fin);
                fin.close();
            }
            catch (Exception e)
            {
                if (fileReader.getInvalidFilePolicy()== FileReaderInterface.InvalidFilePolicy.markBadAndContinue)
                {
                    // TODO mark file bad and continue
                }
                else
                {
                    throw e;
                }
            }
        }
        else
        {
            logger.info("File "+file.getCanonicalPath()+" doesn't exist.");
            if (fileReader.getMissingFilePolicy()== FileReaderInterface.MissingFilePolicy.createNew)
            {
                // TODO create file here.
            }
            else
            {
                throw new FileNotFoundException(file.getCanonicalPath());
            }
        }

        //get file
        //if no exist create
        //validate
        //handle errors in validation or parsing.
    }

    public T getParsedObject()
    {
        return obj;
    }
}
