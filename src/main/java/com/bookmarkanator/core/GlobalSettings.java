package com.bookmarkanator.core;

import java.util.*;

public class GlobalSettings
{
    private Map<String, Object> globalSettings;

    public GlobalSettings()
    {
        globalSettings = new HashMap<>();
    }

    public Object putSettings(String key,List<String> list)
    {
        return this.globalSettings.put(key, list);
    }

    public Object putSetting(String key, String value)
    {
        return this.globalSettings.put(key, value);
    }

    public List<String> getSettings(String key)
    {
        Object object = this.globalSettings.get(key);

        if (object instanceof List)
        {
            return (List<String>)object;
        }
        else if (object instanceof String)
        {//Return single item list
            List<String> l = new ArrayList<>();
            l.add((String)object);
            return l;
        }

        return null;
    }

    public String getSetting(String key)
    {
        Object object = this.globalSettings.get(key);

        if (object instanceof String)
        {
            return (String)object;
        }
        else if (object instanceof List)
        {//Convert single item list into a single item.
            List l = (List)object;
            if (l.size()==1)
            {
                return (String)l.get(0);
            }
        }
        return null;
    }

    public Set<String> keySet()
    {
        return globalSettings.keySet();
    }
}
