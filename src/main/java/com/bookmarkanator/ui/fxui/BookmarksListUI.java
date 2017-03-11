package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.*;

public class BookmarksListUI extends ScrollPane implements BookmarksListInterface
{
    private ListView<AbstractUIBookmark> bookmarkListView;
    private ObservableList<AbstractUIBookmark> observableList;
    private GUIControllerInterface guiController;
    private boolean editMode = false;

    public BookmarksListUI()
    {
        observableList = FXCollections.observableArrayList();
        bookmarkListView = new ListView<>(observableList);
        this.setContent(bookmarkListView);

        bookmarkListView.setCellFactory(new Callback<ListView<AbstractUIBookmark>, ListCell<AbstractUIBookmark>>()
        {
            @Override
            public ListCell<AbstractUIBookmark> call(ListView<AbstractUIBookmark> list)
            {
                return new BookmarkCell();
            }

        });

        bookmarkListView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                //TODO Handle right clicks, and other kinds of clicks.
                try
                {
                    AbstractUIBookmark abs = bookmarkListView.getSelectionModel().getSelectedItem();
                    if (abs!=null)
                    {
                        if (getEditMode())
                        {
                            abs.getTypeIcon();
                        }
                        else
                        {
                            abs.show();
                        }
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        this.setStyle("-fx-background-color: cyan");
        this.setFitToWidth(true);
    }

    @Override
    public void setVisibleBookmarks(Set<AbstractBookmark> bookmarks)
        throws Exception
    {
        observableList.clear();
        for (AbstractBookmark bk : bookmarks)
        {
            String bkClassNameKey = Main.getUIClassString() + bk.getClass().getCanonicalName();
            String className = this.getGUIController().getSettings().getSetting(bkClassNameKey).getValue();
            final AbstractUIBookmark bkui = ModuleLoader.use()
                .loadClass(className, AbstractUIBookmark.class, this.getGUIController().getBootstrap().getClassLoader());
            bkui.setBookmark(bk);

            observableList.add(bkui);
        }
    }

    @Override
    public Set<AbstractBookmark> getVisibleBookmarks()
    {
        return null;
    }

    @Override
    public void setGUIController(GUIControllerInterface guiController)
    {
        this.guiController = guiController;
    }

    @Override
    public boolean getEditMode()
    {
        return this.editMode;
    }

    @Override
    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }

    @Override
    public GUIControllerInterface getGUIController()
    {
        return this.guiController;
    }



    private class BookmarkCell extends ListCell<AbstractUIBookmark>
    {
        @Override
        public void updateItem(AbstractUIBookmark item, boolean empty)
        {
            super.updateItem(item, empty);
            if (empty)
            {
                setText(null);
                setGraphic(null);
            }
            else if (item != null)
            {
                setText(item.getBookmark().getName());
            }

        }
    }
}
