package com.bookmarking.settings;

import javax.xml.bind.annotation.*;

@XmlType
public class FloatSetting extends AbstractSetting<Float>
{
    public FloatSetting()
    {
    }

    public FloatSetting(String key)
    {
        super(key);
    }

    public FloatSetting(String group, String key, Float value)
    {
        super(group, key, value);
    }


}
