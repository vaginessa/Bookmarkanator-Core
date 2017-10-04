package com.bookmarking.structure;

import com.bookmarking.*;

/**
 * The ActionInterface is used to have a function, much like a bookmark, but one that is not tied to tagging, and doesn't
 * have a multiplicity of data to load/save.
 * <p>
 * For example:
 * <p>
 * A one might have many of one group of bookmark, each one with it's own data, and tagging. When that bookmark is ran it will
 * operate using the specific data for that bookmark.
 * <p>
 * With an action the data it has must be saved in the settings for the main program (and thus should be small), and cannot be tagged.
 */
public abstract class AbstractAction
{
    private Bootstrap bootstrap;
    private ActionUIInterface uiInterface;

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
     * @param actionString Specific configuration instructions for running the action.
     * @return The result of the action
     * @throws Exception
     */
    public abstract String runAction(String actionString)
        throws Exception;

    public UIInterface getUiInterface()
    {
        return uiInterface;
    }

    public void setUiInterface(ActionUIInterface uiInterface)
    {
        this.uiInterface = uiInterface;
    }
}
