package com.bookmarkanator.ui.depreciated;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AvailablePanel extends Pane implements UIControllerInterface
{
    private static AvailablePanel availablePanel;

    public AvailablePanel()
    {
        Label label = new Label("Available Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: #8fbc8f");
    }

    public static AvailablePanel use()
    {
        if (availablePanel==null)
        {
            availablePanel = new AvailablePanel();
        }
        return availablePanel;
    }

    @Override
    public void addAction(UIAction uiAction)
    {

    }

    @Override
    public UIAction getLatestUIAction(UIInterface item)
    {
        return null;
    }

    @Override
    public List<UIAction> getUIActions()
    {
        return null;
    }

    @Override
    public void undo()
    {

    }

    @Override
    public void redo()
    {

    }

    @Override
    public void enterSearchTerm(String searchTerm)
    {

    }

    @Override
    public void setSearchOptions(Map<String, Boolean> searchOptions)
    {

    }

    @Override
    public Map<String, Boolean> getSearchOptions()
    {
        return null;
    }

    @Override
    public void setNumSearchResults(int numberSearchResults)
    {

    }

    @Override
    public void setSearchInterface(SearchInterface searchInterface)
    {

    }

    @Override
    public SearchInterface getSearchInterface()
    {
        return null;
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
