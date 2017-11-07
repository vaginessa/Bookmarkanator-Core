package com.bookmarking;

import com.bookmarking.bootstrap.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.ui.*;

/**
 * Interface to allow the UI to interact with the core library through a single class.
 * <p>
 * The default interface is "LocalInstance", which was created for local java programs. However many other interfaces
 * could be made. For example, one could write a JavaFX program that uses a rest web interface, which would in turn
 * uses LocalInstance to communicate to the core libraries.
 */
public interface MainInterface
{
    String SETTINGS_FILE_CONTEXT = "mainSettings";

    // ----------------------------------------------
    // Init
    // ----------------------------------------------

    void init()
        throws Exception;

    void init(Settings settings)
        throws Exception;

    void init(InitUIInterface bookmarkUIInterface)
        throws Exception;

    void init(Settings settings, InitUIInterface bookmarkUIInterface)
        throws Exception;

    void exit() throws Exception;

    // ----------------------------------------------
    // Interfaces
    // ----------------------------------------------

    InitInterface getInitInterface();

    ModuleLoader getModuleLoader();

    FileService getFileService();

    IOInterface getIOInterface();

    // ----------------------------------------------
    // Settings
    // ----------------------------------------------

    MainSettings getSettings();

    void setSettings(MainSettings mainSettings)
        throws Exception;

    MainSettings saveSettings()
        throws Exception;

    MainSettings saveSettings(MainSettings mainSettings)
        throws Exception;

    void setUIInterface(UIInterface uiInterface)
        throws Exception;

    // ----------------------------------------------
    // UI
    // ----------------------------------------------

    UIInterface getUIInterface();

    // ----------------------------------------------
    // Undo/Redo
    // ----------------------------------------------

    void undo() throws Exception;
    void redo() throws Exception;
    void clearUndoRedoStack() throws Exception;
}
