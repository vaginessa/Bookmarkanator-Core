package com.bookmarkanator.fileservice;

import java.io.*;

/**
 * Interface that needs to be implemented by things wishing to use FileSync objects.
 *
 * @param <T> The object that results from parsing a file.
 */
public interface FileReaderInterface<T>
{

    T parse(InputStream inputStream)
        throws Exception;

    void setObject(T obj);

    void validate(InputStream inputStream)
        throws Exception;

    FileSync.InvalidFilePolicy getInvalidFilePolicy();

    FileSync.MissingFilePolicy getMissingFilePolicy();

    FileSync.FileBackupPolicy getFileBackupPolicy();
}
