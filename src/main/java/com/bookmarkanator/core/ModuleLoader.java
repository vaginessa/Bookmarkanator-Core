package com.bookmarkanator.core;

import java.io.*;
import java.net.*;
import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.util.*;
import org.reflections.*;
import org.reflections.util.*;

public class ModuleLoader
{
    private static ModuleLoader moduleLoader;
    private Set<File> jarDirectories;
    private Set<Class<? extends AbstractBookmark>> bookmarkClassesFound;

    public ModuleLoader()
    {
        this.jarDirectories = new HashSet<>();
        bookmarkClassesFound = new HashSet<>();
    }

    public ClassLoader addModulesToClasspath(List<String> jarLocations, ClassLoader classLoader) throws Exception
    {
        if (jarLocations != null && !jarLocations.isEmpty())
        {
            for (String s : jarLocations)
            {
                File f = new File(s);

                if (f.exists() || f.canRead())
                {
                    this.addDirectory(f);
                    System.out.println("Adding directory \""+f.getCanonicalPath()+"\" to class loader paths.");
                }
                else
                {
                    System.out.println("Attempted to add \""+s+"\" to the classloader but this file either doesn't exist or cannot be accessed.");
                }
            }
        }
        return this.addJarsToClassloader(classLoader);
    }

    public Set<Class<? extends AbstractBookmark>> getBookmarkClassesFound()
    {
        return bookmarkClassesFound;
    }

    public <T> T loadClass(String className, Class<T> toCast, ClassLoader classLoader)
        throws Exception
    {
        Class clazz = classLoader.loadClass(className);
        Class<T> sub = clazz.asSubclass(toCast);

        return sub.newInstance();
    }

    private ModuleLoader addDirectory(File jarDirectory)
    {
        this.jarDirectories.add(jarDirectory);
        return this;
    }

    private ClassLoader addJarsToClassloader(ClassLoader parentClassloader)
        throws Exception
    {
        List<URL> urls = new ArrayList<>();

        for (File file: this.jarDirectories)
        {
            Collection<File> jars = Util.listFiles(file.getCanonicalPath(), "jar");

            for (File jarFile: jars)
            {
                URL myJarFile = jarFile.toURI().toURL();

//                System.out.println("\n\nLoading jar classes for jar "+jarFile.getName());
//                ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
//                for (ZipEntry entry = zip.getNextEntry(); entry!=null; entry = zip.getNextEntry())
//                {
//                    System.out.println(entry.getName());
//                }
//                System.out.println("End loading jar classes.");

                System.out.println("Loading jar \""+myJarFile.toString()+"\"");
                urls.add(myJarFile);
            }
        }

        this.jarDirectories.clear();
        URL[] urlsArray = urls.toArray(new URL[urls.size()]);
        ClassLoader classLoader = new URLClassLoader(urlsArray, parentClassloader);

        Reflections reflections = new Reflections(ConfigurationBuilder.build().addClassLoader(classLoader));
        bookmarkClassesFound.addAll(reflections.getSubTypesOf(AbstractBookmark.class));

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
