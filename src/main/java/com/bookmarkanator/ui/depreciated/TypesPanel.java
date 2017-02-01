package com.bookmarkanator.ui.depreciated;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TypesPanel extends Pane
{
    public TypesPanel()
    {
        Label label = new Label("Types Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: violet");
    }
}
