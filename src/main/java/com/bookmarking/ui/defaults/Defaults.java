package com.bookmarking.ui.defaults;

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
    // Default Settings Keys
    // ============================================================

    String CORE_SETTINGS = "Core Settings";
    // The group for settings without a group
    String NO_GROUP = "no-group";
    String DEFAULT_SECONDARY_SETTINGS_FILE_LOCATION_KEY = "default-secondary-settings-file-location";
    // Settings keys
    String FILE_SETTINGS_GROUP_KEY = "file-settings-group-key";
    String DEFAULT_SETTINGS_FILE_NAME_KEY = "default-file-settings-name";
    String DEFAULT_SETTINGS_FILE_LOCATION_KEY = "default-settings-file-location";
    String DEFAULT_SECONDARY_SETTINGS_FILE_NAME_KEY = "default-secondary-file-settings-name";
    // Fallback values in case settings are not present.
    File FALLBACK_SETTINGS_DIRECTORY = new File(".");
    String FALLBACK_SETTGINS_FILE_NAME = "settings.xml";
    String FILE_SYNC_CONTEXT = "Settings File";

    // ============================================================
    // Default File Keys
    // ============================================================

    String IO_INTERFACE_GROUP = "io-interface-group";

    String IMPLEMENTING_CLASSES_GROUP = "implementing-classes-group";
}
