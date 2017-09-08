package com.bookmarking;

import java.util.*;
import org.apache.logging.log4j.*;

/**
 * This class represents a single setting item.
 */
public class SettingItem
{
    private static final Logger logger = LogManager.getLogger(SettingItem.class.getCanonicalName());
    protected String type;
    protected String key;
    protected String value;

    public SettingItem()
    {
    }

    public SettingItem(String key)
    {
        Objects.requireNonNull(key, "Key must not be null.");

        if (key.isEmpty())
        {
            throw new RuntimeException("Key must not be empty");
        }

        this.key = key;
        this.type = "";
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
            SettingItem item = (SettingItem) obj;
            return this.getKey().equals(item.getKey());
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Key: " + this.getKey() + ", type: " + this.getType();
    }
}
