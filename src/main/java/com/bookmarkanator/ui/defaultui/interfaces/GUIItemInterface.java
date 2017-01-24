package com.bookmarkanator.ui.defaultui.interfaces;

public interface GUIItemInterface
{
    void setGUIController(GUIControllerInterface guiController);
    GUIControllerInterface getGUIController();

    void enterEditMode();
    void exitEditMode();
    boolean isEditMode();
}
