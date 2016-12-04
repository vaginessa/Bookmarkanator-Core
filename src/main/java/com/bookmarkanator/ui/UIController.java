package com.bookmarkanator.ui;

import com.bookmarkanator.core.*;

public class UIController
{
    private ContextInterface context;
    private MenuPanel menuPanel;
    private TypesPanel typesPanel;
    private SelectedPanel selectedPanel;
    private AvailablePanel availablePanel;
    private BookmarksPanel bookmarksPanel;
    private DocView docView;

    public UIController()
    {

    }

    public ContextInterface getContext()
    {
        return context;
    }

    public void setContext(ContextInterface context)
    {
        this.context = context;
    }

    public MenuPanel getMenuPanel()
    {
        return menuPanel;
    }

    public void setMenuPanel(MenuPanel menuPanel)
    {
        this.menuPanel = menuPanel;
    }

    public TypesPanel getTypesPanel()
    {
        return typesPanel;
    }

    public void setTypesPanel(TypesPanel typesPanel)
    {
        this.typesPanel = typesPanel;
    }

    public SelectedPanel getSelectedPanel()
    {
        return selectedPanel;
    }

    public void setSelectedPanel(SelectedPanel selectedPanel)
    {
        this.selectedPanel = selectedPanel;
    }

    public AvailablePanel getAvailablePanel()
    {
        return availablePanel;
    }

    public void setAvailablePanel(AvailablePanel availablePanel)
    {
        this.availablePanel = availablePanel;
    }

    public BookmarksPanel getBookmarksPanel()
    {
        return bookmarksPanel;
    }

    public void setBookmarksPanel(BookmarksPanel bookmarksPanel)
    {
        this.bookmarksPanel = bookmarksPanel;
    }

    public DocView getDocView()
    {
        return docView;
    }

    public void setDocView(DocView docView)
    {
        this.docView = docView;
    }
}
