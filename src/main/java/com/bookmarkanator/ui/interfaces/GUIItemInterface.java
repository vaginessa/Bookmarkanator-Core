package com.bookmarkanator.ui.interfaces;

public interface GUIItemInterface
{
    void setGUIController(GUIControllerInterface guiController);
    GUIControllerInterface getGUIController();

    void enterEditMode();
    void exitEditMode();
    boolean isEditMode();
}
