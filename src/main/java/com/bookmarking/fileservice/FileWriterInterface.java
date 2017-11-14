package com.bookmarking.fileservice;

import java.io.*;

/**
 * Interface that needs to be implemented by things wishing to use FileSync objects.
 *
 * @param <T> The object that is to be written to disk.
 */
public interface FileWriterInterface<T>
{

    /**
     * Writes the supplied object to disk.
     * @param object  The object to write out.
     * @param out     The output stream to write to.
     * @throws Exception
     */
    void write(T object, OutputStream out)
        throws Exception;

    /**
     * Write any initial structure this file type needs to be considered good.
     *
     * @param outputStream  The output stream to write to.
     * @throws Exception
     */
    void writeInitial(OutputStream outputStream)
        throws Exception;

    /**
     * Get the file backup policy for this FileSync object.
     * @return
     */
    FileSync.FileBackupPolicy getFileBackupPolicy();
}
