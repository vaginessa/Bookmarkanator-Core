package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.fxui.components.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TypesUI extends VBox implements BKTypesInterface
{
    private boolean editMode = false;
    private VBox vBox;
    private Set<AbstractUIBookmark> types;
    private Set<String> selectedTypes;
    private Set<String> highlightedTypes;
    private String backgroundColor = "lightgray";
    private String currentSearchTerm;
    private boolean isFound;
    private UIControllerInterface controller;

    //TODO Change type button theme/colors so that its easier to tell when they are selected.

    public TypesUI(UIControllerInterface controller)
    {
        Objects.requireNonNull(controller);
        this.controller = controller;
        this.setSpacing(5);
        this.setStyle("-fx-background-color:" + backgroundColor);

        this.vBox = new VBox();
        this.vBox.setSpacing(5);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background:" + backgroundColor);

        scrollPane.setContent(vBox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.getChildren().addAll(getSelectUnselect(), scrollPane);
    }

    private Pane getSelectUnselect()
    {
        HBox hBox = new HBox();
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color:" + backgroundColor);
        Button all = new Button("All");
        all.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    controller.showAllTypes();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        Button none = new Button("None");

        none.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    controller.hideAllTypes();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        hBox.getChildren().addAll(all, none);

        return hBox;
    }

    @Override
    public void setTypes(Set<AbstractUIBookmark> types, Set<String> selectedTypes, Set<String> highlightedTypes)
    {
        this.vBox.getChildren().clear();
        this.types = types;
        this.selectedTypes = selectedTypes;
        this.highlightedTypes = highlightedTypes;

        List<AbstractUIBookmark> typesList = new ArrayList<>(types);

        Collections.sort(typesList, new UIBookmarkComparator());

        for (AbstractUIBookmark bookmarkUI : typesList)
        {
            String typeName = bookmarkUI.getBookmark().getTypeName();
            //TODO Add bookmark image to the toggle button here.
            ToggleButton toggleButton = new ToggleButton(typeName);

            toggleButton.setOnAction(new EventHandler<ActionEvent>()
            {

                @Override
                public void handle(ActionEvent event)
                {
                    try
                    {
                        controller.toggleShowType(bookmarkUI);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });

            //Note: using bookmark class name here so that search options doesn't have to know about the UI.
            if (this.selectedTypes.contains(bookmarkUI.getRequiredBookmarkClassName()))
            {
                toggleButton.setSelected(true);
                if (this.highlightedTypes.contains(bookmarkUI.getRequiredBookmarkClassName()))
                {
                    toggleButton.setStyle("-fx-background-color: lightgreen");
                    isFound = true;
                }
            }

            this.vBox.getChildren().add(toggleButton);
        }
    }
    //
    //    @Override
    //    public Set<String> getSelectedTypes()
    //    {
    //        return this.selectedTypes;
    //    }
    //
    //    @Override
    //    public Set<String> getAllTypes()
    //    {
    //        return this.types;
    //    }

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
