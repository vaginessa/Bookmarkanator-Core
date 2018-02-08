package com.bookmarking.settings;

public class SettingsIO implements  SettingsIOInterface
{
    private static final String DEFAULT_SETTINGS_FILE_NAME = "settings.xml";
    private static final String DEFAULT_SETTINGS_DIRECTORY = "Bookmarkanator";

    @Override
    public Settings init(Settings settings)
    {
        return settings;
    }

    @Override
    public void prepExit()
    {

    }

    @Override
    public void exit()
    {

    }
}
