package com.bookmarking.settings;

public class ClassSetting extends AbstractSetting<Class>
{
    public ClassSetting()
    {
    }

    public ClassSetting(String key)
    {
        super(key);
    }

    public ClassSetting(String group, String key, Class value)
    {
        super(group, key, value);
    }

}
