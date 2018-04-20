package com.bookmarking.settings;

import java.util.*;
import com.bookmarking.settings.types.*;

public class SettingsGroup implements Comparable
{
    // <Key, AbstractSetting>
    private Map<String, AbstractSetting> settingsMap;
    private String groupName;
    // A reference to the containing settings object.
    private Settings settings;

    public SettingsGroup()
    {
        settingsMap = new HashMap<>();
    }

    public Map<String, AbstractSetting> getSettingsMap()
    {
        return settingsMap;
    }

    public void setSettingsMap(Map<String, AbstractSetting> settings)
    {
        this.settingsMap = settings;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public Settings getSettingsContainer()
    {
        return settings;
    }

    public void setSettingsContainer(Settings settingsContainer)
    {
        this.settings = settingsContainer;
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }
}
