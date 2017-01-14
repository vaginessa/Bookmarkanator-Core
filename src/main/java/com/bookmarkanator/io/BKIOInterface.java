package com.bookmarkanator.io;

import com.bookmarkanator.core.*;

public interface BKIOInterface {

    /**
     * Set up the data source with a specific configuration string.
     * @param config  A configuration string.
     * @throws Exception
     */
    void init(String config)throws Exception;

    /**
     * Set up the data source with a specific configuration string, and the global config object.
     * @param config  A configuration setting.
     * @param globalSettings  The settings that were loaded from the settings file.
     * @throws Exception
     */
    void init(String config, GlobalSettings globalSettings, ClassLoader classLoader)throws Exception;

    /**
     * Perform a save of all bookmarks.
     */
    void save()throws Exception;

    /**
     * Perform a save of all bookmarks.
     */
    void save(String config)throws Exception;

    /**
     * Does any closing of connections, or writing to files etc...
     */
    void close()throws Exception;

    /**
     * Returns a context specific to this data source. The default "Context" Object would be returned from the FileIO implementation of this interface
     * for example. A reference to the context object returned is kept in the BKIOInterface, and when the various methods are called (save, close etc...)
     * the context is operated on.
     * @return  A IO specific type of context.
     */
    ContextInterface getContext()throws Exception;
}
