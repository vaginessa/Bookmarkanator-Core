package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class AvailableTagsUI extends FlowPane implements AvailableTagsInterface
{
    private GUIControllerInterface guiController;
    private boolean editMode = false;

    public AvailableTagsUI()
    {
        this.setStyle("-fx-background-color: #8fbc8f");
        this.setVgap(5);
        this.setHgap(5);
    }

    @Override
    public void setAvailableTags(Set<String> availableTags)
    {
        this.getChildren().clear();
        for (final String string: availableTags)
        {
            Pane pane = new Pane();
            pane.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    try
                    {
                        getGUIController().addSelectedTag(string);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });
            Label label = new Label(string);
            label.setStyle("-fx-border-color: black");
            pane.setStyle("-fx-background-color: #8fbc61");
            pane.getChildren().add(label);

            this.getChildren().add(pane);
        }
    }

    @Override
    public Set getAvailableTags()
    {
        return null;
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
