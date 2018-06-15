package com.bookmarking.settings;

public interface SettingsIOInterface
{
    /**
     * Loads/reads settings based on specifications in the supplied settings object. Merges the loaded settings
     * with the current settings (should create a new settings object in the process) and returns it.
     * @param settings  Settings used to determine what to do to load the new settings.
     * @return A settings object loaded by this interface, that has been merged with the settings sent in.
     */
    Settings init(Settings settings)
        throws Exception;

    Settings getSettings();

    void setSettings(Settings settings);

    void save() throws Exception;

    /**
     * Prepare to exit - save settings and other such related items.
      */
    void prepExit()
        throws Exception;

    /**
     * Perform any final cleanup tasks that need to be done after the prepExit step.
     */
    void exit();
}
