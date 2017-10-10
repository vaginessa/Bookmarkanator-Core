package com.bookmarking.settings;

import javax.xml.bind.annotation.*;

@XmlType
public class DoubleSetting extends AbstractSetting<Double>
{
    public DoubleSetting()
    {
    }

    public DoubleSetting(String key)
    {
        super(key);
    }

    public DoubleSetting(String group, String key, Double value)
    {
        super(group, key, value);
    }

}
