package com.bookmarkanator.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AvailablePanel extends Pane implements UIInterface
{
    private static AvailablePanel availablePanel;

    public AvailablePanel()
    {
        Label label = new Label("Available Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: #8fbc8f");
    }

    public static AvailablePanel use()
    {
        if (availablePanel==null)
        {
            availablePanel = new AvailablePanel();
        }
        return availablePanel;
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
