package com.bookmarkanator.ui.interfaces;

public interface GuiItemInterface
{
    public GUIControllerInterface getGUIController();
    public void setGUIController(GUIControllerInterface guiController);
    public boolean getEditMode();
    public void setEditMode(boolean editMode);
}
