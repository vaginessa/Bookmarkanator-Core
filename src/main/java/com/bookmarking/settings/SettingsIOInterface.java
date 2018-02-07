package com.bookmarking.settings;

public interface SettingsIOInterface
{
    /**
     * Loads/reads settings based on specifications in the supplied settings object. Merges the loaded settings
     * with the current settings (creating a new settings object in the process) and returns it.
     * @param settings  Settings used to determine what to do to load the new settings.
     * @return A settings object loaded by this interface, that has been merged with the settings sent in.
     */
    Settings init(Settings settings);

    void save();
    void exit();
}
