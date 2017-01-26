package com.bookmarkanator.ui.fxui;

import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SearchUI extends Pane implements SearchInterface
{
    public SearchUI()
    {
        Label label = new Label("Search Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: crimson");
    }

    @Override
    public void setGUIController(GUIControllerInterface guiController)
    {

    }

    @Override
    public GUIControllerInterface getGUIController()
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
