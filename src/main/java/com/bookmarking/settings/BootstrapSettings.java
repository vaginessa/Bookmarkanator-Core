package com.bookmarking.settings;

import java.util.*;

public class BootstrapSettings
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
}
