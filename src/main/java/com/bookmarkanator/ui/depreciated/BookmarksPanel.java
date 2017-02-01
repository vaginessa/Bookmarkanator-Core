package com.bookmarkanator.ui.depreciated;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BookmarksPanel extends Pane
{
    public BookmarksPanel()
    {
        Label label = new Label("Bookmarks Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: cyan");
    }
}
