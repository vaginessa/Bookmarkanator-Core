package com.bookmarking.settings.types;

import javax.xml.bind.annotation.*;
import com.bookmarking.settings.types.*;

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
