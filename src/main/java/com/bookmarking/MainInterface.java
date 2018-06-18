package com.bookmarking;

import com.bookmarking.bootstrap.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.stats.*;
import com.bookmarking.ui.*;

/**
 * Interface to allow the UI to have a single point of contact with the core library.
 */
public interface MainInterface
{
    // ----------------------------------------------
    // Init
    // ----------------------------------------------

    void exit() throws Exception;

    Bootstrap getStartClass();

    void setStartClass(Bootstrap bootstrap);

    // ----------------------------------------------
    // Interfaces
    // ----------------------------------------------

    /**
     * The module loader inspects the classpath, and locates various classes or interfaces that have been added to it's watch list. It also enables
     * the loading of jars from specified directories.
     */
    ModuleLoader getModuleLoader();

    /**
     * The file service enables the front end to read/write files in a semi automatic way.
     */
    FileService getFileService();

    /**
     * The IOInterface is the main class used to perform CRUD actions with AbstractBookmarks. It also enables searching, and bulk tag CRUD operations.
     */
    IOInterface getIOInterface();

    /**
     * Stats interface to obtain stats about things the main interface cares about.
     * @return StatsInterface
     */
    StatsInterface getStatsInterface();

    // ----------------------------------------------
    // Settings
    // ----------------------------------------------

    /**
     * The main settings object is a container for the settings loaded by the InitInterface, and the settings from the IOInterface. The InitInterface
     * settings are always obtained from the device storage (hard disk), however the IOInterface settings would be obtained after InitInterface settings
     * and in a manner specific to the type of IO. For example if it was a database it would be from tables.
     */
    MainSettings getSettings();

    /**
     * Set main settings as well as IOInterface settings.
     * @param mainSettings  The new settings.
     */
    void setSettings(MainSettings mainSettings)
        throws Exception;

    /**
     * In the case of InitInterface it saves the settings back to disk. And the IOInterface it saves it in whatever specific way it uses.
     */
    MainSettings saveSettings()
        throws Exception;

    // ----------------------------------------------
    // UI
    // ----------------------------------------------

    /**
     * Set the interface that will be used to write status messages to the UI. All other sub UI Interfaces will need to be obtained from this one.
     */
    void setUIInterface(UIInterface uiInterface)
        throws Exception;


    /**
     * @return  Return the UI interface this class is using to communicate with the front end.
     */
    UIInterface getUIInterface();

}
