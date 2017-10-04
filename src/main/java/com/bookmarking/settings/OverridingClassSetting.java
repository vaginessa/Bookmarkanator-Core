package com.bookmarking.settings;

import java.util.*;
import com.bookmarking.*;
import com.bookmarking.structure.*;
import com.bookmarking.util.*;

public class OverridingClassSetting extends ClassSetting
{
    public OverridingClassSetting()
        throws Exception
    {
        super();
        this.setGroup(Bootstrap.OVERRIDDEN_CLASSES);
    }

    public OverridingClassSetting(String key)
        throws Exception
    {
        this();

    }

    public OverridingClassSetting(String group, String key, Class value)
        throws Exception
    {
        this();
        if (group!=null)
        {
            throw new Exception("Cannot change Group of IO Interface setting");
        }

        // Verify this class is on the classpath
        Class clazz = Class.forName(key);

        if (clazz!=null)
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
        throws ClassNotFoundException
    {
        Class clazz = Class.forName(key);

        if (ModuleLoader.use().getTrackedClasses().contains(value))
        {
            if (value.isAssignableFrom(clazz))
            {
                setValue(value);
            }
        }
    }

    @Override
    public void setKey(String key)
        throws Exception
    {
        Class clazz = Class.forName(key);
        setKey(key);
    }

    /**
     * The key must be a super class
     * @return
     */
    public List<Class> getKeyOptions()
    {
//        Set<Class> classes = ModuleLoader.use().getTrackedClasses();
//        List res = new ArrayList(classes);
//        Collections.sort(res);
//        return res;
        return null;
    }

    public List<Class> getValueOptions()
    {
        Set<Class> classes = ModuleLoader.use().getClassesLoaded(IOInterface.class);
        List res = new ArrayList(classes);
        Collections.sort(res);
        return res;
    }
}
