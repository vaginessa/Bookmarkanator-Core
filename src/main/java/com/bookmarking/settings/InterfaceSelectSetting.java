package com.bookmarking.settings;

import java.lang.reflect.*;
import java.util.*;
import com.bookmarking.*;
import com.bookmarking.error.*;
import com.bookmarking.module.*;

/**
 * This class is used to allow one to select a particular implementation of an interface or abstract class. For instance
 * one could select FileIO or another implementation of the IOInterface.
 */
public class InterfaceSelectSetting extends ClassSetting
{
    private static List<Class> trackedClasses;
    private static Map<Class, List<Class>> classesMap;

    public InterfaceSelectSetting()
        throws Exception
    {
        super();
        super.setGroup(Bootstrap.IMPLEMENTING_CLASSES_GROUP);
    }

    // TODO implement proper overridden versions of all constructors.

    /**
     * This method should be used to obtain a list of possible key values. If any other value is selected for the key an
     * exception will be thrown.
     *
     * @return A sorted list of possible key values for this setting type.
     */
    public List<Class> getKeyOptions()
    {
        if (trackedClasses == null)
        {
            Set<ComparableClass> comparableClasses = new HashSet<>();

            for (Class clazz : ModuleLoader.use().getClassesBeingWatched())
            {
                int modifier = clazz.getModifiers();

                // Select only interfaces
                if (Modifier.isAbstract(modifier) || Modifier.isInterface(modifier))
                {
                    comparableClasses.add(new ComparableClass(clazz));
                }
            }

            List<ComparableClass> tmp = new ArrayList<>(comparableClasses);
            Collections.sort(tmp);

            List<Class> res = new ArrayList<>();

            for (ComparableClass comparableClass : comparableClasses)
            {
                res.add(comparableClass.getClazz());
            }

            trackedClasses = res;
        }
        return trackedClasses;
    }

    /**
     * This method returns a list of valid options given the input string as key.
     * @param potentialKey  The key one intends to assign to this object as key but hasn't is not yet in that stage of the process.
     * @return  A list of tracked classes for the supplied potentialKey converted into a class. The key must be in the list of
     * tracked classes.
     */
    public List<Class> getValueOptions(String potentialKey)
    {
        try
        {
            return getValueOptions(Class.forName(potentialKey));
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Returns a list of valid values this class can accept as the value. If any other option is selected for the value an
     * exception will be thrown.
     *
     * @param trackedClass The tracked class to use to obtain the value options for.
     * @return A sorted list of possible value options.
     */
    public List<Class> getValueOptions(Class trackedClass)
    {
        if (classesMap == null)
        {
            classesMap = new HashMap<>();

            for (Class clazz : getKeyOptions())
            {
                Set<ComparableClass> comparableClasses = new HashSet<>();

                for (Class c : ModuleLoader.use().getClassesFound(clazz))
                {
                    comparableClasses.add(new ComparableClass(c));
                }

                if (comparableClasses != null)
                {
                    List<ComparableClass> tmp = new ArrayList<>(comparableClasses);
                    Collections.sort(tmp);

                    List<Class> res = new ArrayList<>();

                    for (ComparableClass comparableClass : comparableClasses)
                    {
                        res.add(comparableClass.getClazz());
                    }

                    classesMap.put(clazz, res);
                }
                else
                {
                    classesMap.put(clazz, new ArrayList<>());
                }
            }
        }

        if (trackedClass == null && getKey() != null)
        {// Use key as the tracked class if present
            try
            {
                trackedClass = Class.forName(getKey());
            }
            catch (ClassNotFoundException e)
            {
                ErrorHandler.handle(e);
            }
        }

        List<Class> classesFound = classesMap.get(trackedClass);

        if (classesFound == null)
        {// Never return a null from this class.
            return new ArrayList<>();
        }

        return classesFound;
    }

    @Override
    public boolean isKeyValid(String key)
    {
        if (key==null)
        {
            return false;
        }

        Class clazz;

        try
        {
            clazz = Class.forName(key);
        }
        catch (Exception e)
        {
            return false;
        }

        Set<Class> options = new HashSet<>(getKeyOptions());

        if (options.contains(clazz))
        {
            return true;
        }

        return false;
    }

    @Override
    public boolean isValueValid(Class value)
    {
        return value!=null && getValueOptions(keyAsClass()).contains(value);
    }
}
