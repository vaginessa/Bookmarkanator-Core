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

    public TypesUI()
    {
        this.vBox = new VBox();
        vBox.setSpacing(5);
        this.setContent(vBox);
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
                        getGUIController().toggleShowType(string);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });

            if (!visibleTypes.contains(string))
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
