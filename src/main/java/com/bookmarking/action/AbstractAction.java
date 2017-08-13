package com.bookmarking.action;

import com.bookmarking.*;

/**
 * The ActionInterface is used to have a function, much like a bookmark, but one that is not tied to tagging, and doesn't
 * have a multiplicity of data to load/save.
 *
 * For example:
 *
 * A one might have many of one type of bookmark, each one with it's own data, and tagging. When that bookmark is ran it will
 * operate using the specific data for that bookmark.
 *
 * With an action the data it has must be saved in the settings for the main program (and thus should be small), and cannot be tagged.
 */
public abstract class AbstractAction
{
    private Bootstrap bootstrap;

    // Needed to give the action access to the whole system (bootstrap settings, IOInterface, etc...)
    void setBootstrap(Bootstrap bootstrap)
    {
        this.bootstrap = bootstrap;
    }

    /**
     * Run specified action.
     *
     * @return The result of the action
     * @throws Exception
     */
    String runAction()
        throws Exception
    {
        return runAction("");
    }

    /**
     * Run run specified action with action string.
     *
     * @param actionString  Specific configuration instructions for running the action.
     * @return The result of the action
     * @throws Exception
     */
     abstract String runAction(String actionString)
        throws Exception;
}
