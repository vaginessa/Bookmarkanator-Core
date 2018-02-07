package com.bookmarking.bootstrap;

import com.bookmarking.settings.*;
import com.bookmarking.util.*;

public interface InitInterface
{
    String OVERRIDDEN_CLASSES_GROUP = "overridden-classes";

    /**
     * - Load settings file from disk.
     * - Load modules if any.
     * - Init IOInterface
     */
    void init(SettingsIOInterface settingsIOInterface, InitUIInterface initUIInterface) throws Exception;

    /**
     * Save settings to disk, call save on IO Interface.
     * @throws Exception
     */
    void exit() throws Exception;

    /**
     * Get current version string.
     * @return  A string indicating this classes current version.
     */
    Version getVersion();
}
