package com.bookmarkanator.io;

import com.bookmarkanator.core.*;

public interface BKIOInterface {
    /**
     * Set up the data source
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * Set up the data source with a specific context
     * @param mode  The context or mode of the data source
     * @throws Exception
     */
    void init(String mode)throws Exception;

    /**
     * Perform a save of all bookmarks.
     */
    void save();

    /**
     * Does any closing of connections, or writing to files etc...
     */
    void close();

    /**
     * Returns a context specific to this data source. The default "Context" Object would be returned from the FileIO implementation of this interface
     * for example. A reference to the context object returned is kept in the BKIOInterface, and when the various methods are called (save, close etc...)
     * the context is operated on.
     * @return  A IO specific type of context.
     */
    ContextInterface getContext();
}
