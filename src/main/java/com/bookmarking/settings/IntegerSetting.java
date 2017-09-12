package com.bookmarking.settings;

public class IntegerSetting extends AbstractSetting<Integer>
{
    public IntegerSetting()
    {
    }

    public IntegerSetting(String key)
    {
        super(key);
    }

    public IntegerSetting(String group, String key, Integer value)
    {
        super(group, key, value);
    }
}
