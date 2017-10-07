package com.bookmarking.structure;

import java.util.*;
import org.apache.logging.log4j.*;

public abstract class AbstractSetting<T> implements Comparable<AbstractSetting>
{
    private static final Logger logger = LogManager.getLogger(AbstractSetting.class.getCanonicalName());
    protected String group;
    protected String key;
    protected T value;

    public AbstractSetting()
    {
    }

    public AbstractSetting(String key)
    {
        this.key = key;
    }

    public AbstractSetting(String group, String key, T value)
    {
        this.group = group;
        this.key = key;
        this.value = value;
    }

    public void setValue(T value)
        throws ClassNotFoundException, Exception
    {
        this.value = value;
    }

    public T getValue()
    {
        return this.value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
        throws Exception
    {
        Objects.requireNonNull(key, "Key cannot be null.");
        this.key = key;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
        throws Exception
    {
        this.group = group;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof AbstractSetting)
        {
            AbstractSetting item = (AbstractSetting) obj;
            if (this.getGroup().equals(item.getGroup()) && this.getKey().equals(item.getKey()))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Group: " + getGroup() + ",  Key: " + getKey() + ", Value: " + getValue();
    }

    @Override
    public int compareTo(AbstractSetting o)
    {
        return this.getKey().compareTo(o.getKey());
    }
}