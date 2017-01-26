package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AvailableTagsUI extends Pane implements AvailableTagsInterface
{
    public AvailableTagsUI()
    {
        Label label = new Label("Available Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: #8fbc8f");
    }

    @Override
    public void setAvailableTags(Set<String> availableTags)
    {

    }

    @Override
    public Set getAvailableTags()
    {
        return null;
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
