package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SelectedTagsUI extends Pane implements SelectedTagsInterface
{
    public SelectedTagsUI()
    {
        Label label = new Label("Selected Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: goldenrod");
    }

    @Override
    public void setSelectedTags(Set<String> selectedTags)
    {

    }

    @Override
    public Set<String> getSelectedTags()
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
