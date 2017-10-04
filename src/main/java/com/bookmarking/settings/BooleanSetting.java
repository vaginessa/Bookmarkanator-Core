package com.bookmarking.settings;

import com.bookmarking.structure.*;

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
