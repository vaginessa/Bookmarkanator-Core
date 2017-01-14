package com.bookmarkanator.core;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import com.bookmarkanator.util.*;

public class ModuleLoader
{
    private static ModuleLoader moduleLoader;
    private Set<File> jarDirectories;

    public ModuleLoader()
    {
        this.jarDirectories = new HashSet<>();
    }

    public ModuleLoader addDirectory(File jarDirectory)
    {
        this.jarDirectories.add(jarDirectory);
        return this;
    }

    public ClassLoader addJarsToClassloader(ClassLoader parentClassloader)
        throws Exception
    {
        List<URL> urls = new ArrayList<>();

        for (File file: this.jarDirectories)
        {
            Collection<File> jars = Util.listFiles(file.getCanonicalPath(), "jar");

            for (File jarFile: jars)
            {
                URL myJarFile = jarFile.toURI().toURL();

                System.out.println("\n\nLoading jar classes for jar "+jarFile.getName());
                ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
                for (ZipEntry entry = zip.getNextEntry(); entry!=null; entry = zip.getNextEntry())
                {
                    System.out.println(entry.getName());
                }
                System.out.println("End loading jar classes.");

                System.out.println("Loading jar \""+myJarFile.toString()+"\"");
                urls.add(myJarFile);
            }
        }

        this.jarDirectories.clear();
        URL[] urlsArray = urls.toArray(new URL[urls.size()]);
        ClassLoader classLoader = new URLClassLoader(urlsArray, parentClassloader);
        return classLoader;
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
