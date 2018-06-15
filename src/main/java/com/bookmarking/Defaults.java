package com.bookmarking;

import java.io.*;

public interface Defaults
{
    // ============================================================
    // Default General Settings Keys
    // ============================================================

    /**
     * The group that is used to specify implementing classes, or overriding classes.
     * <p>
     * For example you can only have a single IOInterface implementation loaded at a time. So you would add an entry here specifying the key as the
     * interface class, and the value as the implementation you want to load for that interface.
     * <p>
     * If you want to replace a class with another one, say class 'A' with class 'B', you would add an entry with key 'A' and the value would be
     * 'B'. When the module loader is asked for class 'A' it would check this value, and return class 'B' instead.
     */
    String DEFAULT_CLASSES_GROUP = "default-classes-group";

    // ============================================================
    // Settings... Settings
    // ============================================================

    String CORE_SETTINGS = "Core Settings";

    // The group for settings without a group
    String NO_GROUP = "no-group";

    String DEFAULT_SECONDARY_SETTINGS_FILE_LOCATION_KEY = "default-secondary-settings-file-location";

    // Fallback values in case settings are not present.
    String FALLBACK_SETTINGS_FILE_NAME = "settings.xml";
    File FALLBACK_SETTINGS_DIRECTORY = new File(".");

    String SETTINGS_FILE_CONTEXT = "mainSettings";

    // ============================================================
    // Default Classes Settings
    // ============================================================


    // ============================================================
    // FileIO Settings
    // ============================================================

    String FILE_IO_SETTINGS = "FileIO";
    String IMPLEMENTING_CLASSES_GROUP = "implementing-classes-group";
    String OVERRIDDEN_CLASSES_GROUP= "overridden-classes-group";
    String DEFAULT_SETTINGS_FILE_NAME_KEY = "default-file-settings-name";
    String DEFAULT_SETTINGS_FILE_LOCATION_KEY = "default-settings-file-location";
    String DEFAULT_SECONDARY_SETTINGS_FILE_NAME_KEY = "default-secondary-file-settings-name";
    String FILE_IO_KEY = "FILE_IO";
    String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";
    String FILE_IO_SETTINGS_KEY = "FILE_IO_SETTINGS";
    String DEFAULT_SETTINGS_FILE_NAME = "file-io-settings.xml";
    String FILE_SEARCH_SETTINGS_KEY = "FILE_SEARCH_SETTINGS";
    String DEFAULT_SEARCH_SETTINGS_FILE_NAME = "file-search-settings.xml";
    // Settings keys
    String GLOBAL_SETTINGS_GROUP = "GLOBAL_SETTINGS";
    // Module loader keys
    String MODULE_LOCATIONS_GROUP = "module-locations";
    String INIT_SETTING_KEY = "init-setting";

    /**
     * Structure:
     *
     * default classes - all classes this component uses by default. IO interface, SettingsIOInterface, BookmarkClasses etc... (these are classes that specify an implementation for an interface or abstract class)
     * default locations - Locations to search for settings files if none are specified.
     * overridden classes - entries for classes that can be overridden at run time using this setting. This can be used to point to a different implementation of a class at runtime.
     * file settings - Settings related to FileIO. FileIO is the default storage mechanism, but a different mechanism can be specified at runtime in the default classes. In this case these settings will be ignored.
     */













}
