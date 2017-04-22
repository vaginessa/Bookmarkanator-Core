package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.core.*;
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
    private UIControllerInterface controller;

    public BookmarksListUI(UIControllerInterface controller)
    {
        Objects.requireNonNull(controller);
        this.controller = controller;
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
                            controller.updateUI();
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
    public void setVisibleBookmarks(Set<AbstractUIBookmark> bookmarks)
        throws Exception
    {
        observableList.clear();

        List<AbstractUIBookmark> bookmarksList = new ArrayList<>();
        bookmarksList.addAll(bookmarks);

        Collections.sort(bookmarksList, new Comparator<AbstractUIBookmark>()
        {
            @Override
            public int compare(AbstractUIBookmark o1, AbstractUIBookmark o2)
            {
                if (o1.getBookmark().getName()==null || o1.getBookmark().getName().isEmpty())
                {
                    if (o2.getBookmark().getName()==null || o2.getBookmark().getName().isEmpty())
                    {
                        return 0;
                    }
                    else
                    {
                        return -1;
                    }
                }
                else if (o2.getBookmark().getName()==null || o2.getBookmark().getName().isEmpty())
                {
                    if (o1.getBookmark().getName()==null || o1.getBookmark().getName().isEmpty())
                    {
                        return 0;
                    }
                    else
                    {
                        return 1;
                    }
                }
                else
                {
                    return o1.getBookmark().getName().compareTo(o2.getBookmark().getName());
                }
            }
        });

        observableList.addAll(bookmarksList);

//        for (AbstractUIBookmark bk : bookmarksList)
//        {
//            String bkClassNameKey = Main.getUIClassString() + bk.getClass().getCanonicalName();
//            String className = controller.getContent().getSetting(bkClassNameKey).getValue();
//
//
//            observableList.add(bkui);
//        }
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

    @Override
    public UIControllerInterface getController()
    {
        return this.controller;
    }

    @Override
    public void setController(UIControllerInterface controller)
    {
        this.controller = controller;
    }
}
