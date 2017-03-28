package com.bookmarkanator.ui.fxui;

import com.bookmarkanator.ui.interfaces.*;

public class QuickPanelUI implements QuickPanelInterface
{
    private UIControllerInterface controller;
    private boolean editMode = false;

    public QuickPanelUI(UIControllerInterface controller)
    {
        this.controller = controller;
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
    public UIControllerInterface getController()
    {
        return this.controller;
    }

    @Override
    public void setController(UIControllerInterface controller)
    {
        this.controller = controller;
    }
}
