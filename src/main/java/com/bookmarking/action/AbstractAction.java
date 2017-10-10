package com.bookmarking.action;

import com.bookmarking.ui.*;

/**
 * The ActionInterface is used to have a function, much like a bookmark, but one that is not tied to tagging, and doesn't
 * have data to load/save like bookmarks have.
 */
public abstract class AbstractAction
{
    private ActionUIInterface uiInterface;

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
