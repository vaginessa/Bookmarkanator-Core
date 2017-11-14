package com.bookmarking.fileservice;

import java.io.*;

/**
 * Interface that needs to be implemented by things wishing to use FileSync objects.
 *
 * @param <T> The object that results from parsing a file.
 */
public interface FileReaderInterface<T>
{

    /**
     * Parse the input stream to obtain object T
     * @param inputStream  The input stream to parse
     * @return      The object T that was parsed from the file.
     * @throws Exception
     */
    T parse(InputStream inputStream)
        throws Exception;

    /**
     * Set the object to populate during parsing. Present if one needs to initialize the object elseware, and still have it populated.
     *
     * Note: The implementing class should create the object T if none is supplied.
     * @param obj  The object to populate during parsing.
     */
    void setObject(T obj);

    /**
     * Get the object that was populated by the parsing.
     * @return
     */
    T getObject();

    void validate(InputStream inputStream)
        throws Exception;

    /**
     * @return  The intended action to take when an invalid file is encountered
     */
    FileSync.InvalidFilePolicy getInvalidFilePolicy();

    /**
     * @return  The intended action to take is no file is found.
     */
    FileSync.MissingFilePolicy getMissingFilePolicy();

    /**
     * @return  The intended backup action to take (if any)
     */
    FileSync.FileBackupPolicy getFileBackupPolicy();
}
