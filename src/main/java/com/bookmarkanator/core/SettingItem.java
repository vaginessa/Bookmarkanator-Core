package com.bookmarkanator.core;

import java.util.*;
import org.apache.logging.log4j.*;

/**
 * This class represents a single setting item.
 */
public class SettingItem
{
    private static final Logger logger = LogManager.getLogger(SettingItem.class.getCanonicalName());
    protected String key;
    protected String type;
    protected String value;
    protected boolean allowDuplicates;//Specifies if more than one of these settings is allowed.

    public SettingItem(String key)
    {
        Objects.requireNonNull(key, "Key must not be null.");

        if (key.isEmpty())
        {
            throw new RuntimeException("Key must not be empty");
        }

        this.key = key;
    }

    public void setValue(String settingString)
        throws Exception
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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public boolean isAllowDuplicates()
    {
        return allowDuplicates;
    }

    public void setAllowDuplicates(boolean allowDuplicates)
    {
        this.allowDuplicates = allowDuplicates;
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
