package com.bookmarking.structure;

import java.util.*;
import com.bookmarking.ui.*;

/**
 * This interface is the base interface for all UI Interfaces. These interfaces will be used by any UI elements
 * using the core classes to receive feedback from those classes.
 */
public interface UIInterface
{
    /**
     * Tell the ui to show a notification message (e.g. in a dialog, or box, or however the UI does it)
     * @param message
     */
    void notifyUI(String message);

    /**
     * The text of this UI element. For bookmark it might be bookmark name, for IO interface it might be the common name.
     * @param text
     */
    void setText(String text);

    /**
     * Sets the state of this item so the UI can show it. Typically the UI would add an icon to the ui element, or change it's color
     * or whatever.
     * @param state
     */
    void setUIState(UIStateEnum state);

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

    /**
     * Requests the users input from a set of values.
     * @param inputOption The object defining the group and parameters of the input option.
     * @return  The selected value populated into the BasicInputOption
     */
    List<AbstractInputOption> requestUserInput(List<AbstractInputOption> inputOption);
}
