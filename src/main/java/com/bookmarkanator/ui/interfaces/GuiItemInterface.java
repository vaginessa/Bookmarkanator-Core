package com.bookmarkanator.ui.interfaces;

public interface GuiItemInterface
{
    boolean getEditMode();
    void setEditMode(boolean editMode);
    void setCurrentSearchTerm(String searchTerm);
    boolean isSearchTermFound();
}
