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
    public GUIControllerInterface getGUIController()
    {
        return this.guiController;
    }

    @Override
    public void enterEditMode()
    {
        this.editMode = true;
    }

    @Override
    public void exitEditMode()
    {
        this.editMode = false;
    }

    @Override
    public boolean isEditMode()
    {
        return this.editMode;
    }
}
