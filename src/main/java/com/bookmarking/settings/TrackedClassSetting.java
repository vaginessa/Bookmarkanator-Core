package com.bookmarking.settings;

import java.util.*;
import com.bookmarking.*;
import com.bookmarking.util.*;

/**
 * This setting allows the overriding of tracked classes.
 * <p>
 * The only valid values for this setting are classes that are tracked by the module loader.
 */
public class TrackedClassSetting extends ClassSetting
{
    private static List<Class> trackedClasses;
    private static Map<Class, List<Class>> classesMap;

    public TrackedClassSetting()
        throws Exception
    {
        super();
        this.setGroup(Bootstrap.TRACKED_CLASSES_GROUP);
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

        setKey(key);
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
            Set<Class> classes = ModuleLoader.use().getTrackedClasses();
            List res = new ArrayList(classes);
            Collections.sort(res);
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
                Set<Class> classesSet = ModuleLoader.use().getClassesLoaded(clazz);

                if (classesSet!=null)
                {
                    List res = new ArrayList(classesSet);
                    Collections.sort(res);
                    classesMap.put(clazz, res);
                }
                else
                {
                    classesMap.put(clazz, new ArrayList<>());
                }
            }
        }

        List<Class> classesFound = classesMap.get(trackedClass);

        if (classesFound==null)
        {// Never return a null from this class.
            return new ArrayList<>();
        }

        return classesFound;
    }
}
