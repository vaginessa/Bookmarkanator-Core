package com.bookmarkanator.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class DocView extends Pane
{
    public DocView()
    {
        Label label = new Label("Doc Panel");
        this.getChildren().add(label);
    }
}
