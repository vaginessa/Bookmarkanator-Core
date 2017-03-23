package com.bookmarkanator.ui.fxui;

import com.bookmarkanator.ui.interfaces.*;

public class QuickPanelUI implements QuickPanelInterface
{
    private GUIControllerInterface guiController;
    private boolean editMode = false;

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
    public void setCurrentSearchTerm(String searchTerm)
    {

    }

    @Override
    public boolean isSearchTermFound()
    {
        return false;
    }
}
