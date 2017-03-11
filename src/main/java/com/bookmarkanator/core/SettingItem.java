package com.bookmarkanator.core;

import java.util.*;

public class SettingItem
{
    private String key;
    private String type;
    private String value;

    public SettingItem(String key)
    {
        Objects.requireNonNull(key, "Key must not be null.");

        if (key.isEmpty())
        {
            throw new RuntimeException("Key must not be empty");
        }

        this.key = key;
    }

    public void setSetting(String settingString)
        throws Exception
    {
        this.value = settingString;
    }

    public String getSetting()
    {
        return this.value;
    }

    public String getKey()
    {
        return key;
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
            return key.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SettingItem)
        {
            SettingItem item = (SettingItem)obj;
            return this.getKey().equals(item.getKey());
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Key: "+this.getKey()+", type: "+this.getType();
    }
}
