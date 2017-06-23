package com.bookmarkanator.xml;

import java.io.*;
import org.apache.logging.log4j.*;

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
            FileInputStream fin = new FileInputStream(file);

            try
            {
                fileReader.validate(fin);
                fin.close();

                if (fileReader.getFileBackupPolicy().equals(FileReaderInterface.FileBackupPolicy.singleBackupOnRead))
                {

                }

                fin = new FileInputStream(file);
                obj = fileReader.parse(fin);
                fin.close();
            }
            catch (Exception e)
            {
                if (fileReader.getInvalidFilePolicy()== FileReaderInterface.InvalidFilePolicy.markBadAndContinue)
                {
                    File newFile = new File(file.getPath()+File.separatorChar+file.getName()+".bad");
                    file.renameTo(newFile);
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
            logger.info("File "+file.getCanonicalPath()+" doesn't exist.");
            if (fileReader.getMissingFilePolicy()== FileReaderInterface.MissingFilePolicy.createNew)
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
                    fout.flush();
                    fout.close();
                }
            }
            else
            {
                throw new FileNotFoundException(file.getCanonicalPath());
            }
        }
    }

    public T getParsedObject()
    {
        return obj;
    }
}
