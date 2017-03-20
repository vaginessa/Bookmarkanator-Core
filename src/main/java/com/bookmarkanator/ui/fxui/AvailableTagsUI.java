package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class AvailableTagsUI extends ScrollPane implements AvailableTagsInterface
{
    private boolean editMode = false;
    private FlowPane flowPane;
    private String colorString =  "#8fbc8f";

    public AvailableTagsUI()
    {
        this.flowPane = new FlowPane();
        flowPane.setStyle("-fx-background-color:"+colorString);
        flowPane.setVgap(5);
        flowPane.setHgap(5);

        this.setStyle("-fx-background:"+colorString);
        this.setFitToWidth(true);
        this.setContent(flowPane);
    }

    @Override
    public void setAvailableTags(Set<String> availableTags)
    {
        this.flowPane.getChildren().clear();
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
                        UIController.use().addSelectedTag(string);
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

            this.flowPane.getChildren().add(pane);
        }
    }

    @Override
    public Set getAvailableTags()
    {
        return null;
    }

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

}
