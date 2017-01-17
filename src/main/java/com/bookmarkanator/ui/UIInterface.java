package com.bookmarkanator.ui;

public interface UIInterface
{
    void enterEditMode();
    void exitEditMode();

    /**
     * The class must implement the behaviour to undo any of the actions it can generate.
     * @param action  The action to undo
     */
    void undo(String action);

    /**
     * The class must implement the behaviour to redo any of the actions it can generate.
     * @param action  The action to undo
     */
    void redo(String action);
}
