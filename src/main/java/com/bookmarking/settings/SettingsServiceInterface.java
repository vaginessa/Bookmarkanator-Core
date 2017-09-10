package com.bookmarking.settings;

/**
 * An interface to force exposing of the settings methods.
 */
public interface SettingsServiceInterface
{
    Settings getSettings();
    void setSettings(Settings settings);
}
