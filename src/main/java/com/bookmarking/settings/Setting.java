package com.bookmarking.settings;

import java.util.*;
import org.apache.logging.log4j.*;

/**
 * This class represents a single setting item.
 */
public class Setting
{
    private static final Logger logger = LogManager.getLogger(Setting.class.getCanonicalName());
    protected String group;
    protected String type;
    protected String key;
    protected String value;

    public Setting()
    {
    }

    public Setting(String key)
    {
        Objects.requireNonNull(key, "Key must not be null.");

        if (key.isEmpty())
        {
            throw new RuntimeException("Key must not be empty");
        }

        this.key = key;
        this.group = "";
    }

    public void setValue(String settingString)
    {
        this.value = settingString;
    }

    public String getValue()
    {
        return this.value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        Objects.requireNonNull(key,"Key cannot be null.");
        this.key = key;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public int hashCode()
    {
        return key.hashCode()+group.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Setting)
        {
            Setting item = (Setting) obj;
            if (this.getGroup().equals(item.getGroup())&& this.getKey().equals(item.getKey()))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Group: "+getGroup()+", Type: "+getType()+", Key: "+getKey()+", Value: "+getValue();
    }
}
