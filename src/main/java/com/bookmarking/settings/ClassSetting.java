package com.bookmarking.settings;

/**
 * This setting links one class to another so that things like setting default classes, and overriding classes can be set.
 * It only stores valid classes as keys and values.
 */
public class ClassSetting extends AbstractSetting<Class>
{
    public ClassSetting()
    {
    }

    public ClassSetting(String key)
        throws ClassNotFoundException
    {
        super(key);
        Class.forName(key);
    }

    public ClassSetting(String group, String key, Class value)
        throws ClassNotFoundException
    {
        super(group, key, value);
        Class.forName(key);
    }

    public Class keyAsClass()
    {
        try
        {
            return Class.forName(getKey());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public void setKey(String key)
        throws Exception
    {
        super.setKey(key);
        Class.forName(key);
    }

    @Override
    public boolean isKeyValid(String key)
    {
        if (key==null)
        {
            return false;
        }

        try
        {
            Class.forName(key);
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;
    }

    @Override
    public boolean isValueValid(Class value)
    {
        if (value==null)
        {
            return false;
        }

        return true;
    }
}
