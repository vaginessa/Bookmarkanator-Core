package com.bookmarkanator.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MenuPanel extends Pane
{
    public MenuPanel()
    {
        Label label = new Label("Menu Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: crimson");
//        this.setPrefHeight(100);
    }
}
