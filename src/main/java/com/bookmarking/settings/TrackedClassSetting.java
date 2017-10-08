package com.bookmarking.settings;

import java.util.*;
import com.bookmarking.*;
import com.bookmarking.structure.*;
import com.bookmarking.util.*;

/**
 * This setting allows the overriding of tracked classes.
 * <p>
 * The only valid values for this setting are classes that are tracked by the module loader.
 */
public class TrackedClassSetting extends ClassSetting
{

    private static final Set<Class> whiteList;

    static
    {
        whiteList = new HashSet<>();
        whiteList.add(IOInterface.class);
    }

    private static List<Class> trackedClasses;
    private static Map<Class, List<Class>> classesMap;

    public TrackedClassSetting()
        throws Exception
    {
        super();
        super.setGroup(Bootstrap.TRACKED_CLASSES_GROUP);
    }

    public TrackedClassSetting(String key)
        throws Exception
    {
        this();

    }

    public TrackedClassSetting(String group, String key, Class value)
        throws Exception
    {
        this();
        if (group != null)
        {
            throw new Exception("Cannot change Group of IO Interface setting");
        }

        // Verify this class is on the classpath
        Class clazz = Class.forName(key);

        if (clazz != null)
        {
            this.key = key;
        }

        if (ModuleLoader.use().getTrackedClasses().contains(value))
        {
            if (value.isAssignableFrom(clazz))
            {
                this.value = value;
            }
        }
    }

    @Override
    public void setGroup(String group)
        throws Exception
    {
        throw new Exception("Cannot change Group of overriding classes setting");
    }

    @Override
    public void setValue(Class value)
        throws Exception
    {
        Objects.requireNonNull(this.getKey(), "The key must not be null, and must be in the tracked class list." +
            " Please set the key prior to setting the value.");

        Class clazz = Class.forName(key);

        if (ModuleLoader.use().getClassesLoaded(getKey()).contains(clazz))
        {
            setValue(clazz);
        }
        else
        {
            throw new Exception("Value must be present in list of tracked classes for key");
        }
    }

    @Override
    public void setKey(String key)
        throws Exception
    {
        Objects.requireNonNull(key);

        Class clazz = Class.forName(key);

        if (!trackedClasses.contains(clazz))
        {
            throw new Exception("Cannot accept class \""+key+"\" as it is not a tracked class");
        }

        super.setKey(key);
    }

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

            for (Class clazz: ModuleLoader.use().getTrackedClasses())
            {
                if (whiteList.contains(clazz))
                {
                    comparableClasses.add(new ComparableClass(clazz));
                }
            }

            List<ComparableClass> tmp = new ArrayList<>(comparableClasses);
            Collections.sort(tmp);

            List<Class> res = new ArrayList<>();

            for (ComparableClass comparableClass: comparableClasses)
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

            for (Class clazz: getKeyOptions())
            {
                Set<ComparableClass> comparableClasses = new HashSet<>();

                for (Class c: ModuleLoader.use().getClassesLoaded(clazz))
                {
                    comparableClasses.add(new ComparableClass(c));
                }

                if (comparableClasses!=null)
                {
                    List<ComparableClass> tmp = new ArrayList<>(comparableClasses);
                    Collections.sort(tmp);

                    List<Class> res = new ArrayList<>();

                    for (ComparableClass comparableClass: comparableClasses)
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

        if (trackedClass==null && getKey() !=null)
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

        if (classesFound==null)
        {// Never return a null from this class.
            return new ArrayList<>();
        }

        return classesFound;
    }

    private class ComparableClass implements Comparable<ComparableClass>
    {
        private Class clazz;

        public ComparableClass(Class clazz)
        {
            this.clazz = clazz;
        }

        public Class getClazz()
        {
            return clazz;
        }

        public void setClazz(Class clazz)
        {
            this.clazz = clazz;
        }


        @Override
        public int compareTo(ComparableClass o)
        {
            return clazz.getCanonicalName().compareTo(o.getClazz().getCanonicalName());
        }
    }
}
