package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BookmarksListUI extends Pane implements BookmarksListInterface
{
    public BookmarksListUI()
    {
        Label label = new Label("Bookmarks Panel");
        this.getChildren().add(label);
        this.setStyle("-fx-background-color: cyan");
    }

    @Override
    public void setVisibleBookmarks(Set<AbstractBookmark> bookmarks)
        throws Exception
    {

    }

    @Override
    public Set<AbstractBookmark> getVisibleBookmarks()
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
