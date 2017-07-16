package com.bookmarkanator.ui.interfaces;

public interface UIItemInterface
{
    boolean getEditMode();

    void setEditMode(boolean editMode);

    UIControllerInterface getController();

    void setController(UIControllerInterface controller);
}
