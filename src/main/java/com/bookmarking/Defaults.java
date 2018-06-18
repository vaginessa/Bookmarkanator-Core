package com.bookmarking;

import java.io.*;

public interface Defaults
{
    // ============================================================
    // Settings... Settings
    // ============================================================

    // Groups:
    String NO_GROUP = "no-group";// The group for settings without a group
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
    String DIRECTORIES_GROUP = "directory-settings";

    // Keys:
    String PRIMARY_FILE_LOCATION_KEY = "primary-file-location";
    String SECONDARY_FILE_LOCATION_KEY = "secondary-file-location";
    String SELECTED_FILE_LOCATION_KEY = "chosen-file-location";
    String SETTINGS_FILE_NAME_KEY = "settings-file-name";

    // Values:
    String CORE_SETTINGS_NAME = "Core Settings";
    File PRIMARY_DIRECTORY = new File(".");
    File SECONDARY_DIRECTORY = new File(System.getProperty("user.home"));
    String SETTINGS_FILE_NAME = "settings.xml";
    String SETTINGS_FILE_CONTEXT = "mainSettings";
    String USE_FILE_SYSTEM = "useFileSystem";// Don't read/write settings file if false

    // ============================================================
    // FileIO Settings
    // ============================================================

    // Groups:
    String FILE_IO_SETTINGS_GROUP = "FileIO";

    // Keys:
    String IMPLEMENTING_CLASSES_GROUP = "implementing-classes-group";
    String OVERRIDDEN_CLASSES_GROUP = "overridden-classes-group";

    // Values:
    String BOOKMARKS_FILE_NAME = "Bookmarks.xml";
    String BOOKMARKS_FILE_SYNC_CONTEXT = "bookmark-file-context";


}
