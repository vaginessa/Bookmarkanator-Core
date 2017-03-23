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
    private String currentSearchTerm;

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

        List<AbstractBookmark> bookmarksList = new ArrayList<>();
        bookmarksList.addAll(bookmarks);

        Collections.sort(bookmarksList, new Comparator<AbstractBookmark>()
        {
            @Override
            public int compare(AbstractBookmark o1, AbstractBookmark o2)
            {
                if (o1.getName()==null || o1.getName().isEmpty())
                {
                    if (o2.getName()==null || o2.getName().isEmpty())
                    {
                        return 0;
                    }
                    else
                    {
                        return -1;
                    }
                }
                else if (o2.getName()==null || o2.getName().isEmpty())
                {
                    if (o1.getName()==null || o1.getName().isEmpty())
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
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });

        for (AbstractBookmark bk : bookmarksList)
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

    @Override
    public void setCurrentSearchTerm(String searchTerm)
    {
        this.currentSearchTerm = searchTerm;
    }

    @Override
    public boolean isSearchTermFound()
    {
        return false;
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
