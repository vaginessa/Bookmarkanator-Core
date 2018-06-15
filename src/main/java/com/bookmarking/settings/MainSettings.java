package com.bookmarking.settings;

import java.util.*;
import com.bookmarking.*;

public class MainSettings
{
    private Settings mainSettings;
    private Settings ioSettings;

    public Settings getMainSettings()
    {
        if (mainSettings==null)
        {
            mainSettings = new Settings();
        }

        return mainSettings;
    }

    public void setMainSettings(Settings mainSettings)
    {
        Objects.requireNonNull(mainSettings);

        this.mainSettings = mainSettings;
    }

    public Settings getIoSettings()
    {
        if (ioSettings==null)
        {
            ioSettings = new Settings();
        }
        return ioSettings;
    }

    public void setIoSettings(Settings ioSettings)
    {
        Objects.requireNonNull(ioSettings);

        this.ioSettings = ioSettings;
    }

    public List<SettingsGroup> getMainGroups()
    {
        List<SettingsGroup> settingsGroups = new ArrayList<>(mainSettings.getGroups().values());
        Collections.sort(settingsGroups);
        return settingsGroups;
    }

    public List<SettingsGroup> getIOGroups()
    {
        List<SettingsGroup> settingsGroups = new ArrayList<>(ioSettings.getGroups().values());
        Collections.sort(settingsGroups);
        return settingsGroups;
    }

    public String getIOInterfaceName()
        throws Exception
    {
        String ioInterfaceName = LocalInstance.use().getIOInterface().getClass().getName();
        ioInterfaceName = ioInterfaceName.substring(ioInterfaceName.lastIndexOf('.'), ioInterfaceName.length());
        ioInterfaceName = ioInterfaceName.replaceAll("\\.","");
        return ioInterfaceName;
    }

    public String getMainSettingsName()
    {
        return Defaults.CORE_SETTINGS;
    }
}
