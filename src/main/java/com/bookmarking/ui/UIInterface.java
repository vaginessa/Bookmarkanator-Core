package com.bookmarking.ui;

import java.util.*;
import com.bookmarking.settings.input.*;

/**
 * This interface is the base interface for all UI Interfaces. These interfaces will be used by any UI elements
 * using the core classes to receive feedback from those classes.
 */
public interface UIInterface
{
    // =====================================================================================
    // This bookmarks status changes (Modify elements of the UI that displays this bookmark)
    // =====================================================================================

    /**
     * Sets the state of this item so the UI can show it. Typically the UI would add an icon to the ui element, or change it's color
     * or whatever.
     * @param state
     */
    void setUIState(UIStateEnum state);

    /**
     * Tell the ui to show a notification message (e.g. in a dialog, or box, or however the UI does it)
     * @param message
     */
    void setStatus(String message);

    /**
     * Intended to allow the items to show a progress indicator with value.
     * @param level  How fare we have gone.
     * @param min  where we started.
     * @param max  Where we will end.
     */
    void setProgress(int level, int min, int max);

    /**
     * Used to show indeterminate progress indicator.
     */
    void setProgressOn();

    /**
     * Used to hide indeterminate progress indicator.
     */
    void setProgressOff();

    // =====================================================================================
    // Messages to the user (in message area not as strong as a notification)
    // =====================================================================================

    /**
     * A message for the user that will be displayed in whatever general message form the UI implements.
     */
    void postMessage(String message);

    // =====================================================================================
    // Higher level notifications and interactions
    // =====================================================================================

    /**
     * Requests a higher level notification in the UI. For example maybe it needs to grab the users attention with a
     * dialog. It would call this method and the UI would do whatever prominent message action it implements.
     * @param message
     */
    void notifyUI(String message);

    /**
     * Requests the users input from a set of values.
     * @param inputOption The object defining the group and parameters of the input option.
     * @return  The selected value populated into the BasicInputOption
     */
    List<InputOption> requestUserInput(List<InputOption> inputOption);
}
