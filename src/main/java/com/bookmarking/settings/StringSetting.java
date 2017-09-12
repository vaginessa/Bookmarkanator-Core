package com.bookmarking.settings;

import javax.xml.bind.annotation.*;

@XmlType
public class StringSetting extends AbstractSetting<String>
{
    public StringSetting()
    {
    }

    public StringSetting(String key)
    {
        super(key);
    }

    public StringSetting(String group, String key, String value)
    {
        super(group, key, value);
    }
}
