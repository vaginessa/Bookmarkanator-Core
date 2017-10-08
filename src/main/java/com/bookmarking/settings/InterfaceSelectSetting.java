package com.bookmarking.settings;

import java.lang.reflect.*;
import java.util.*;
import com.bookmarking.util.*;

public class InterfaceSelectSetting extends ClassSetting
{
    private static List<Class> trackedClasses;
    private static Map<Class, List<Class>> classesMap;

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

            for (Class clazz : ModuleLoader.use().getTrackedClasses())
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

                for (Class c : ModuleLoader.use().getClassesLoaded(clazz))
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
                e.printStackTrace();
            }
        }

        List<Class> classesFound = classesMap.get(trackedClass);

        if (classesFound == null)
        {// Never return a null from this class.
            return new ArrayList<>();
        }

        return classesFound;
    }
}
