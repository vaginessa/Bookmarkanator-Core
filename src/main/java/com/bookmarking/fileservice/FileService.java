package com.bookmarking.fileservice;

import java.util.*;

/**
 * This class is used to give convenient access to a map of FileSync objects.
 */
public class FileService
{
    private static FileService fileService;
    private Map<String, FileSync> map;

    private FileService()
    {
        map = new HashMap<>();
    }

    public void addFile(FileSync fileSync, String context)
    {
        map.put(context, fileSync);
    }

    public FileSync deleteFile(String context)
    {
        return map.remove(context);
    }

    public FileSync getFile(String context)
    {
        return map.get(context);
    }

    public static FileService use()
    {
        if (fileService == null)
        {
            fileService = new FileService();
        }

        return fileService;
    }
}
