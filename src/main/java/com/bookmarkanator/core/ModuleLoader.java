package com.bookmarkanator.core;

import java.io.*;
import java.net.*;
import java.util.*;
import com.bookmarkanator.util.*;
import org.reflections.*;
import org.reflections.util.*;

public class ModuleLoader
{
    private static ModuleLoader moduleLoader;
    private Set<File> jarDirectories;
    private Map<String, Set<Class>> classesFoundMap;
    private ClassLoader classLoader;

    public ModuleLoader()
    {
        this.jarDirectories = new HashSet<>();
        classesFoundMap = new HashMap<>();
        classLoader = this.getClass().getClassLoader();
    }

    public ClassLoader addModulesToClasspath()
        throws Exception
    {
        return this.addJarsToClassloader();
    }

    public ClassLoader addModulesToClasspath(List<String> jarLocations)
        throws Exception
    {
        if (jarLocations != null && !jarLocations.isEmpty())
        {
            for (String s : jarLocations)
            {
                File f = new File(s);

                if (f.exists() || f.canRead())
                {
                    this.addDirectory(f);
                    System.out.println("Adding directory \"" + f.getCanonicalPath() + "\" to class loader paths.");
                }
                else
                {
                    System.out.println("Attempted to add \"" + s + "\" to the classloader but this file either doesn't exist or cannot be accessed.");
                }
            }
        }
        return this.addJarsToClassloader();
    }

    public void addClassToTrack(String classname)
    {
        if (classname == null)
        {
            return;
        }

        Set<Class> classes = classesFoundMap.get(classname);

        if (classes == null)
        {
            classesFoundMap.put(classname, new HashSet<>());
        }
    }

    public void addClassToTrack(Class clazz)
    {
        if (clazz == null)
        {
            return;
        }

        Set<Class> classes = classesFoundMap.get(clazz.getCanonicalName());

        if (classes == null)
        {
            classesFoundMap.put(clazz.getCanonicalName(), new HashSet<>());
        }
    }

    public Set<Class> getClassesLoaded(String className)
    {
        return classesFoundMap.get(className);
    }

    public Set<Class> getClassesLoaded(Class clazz)
    {
        return classesFoundMap.get(clazz.getCanonicalName());
    }

    public <T> T loadClass(String className, Class<T> toCast)
        throws Exception
    {
        className = className.trim();
        Class clazz = this.getClass().getClassLoader().loadClass(className);
        Class<T> sub = clazz.asSubclass(toCast);

        return sub.newInstance();
    }

    private ModuleLoader addDirectory(File jarDirectory)
    {
        this.jarDirectories.add(jarDirectory);
        return this;
    }

    private ClassLoader addJarsToClassloader()
        throws Exception
    {
        List<URL> urls = new ArrayList<>();

        for (File file : this.jarDirectories)
        {
            Collection<File> jars = Util.listFiles(file.getCanonicalPath(), "jar");

            for (File jarFile : jars)
            {
                URL myJarFile = jarFile.toURI().toURL();

                //                System.out.println("\n\nLoading jar classes for jar "+jarFile.getName());
                //                ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
                //                for (ZipEntry entry = zip.getNextEntry(); entry!=null; entry = zip.getNextEntry())
                //                {
                //                    System.out.println(entry.getName());
                //                }
                //                System.out.println("End loading jar classes.");

//                MLog.info("Loading jar \"" + myJarFile.toString() + "\"");
                urls.add(myJarFile);
            }
        }

        this.jarDirectories.clear();
        URL[] urlsArray = urls.toArray(new URL[urls.size()]);
        this.classLoader = new URLClassLoader(urlsArray, this.getClass().getClassLoader());

        //Getting loaded bookmark types so that when a new bookmark is created it can be selected from a list of these types.
        Reflections reflections = new Reflections(ConfigurationBuilder.build().addClassLoader(classLoader));
        //        bookmarkClassesFound.addAll(reflections.getSubTypesOf(AbstractBookmark.class));

        for (String s: classesFoundMap.keySet())
        {
            Class clazz = classLoader.loadClass(s);
            Set<Class> classes = reflections.getSubTypesOf(clazz);
            classesFoundMap.put(s, classes);
        }

        return this.classLoader;
    }

    public static ClassLoader getClassLoader()
    {
        return ModuleLoader.use().getClass().getClassLoader();
    }

    public static ModuleLoader use()
    {
        if (ModuleLoader.moduleLoader == null)
        {
            ModuleLoader.moduleLoader = new ModuleLoader();
        }
        return ModuleLoader.moduleLoader;
    }
}
