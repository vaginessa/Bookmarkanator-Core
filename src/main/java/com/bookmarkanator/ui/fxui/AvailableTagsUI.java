package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.fxui.components.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class AvailableTagsUI extends ScrollPane implements AvailableTagsInterface
{
    private boolean editMode = false;
    private FlowPane flowPane;
    private String colorString = "#8fbc8f";
    private String currentSearchTerm;
    private boolean isFound;
    private UIControllerInterface controller;

    public AvailableTagsUI(UIControllerInterface controller)
    {
        Objects.requireNonNull(controller);
        this.controller = controller;
        this.flowPane = new FlowPane();
        //        flowPane.setStyle("-fx-background-color:"+colorString);
        flowPane.setVgap(5);
        flowPane.setHgap(5);

        //        this.setStyle("-fx-background:"+colorString);
        this.setFitToWidth(true);
        this.setContent(flowPane);
    }

    @Override
    public void setAvailableTags(Set<String> availableTags, Set<String> highlightTags, Set<String> tagsToHighlightBorders)
    {
        this.flowPane.getChildren().clear();

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(availableTags);

        Collections.sort(tagsList, new TagComparator());

        for (final String string : tagsList)
        {
            Pane pane = new Pane();

            pane.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    try
                    {
                        controller.selectTag(string);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });

            Label label = new Label(string);
            label.setStyle("-fx-border-color: black");
            if (highlightTags.contains(string))
            {
                if (tagsToHighlightBorders.contains(string))
                {
                    label.setStyle("-fx-border-color: orange;-fx-border-width: 2px");
                }
                pane.setStyle("-fx-background-color: lightgreen");

                isFound = true;
            }
            else
            {
                pane.setStyle("-fx-background-color: mintcream");
            }
            pane.getChildren().add(label);

            this.flowPane.getChildren().add(pane);
        }
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

    @Override
    public UIControllerInterface getController()
    {
        return this.controller;
    }

    @Override
    public void setController(UIControllerInterface controller)
    {
        this.controller = controller;
    }

}
