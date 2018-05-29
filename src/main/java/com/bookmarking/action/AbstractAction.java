package com.bookmarking.action;

import com.bookmarking.ui.*;

/**
 * The ActionInterface is used to have a function, much like a bookmark, but one that is not tied to tagging, and doesn't
 * have data to load/save during startup/shutdown like bookmarks have. Any information that needs to be saved should be limited, and written to
 * settings by the action it self when necessary.
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
     * Run specified action(s) with action string.
     *
     * @param actionStrings Zero or more specific configuration instructions or actions for running the action.
     * @return The result of the action. Returns an array of strings so that individual statuses can be given to the individual strings sent in.
     * @throws Exception
     */
    public abstract String[] runAction(String[] ... actionStrings)
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

    /**
     * Hidden actions are actions intended to be used internally by the program or other actions.
     */
    public boolean isHidden()
    {
        return isHidden;
    }

    public void setHidden(boolean hidden)
    {
        isHidden = hidden;
    }

    /**
     * Enabling/Disabling actions bascially specifies which ones will loaded and not loaded. It is important to have this field for when the user
     * needs to enable a disabled action. It must be possible to list it so they can select it.
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
