package com.bookmarkanator.xml;

import java.util.*;

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

    public static FileService use()
    {
        if (fileService==null)
        {
            fileService = new FileService();
        }

        return fileService;
    }
}
