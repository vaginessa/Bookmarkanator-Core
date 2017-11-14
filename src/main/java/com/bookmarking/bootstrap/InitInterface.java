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
    void init() throws Exception;
    void init(Settings settings) throws Exception;

    /**
     * Save settings to disk, call save on IO Interface.
     * @throws Exception
     */
    void exit() throws Exception;

    /**
     * Set the UI Interface that this class will use to interact with the front end.
     */
    void setInitUIInterface(InitUIInterface initUIInterface);
    InitUIInterface getInitUIInterface();

    /**
     * Get current version string.
     * @return  A string indicating this classes current version.
     */
    Version getVersion();
}
