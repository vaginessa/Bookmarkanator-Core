package com.bookmarking.fileservice;

import java.util.*;

/**
 * This class is used to give convenient access to a map of FileSync objects.
 *
 * Usage:
 * FileSync<T> fileSync = FileService.use().getFileSync( <context string> );
 *
 * fileSync.writeToDisk();
 *
 * Or
 *
 * fileSync.readFromDisk();
 *
 * etc...
 */
public class FileService
{
    private static FileService fileService;
    private Map<String, FileSync> map;

    private FileService()
    {
        map = new HashMap<>();
    }

    /**
     * Add a FileSync object with specific context for retrieval later.
     * @param fileSync  The FileSync object to use in reading and writing the file.
     * @param context  The context used to reference this file.
     */
    public void addFile(FileSync fileSync, String context)
    {
        map.put(context, fileSync);
    }

    /**
     * Remove FileSync object from context map
     * @param context  The context the FileSync object is associated with.
     * @return  The removed FileSync object.
     */
    public FileSync deleteFile(String context)
    {
        return map.remove(context);
    }

    /**
     * Retrieve a FileSync object by context.
     * @param context  The context associated with the requested FileSync object.
     * @return  The FileSync object connected with the context string.
     */
    public FileSync getFileSync(String context)
    {
        return map.get(context);
    }

    /**
     * Singleton pattern.
     */
    public static FileService use()
    {
        if (fileService == null)
        {
            fileService = new FileService();
        }

        return fileService;
    }
}
