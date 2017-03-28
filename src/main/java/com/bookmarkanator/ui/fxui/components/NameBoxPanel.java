package com.bookmarkanator.ui.fxui.components;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NameBoxPanel extends HBox
{
    private final TextField name;
    private final Label label;

    public NameBoxPanel()
    {
        name = new TextField();
        label = new Label("Name");
        this.getChildren().addAll(label, name);

        HBox.setMargin(label, new Insets(10, 10, 0, 10));
        HBox.setMargin(name, new Insets(5, 5, 5, 0));
        HBox.setHgrow(name, Priority.ALWAYS);
        Platform.runLater(name::requestFocus);
    }

    public NameBoxPanel(String label, String name)
    {
        this();
        this.label.setText(label);
        this.name.setText(name);
    }

    public NameBoxPanel(String name)
    {
        this();
        this.name.setText(name);
    }

    public TextField getName()
    {
        return name;
    }

    public Label getLabel()
    {
        return label;
    }
}
