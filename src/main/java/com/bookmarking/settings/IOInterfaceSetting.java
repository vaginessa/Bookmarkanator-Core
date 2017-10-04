package com.bookmarking.settings;

import java.util.*;
import com.bookmarking.*;
import com.bookmarking.structure.*;
import com.bookmarking.util.*;

/**
 * This is a custom setting that is only for use in selecting the IO interface to use.
 */
public class IOInterfaceSetting extends ClassSetting
{
    public IOInterfaceSetting()
        throws Exception
    {
        super();
        this.setGroup(Bootstrap.DEFAULT_CLASSES_GROUP_NAME);
        this.setKey(Bootstrap.IO_INTERFACE_KEY);
    }

    public IOInterfaceSetting(String key)
        throws Exception
    {
        this();
        if (key!=null)
        {
            throw new Exception("Cannot change IO Interface setting key");
        }
    }

    public IOInterfaceSetting(String group, String key, Class value)
        throws Exception
    {
        this();
        if (group!=null)
        {
            throw new Exception("Cannot change Group of IO Interface setting");
        }
        else if (key!=null)
        {
            throw new Exception("Cannot change IO Interface setting key");
        }
    }

    @Override
    public void setKey(String key)
        throws Exception
    {
        throw new Exception("Cannot change IO Interface setting key");
    }

    @Override
    public void setGroup(String group)
        throws Exception
    {
        throw new Exception("Cannot change Group of IO Interface setting");
    }

    public List<Class> getOptions()
    {
        Set<Class> classes = ModuleLoader.use().getClassesLoaded(IOInterface.class);
        List res = new ArrayList(classes);
        Collections.sort(res);
        return res;
    }
}
