package com.bookmarkanator.io;

import com.bookmarkanator.core.*;

/**
 * This is the interface that other classes will use to load and save bookmarks. It could point to files on the file system as FileIo does,
 * or it could point to a database, web service, ftp server, etc...
 */
public interface BKIOInterface
{

    enum Actions
    {
        SAVING,
        LOADING,
        DELETING,
        COMPLETE;
    }

    /**
     * Set up the data source with a specific configuration string.
     *
     * @param config A configuration string.
     * @throws Exception
     */
    void init(String config)
        throws Exception;

    /**
     * Perform a save of all bookmarks.
     */
    void save()
        throws Exception;

    /**
     * Perform a save of all bookmarks.
     */
    void save(String config)
        throws Exception;

    /**
     * Does any closing of connections, streams, open files etc...
     */
    void close()
        throws Exception;

    /**
     * Returns a context specific to this data source. The default "Context" Object would be returned from the FileIO implementation of this interface
     * for example. A reference to the context object returned is kept in the BKIOInterface, and when the various methods are called (save, close etc...)
     * the context is operated on.
     *
     * @return A IO specific type of context.
     */
    AbstractContext getContext()
        throws Exception;

    /**
     * Get settings that are saved/loaded in whatever way the particular interface does it. These settings are separate
     * from the global settings that Bootstrap uses, and which are file based.
     *
     * @return
     */
    Settings getSettings();

    /**
     * Set the settings that this particular interface will use (and save/load). These settings will be treated separately
     * than the global settings object that Bootstrap uses.
     *
     * @param settings The settings for this IOInterface to use.
     */
    void setSettings(Settings settings);
}
