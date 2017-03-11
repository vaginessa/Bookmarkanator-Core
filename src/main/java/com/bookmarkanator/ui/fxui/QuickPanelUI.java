package com.bookmarkanator.ui.fxui;

import com.bookmarkanator.ui.interfaces.*;

public class QuickPanelUI implements QuickPanelInterface
{
    private GUIControllerInterface guiController;
    private boolean editMode = false;

    @Override
    public void setGUIController(GUIControllerInterface guiController)
    {
        this.guiController = guiController;
    }

    @Override
    public boolean getEditMode()
    {
        return this.editMode;
    }

    @Override
    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }

    @Override
    public GUIControllerInterface getGUIController()
    {
        return this.guiController;
    }

}
