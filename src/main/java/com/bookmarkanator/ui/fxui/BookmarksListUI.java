package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;

public class BookmarksListUI extends VBox implements BookmarksListInterface
{
    private ListView<AbstractUIBookmark> bookmarkListView;
    private ObservableList<AbstractUIBookmark> observableList;
    private boolean editMode = false;

    public BookmarksListUI()
    {
        observableList = FXCollections.observableArrayList();
        bookmarkListView = new ListView<>(observableList);
        this.getChildren().add(bookmarkListView);

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
                    if (abs != null)
                    {
                        if (getEditMode())
                        {
                            Bootstrap.context().updateBookmark(abs.edit());
                            UIController.use().updateUI();
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

//        this.setStyle("-fx-background-color: cyan");
        VBox.setVgrow(bookmarkListView, Priority.ALWAYS);
    }

    @Override
    public void setVisibleBookmarks(Set<AbstractBookmark> bookmarks)
        throws Exception
    {
        observableList.clear();

        for (AbstractBookmark bk : bookmarks)
        {
            String bkClassNameKey = Main.getUIClassString() + bk.getClass().getCanonicalName();
            String className = UIController.use().getSettings().getSetting(bkClassNameKey).getValue();
            final AbstractUIBookmark bkui = ModuleLoader.use().loadClass(className, AbstractUIBookmark.class);
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
    public boolean getEditMode()
    {
        return this.editMode;
    }

    @Override
    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
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
