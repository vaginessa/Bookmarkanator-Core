package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TypesUI extends VBox implements BKTypes
{
    private boolean editMode = false;
    private VBox vBox;
    private Set<String> types;
    private Set<String> visibleTypes;
    private String backgroundColor = "paleturquoise";

    //TODO Add type icons that match bookmark type icons
    //TODO Change type button theme/colors so that its easier to tell when they are selected.

    public TypesUI()
    {
        this.setSpacing(5);
        this.setStyle("-fx-background-color:"+backgroundColor);

        this.vBox = new VBox();
        this.vBox.setSpacing(5);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background:"+backgroundColor);

        scrollPane.setContent(vBox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.getChildren().addAll(getSelectUnselect(), scrollPane);
    }

    private Pane getSelectUnselect()
    {
        HBox hBox = new HBox();
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color:"+backgroundColor);
        Button all = new Button("All");
        all.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try
                {
                    UIController.use().showTypes(types);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        Button none = new Button("None");

        none.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try
                {
                    UIController.use().showTypes(null);
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
    public void setTypes(Set<String> types, Set<String> showTypes)
    {
        this.vBox.getChildren().clear();
        this.types = types;
        this.visibleTypes = showTypes;

        for (final String string: types)
        {
            ToggleButton toggleButton = new ToggleButton(string);

            toggleButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    try
                    {
                        UIController.use().toggleShowType(string);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });

            if (visibleTypes.contains(string))
            {
                toggleButton.setSelected(true);
            }

            this.vBox.getChildren().add(toggleButton);
        }
    }

    @Override
    public Set<String> getVisibleTypes()
    {
        return this.visibleTypes;
    }

    @Override
    public Set<String> getAllTypes()
    {
        return this.types;
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
