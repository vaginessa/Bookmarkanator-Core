package com.bookmarking.settings.types;

import com.bookmarking.settings.types.*;

public class BooleanSetting extends AbstractSetting<Boolean>
{
    public BooleanSetting()
    {
    }

    public BooleanSetting(String key)
    {
        super(key);
    }

    public BooleanSetting(String group, String key, Boolean value)
    {
        super(group, key, value);
    }

}