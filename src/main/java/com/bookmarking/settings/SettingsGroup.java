package com.bookmarking.settings;

import java.util.*;

public class SettingsGroup implements Comparable
{
    // <Key, AbstractSetting>
    private Map<String, AbstractSetting> settings;
    private String groupName;

    public SettingsGroup()
    {
        settings = new HashMap<>();
    }

    public Map<String, AbstractSetting> getSettings()
    {
        return settings;
    }

    public void setSettings(Map<String, AbstractSetting> settings)
    {
        this.settings = settings;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }
}
