package com.bookmarking.action;

import com.bookmarking.ui.*;

/**
 * The ActionInterface is used to have a function, much like a bookmark, but one that is not tied to tagging, and doesn't
 * have data to load/prepExit like bookmarks have.
 */
public abstract class AbstractAction
{
    // ============================================================
    // Fields
    // ============================================================

    private ActionUIInterface uiInterface;
    private boolean isHidden;
    private boolean enabled;

    // ============================================================
    // Constructors
    // ============================================================

    public AbstractAction()
    {
        isHidden = false;
        enabled = true;
    }

    // ============================================================
    // Methods
    // ============================================================

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

    /**
     * Called as the system is starting up so that if individual actions want to do some kind of configuration they can.
     */
    public abstract void systemInit();

    /**
     * Called prior to shutting the system down, so that individual actions can perform any actions they deem necessary
     * prior to being shut down.
     */
    public abstract void systemShuttingDown();

    public UIInterface getUiInterface()
    {
        return uiInterface;
    }

    public void setUiInterface(ActionUIInterface uiInterface)
    {
        this.uiInterface = uiInterface;
    }

    public boolean isHidden()
    {
        return isHidden;
    }

    public void setHidden(boolean hidden)
    {
        isHidden = hidden;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
