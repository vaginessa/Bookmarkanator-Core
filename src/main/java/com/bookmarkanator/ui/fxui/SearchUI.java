package com.bookmarkanator.ui.fxui;

import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SearchUI extends Pane implements SearchInterface
{
    private GUIControllerInterface guiController;
    private boolean editMode = false;

    public SearchUI()
    {
        Label label = new Label("Search Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: crimson");
    }

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
