package com.bookmarkanator.xml;

import java.io.*;

public class FileSync
{
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

    public void readFromDisk()
    {
        //get file
        //if no exist create
        //validate
        //handle errors in validation or parsing.
    }
}
