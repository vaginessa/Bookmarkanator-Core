package com.bookmarkanator.core;

import java.io.*;
import java.net.*;
import java.util.*;

public class ModuleLoader
{
    private static ModuleLoader moduleLoader;
    private Set<File> jarDirectories;

    public ModuleLoader()
    {
        jarDirectories = new HashSet<>();
    }

    public ModuleLoader addDirectory(File jarDirectory)
    {
        jarDirectories.add(jarDirectory);
        return this;
    }

    public ClassLoader addJarsToClassloader(ClassLoader classLoader)
        throws Exception
    {
        URL[] urls = new URL[jarDirectories.size()];
        int c = 0;

        for (File file: jarDirectories)
        {
            URL myJarFile = new URL("jar", "", "file:" +file.getCanonicalPath());
            urls[c] = myJarFile;
            c = c+1;
        }

        jarDirectories.clear();
        return new URLClassLoader(urls, classLoader);
    }

    public static ModuleLoader use()
    {
        if (ModuleLoader.moduleLoader==null)
        {
            ModuleLoader.moduleLoader = new ModuleLoader();
        }
        return ModuleLoader.moduleLoader;
    }
}
