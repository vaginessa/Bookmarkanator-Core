package com.bookmarking.settings;

import java.util.*;

public class SettingsGroup
{
    // <Key, AbstractSetting>
    private Map<String, AbstractSetting> settings;

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
}
