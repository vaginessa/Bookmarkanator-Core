package com.bookmarkanator.ui;

import com.bookmarkanator.io.*;

public class UIController
{
    private static UIController uiController;
    private ContextInterface context;
    private SearchPanel menuPanel;
    private TypesPanel typesPanel;
    private SelectedPanel selectedPanel;
//    private AvailablePanel availablePanel;
    private BookmarksPanel bookmarksPanel;
    private DocView docView;

    public UIController()
    {

    }

    public static UIController use()
    {
        if (uiController==null)
        {
            uiController = new UIController();
        }
        return uiController;
    }

    public ContextInterface getContext()
    {
        return context;
    }

    public void setContext(ContextInterface context)
    {
        this.context = context;
    }

    public SearchPanel getMenuPanel()
    {
        return menuPanel;
    }

    public void setMenuPanel(SearchPanel menuPanel)
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

//    public AvailablePanel getAvailablePanel()
//    {
//        return availablePanel;
//    }
//
//    public void setAvailablePanel(AvailablePanel availablePanel)
//    {
//        this.availablePanel = availablePanel;
//    }

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
