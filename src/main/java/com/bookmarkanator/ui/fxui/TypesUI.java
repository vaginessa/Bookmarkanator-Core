package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TypesUI extends Pane implements BKTypesInterface
{
    public TypesUI()
    {
        Label label = new Label("Types Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: violet");
    }

    @Override
    public void setTypes(Set<String> bookmarkTypes)
    {

    }

    @Override
    public Set<String> getTypes()
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
