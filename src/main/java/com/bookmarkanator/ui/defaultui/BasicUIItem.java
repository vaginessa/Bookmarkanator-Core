package com.bookmarkanator.ui.defaultui;

import com.bookmarkanator.ui.defaultui.interfaces.*;

public class BasicUIItem implements GUIItemInterface
{
    private GUIControllerInterface guiController;

    @Override
    public void setGUIController(GUIControllerInterface guiController)
    {
        this.guiController = guiController;
    }

    @Override
    public GUIControllerInterface getGUIController()
    {
        return this.guiController;
    }

    @Override
    public void enterEditMode()
    {

    }

    @Override
    public void exitEditMode()
    {

    }

    @Override
    public boolean isEditMode()
    {
        return false;
    }
}
