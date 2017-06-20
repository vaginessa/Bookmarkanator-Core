package com.bookmarkanator.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class FileSync
{
    private static final Logger logger = LogManager.getLogger(FileSync.class.getCanonicalName());

    private FileWriterInterface fileWriter;
    private FileReaderInterface fileReader;
    private File file;

    public void setFileWriter(FileWriterInterface fileWriter)
    {
        this.fileWriter = fileWriter;
    }

    public void setFileReader(FileReaderInterface fileReader)
    {
        this.fileReader = fileReader;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public File getFile()
    {
        return file;
    }

    public void writeToDisk()
    {
        //save file
        //handle any backups

    }

    public void readFromDisk() throws IOException {
        if (file.exists())
        {

        }
        else
        {
            logger.info("File "+file.getCanonicalPath()+" doesn't exist.");

            if (fileReader.)
        }

        //get file
        //if no exist create
        //validate
        //handle errors in validation or parsing.
    }
}
