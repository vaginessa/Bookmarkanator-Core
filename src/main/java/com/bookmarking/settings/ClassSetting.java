package com.bookmarking.settings;

import com.bookmarking.structure.*;

public class ClassSetting extends AbstractSetting<Class>
{
    public ClassSetting()
    {
    }

    public ClassSetting(String key)
        throws ClassNotFoundException
    {
        super(key);
        Class.forName(key);
    }

    public ClassSetting(String group, String key, Class value)
        throws ClassNotFoundException
    {
        super(group, key, value);
        Class.forName(key);
    }

    @Override
    public void setKey(String key)
        throws Exception
    {
        super.setKey(key);
        Class.forName(key);
    }
}
