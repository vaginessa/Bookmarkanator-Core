package com.bookmarking.util;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import org.apache.logging.log4j.*;
import org.reflections.*;
import org.reflections.util.*;

/**
 * The module loader loads jars from specific directories, tracks classes, and instantiates classes.
 */
public class ModuleLoader
{
    // Static fields
    private static final Logger logger = LogManager.getLogger(ModuleLoader.class.getCanonicalName());
    private static ModuleLoader moduleLoader;

    // Fields
    private Set<File> jarDirectories;
    private Map<Class, Set<Class>> classesToTrackMap;
    private ClassLoader classLoader;

    // ============================================================
    // Constructors
    // ============================================================

    public ModuleLoader()
    {
        jarDirectories = new HashSet<>();
        classesToTrackMap = new HashMap<>();
        classLoader = this.getClass().getClassLoader();
    }

    // ============================================================
    // Methods
    // ============================================================

    public ClassLoader addModulesToClasspath()
        throws Exception
    {
        return this.addJarsToClassloader();
    }

    public ClassLoader addModulesToClasspath(Collection<String> jarLocations)
        throws Exception
    {
        if (jarLocations != null && !jarLocations.isEmpty())
        {
            logger.trace("Begin loading modules...");
            for (String s : jarLocations)
            {
                logger.info("Attempting to load modules directory \'" + s + "\"");

                File f = new File(s);

                if (f.exists() || f.canRead())
                {
                    this.addDirectory(f);
                    logger.info("Success.");
                }
                else
                {
                    logger.warn("Failed to load. The file either doesn't exist or cannot be read.");
                }
            }
        }
        return this.addJarsToClassloader();
    }

    public ClassLoader addModulesToClasspath(Set<File> jarLocations)
        throws Exception
    {
        if (jarLocations != null && !jarLocations.isEmpty())
        {
            logger.trace("Begin loading modules...");
            for (File f : jarLocations)
            {
                logger.info("Attempting to load modules directory \'" + f.getCanonicalPath() + "\"");

                if (f.exists() || f.canRead())
                {
                    this.addDirectory(f);
                    logger.info("Success.");
                }
                else
                {
                    logger.warn("Failed to load. The file either doesn't exist or cannot be read.");
                }
            }
        }
        return this.addJarsToClassloader();
    }

    public void addClassToTrack(Class clazz)
    {
        if (clazz == null)
        {
            return;
        }

        logger.info("Tracking class \"" + clazz.getCanonicalName() + "\"");

        Set<Class> classes = classesToTrackMap.get(clazz.getCanonicalName());

        if (classes == null)
        {
            classesToTrackMap.put(clazz, new HashSet<>());
        }
    }

    public Set<Class> getClassesLoaded(String className)
    {
        if (className==null || className.trim().isEmpty())
        {
            return null;
        }

        try
        {
            Class clazz = Class.forName(className);
            return classesToTrackMap.get(clazz);
        }
        catch (ClassNotFoundException e)
        {
            logger.error(e);
        }

        return null;
    }

    public Set<Class> getClassesLoaded(Class clazz)
    {
        return classesToTrackMap.get(clazz);
    }

    public <T> T loadClass(String className, Class<T> toCast)
        throws Exception
    {
        logger.trace("Loading class \"" + className + "\"");
        className = className.trim();
        Class clazz = this.getClass().getClassLoader().loadClass(className);
        Class<T> sub = clazz.asSubclass(toCast);

        return sub.newInstance();
    }

    public Set<Class> getTrackedClasses()
    {
        return classesToTrackMap.keySet();
    }


    // ============================================================
    // Private Methods
    // ============================================================

    private ModuleLoader addDirectory(File jarDirectory)
    {
        this.jarDirectories.add(jarDirectory);
        return this;
    }

    private ClassLoader addJarsToClassloader()
        throws Exception
    {
        logger.trace("Adding jars to classloader");
        List<URL> urls = new ArrayList<>();

        for (File file : this.jarDirectories)
        {
            logger.trace("Inspecting files in directory \"" + file.getCanonicalPath() + "\"");
            Collection<File> jars = Util.listFiles(file.getCanonicalPath(), "jar");

            for (File jarFile : jars)
            {
                URL myJarFile = jarFile.toURI().toURL();

                logger.trace("\n\nLoading jar classes for jar " + jarFile.getName());
                ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
                for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
                {
                    logger.trace(entry.getName());
                }
                logger.trace("=====================================");
                urls.add(myJarFile);
            }
        }

        this.jarDirectories.clear();
        URL[] urlsArray = urls.toArray(new URL[urls.size()]);
        this.classLoader = new URLClassLoader(urlsArray, this.getClass().getClassLoader());

        //Getting loaded bookmark types so that when a new bookmark is created it can be selected from a list of these types.
        Reflections reflections = new Reflections(ConfigurationBuilder.build().addClassLoader(classLoader));

        logger.trace("Locating tracked classes");
        for (Class classToLoad : classesToTrackMap.keySet())
        {
            logger.trace("Adding classes of group \"" + classToLoad + "\"");
            Class clazz = classLoader.loadClass(classToLoad.getCanonicalName());
            Set<Class> classes = reflections.getSubTypesOf(clazz);

            for (Class tmpClass : classes)
            {
                logger.trace("Found class \"" + tmpClass.getCanonicalName() + "\"");
            }

            classesToTrackMap.put(classToLoad, classes);
        }

        return this.classLoader;
    }

    // ============================================================
    // Static Methods
    // ============================================================

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
