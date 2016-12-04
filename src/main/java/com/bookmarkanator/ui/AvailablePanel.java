package com.bookmarkanator.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AvailablePanel extends Pane
{
    public AvailablePanel()
    {
        Label label = new Label("Available Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: #8fbc8f");
    }
}
