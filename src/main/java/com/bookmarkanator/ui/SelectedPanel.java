package com.bookmarkanator.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SelectedPanel extends Pane
{
    public SelectedPanel()
    {
        Label label = new Label("Selected Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: goldenrod");
    }
}
