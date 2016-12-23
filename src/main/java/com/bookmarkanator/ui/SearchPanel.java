package com.bookmarkanator.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SearchPanel extends Pane
{
    public SearchPanel()
    {
        Label label = new Label("Search Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: crimson");
//        this.setPrefHeight(100);
    }
}
