package com.bookmarking.fileservice;

import java.io.*;

/**
 * Interface that needs to be implemented by things wishing to use FileSync objects.
 *
 * @param <T> The object that is to be written to disk.
 */
public interface FileWriterInterface<T>
{
    void write(T object, OutputStream out)
        throws Exception;

    void writeInitial(OutputStream outputStream)
        throws Exception;

    FileSync.FileBackupPolicy getFileBackupPolicy();
}
