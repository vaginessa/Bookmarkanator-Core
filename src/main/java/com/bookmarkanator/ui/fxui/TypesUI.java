package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TypesUI extends ScrollPane implements BKTypesInterface
{
    private GUIControllerInterface guiController;
    private boolean editMode = false;
    private VBox vBox;
    private Set<String> types;
    private Set<String> visibleTypes;
    private HBox hBox;

    //TODO Add type icons that match bookmark type icons
    //TODO Change type button theme/colors so that its easier to tell when they are selected.

    public TypesUI()
    {
        this.vBox = new VBox();
        vBox.setSpacing(5);

        hBox = new HBox();
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: lightgrey");
        Button all = new Button("All");
        all.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try
                {
                    System.out.println("action all ");
                    guiController.showTypes(types);
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
                System.out.println("action none ");
                guiController.showTypes(null);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    });

        hBox.getChildren().addAll(all, none);
        vBox.getChildren().add(hBox);

        this.setContent(vBox);
    }

    @Override
    public void setTypes(Set<String> types, Set<String> showTypes)
    {
        this.vBox.getChildren().clear();
        this.vBox.getChildren().add(hBox);

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
                        getGUIController().toggleShowType(string);
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
    public Set<String> getTypes()
    {
        return this.types;
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
