package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.util.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class TagPanel extends ScrollPane
{
    Set<String> selectedTags;
    Set<String> newTagSet;
    Set<String> availableTags;
    Search<String> search;
    TextField name;
    FlowPane availableTagsPanel;//All tags that match the search query
    FlowPane tagsIngroupPanel;////All the other tags the bookmarks that contain at least one of the tags matched in the search
    FlowPane selectedTagsPanel;//Tags selected to be added to the bookmark
    FlowPane tagsInBookmarksGroupPanel;//All other tags from bookmarks that contain all of the selected

    private String colorString = "#8fffff";
    private String tagColorString = "#8f9999";
    private String newTagColorString = "aqua";

    public TagPanel(Set<String> selectedTags)
        throws Exception
    {
        this.setStyle("-fx-background:" + colorString);
        this.setFitToWidth(true);
        this.setPadding(new Insets(5, 5, 5, 5));

        createPane();

        if (selectedTags != null)
        {
            this.selectedTags = selectedTags;
        }
        else
        {
            this.selectedTags = new HashSet<>();
        }

        availableTags = new HashSet<>();
        newTagSet = new HashSet<>();
        search = new Search<>();
        Set<String> availableTags = Bootstrap.context().getAllTags();

        for (String s : availableTags)
        {
            search.add(s, s);
        }

        updateUI();
    }

    private void updateUI()
        throws Exception
    {
        availableTagsPanel.getChildren().clear();
        tagsIngroupPanel.getChildren().clear();
        selectedTagsPanel.getChildren().clear();
        tagsInBookmarksGroupPanel.getChildren().clear();

        if (name.getText().isEmpty())
        {
            availableTags = search.getFullTextWords();
        }
        else
        {
            availableTags = search.searchAll(name.getText(), 10000);//TODO ADD A SEARCH ALL SO THE SEARCH IS NOT LIMITED...
        }

        for (String s : availableTags)
        {
            if (!selectedTags.contains(s))
            {
                availableTagsPanel.getChildren().add(getRegularTagPane(s));
            }
        }

        for (String s : selectedTags)
        {
            selectedTagsPanel.getChildren().add(getSelectedTagPane(s));
        }

        //Get all bookmarks that have any tag in the available tags list.
        List<AbstractBookmark> bks = Filter.use(Bootstrap.context().getBookmarks()).keepWithAnyTag(selectedTags).results();

        Set<String> tags = Bootstrap.context().getTagsFromBookmarks(bks);
        tags.removeAll(selectedTags);

        for (String s : tags)
        {
            if (availableTags.contains(s))
            {//If it's in the available tags list then allow it here as well
                tagsIngroupPanel.getChildren().add(getRegularTagPane(s));
            }
        }

        //Get all bookmarks that have any tag in the available tags list.
        bks = Filter.use(Bootstrap.context().getBookmarks()).keepWithAllTags(selectedTags).results();

        tags = Bootstrap.context().getTagsFromBookmarks(bks);
        tags.removeAll(selectedTags);

        for (String s : tags)
        {
            if (availableTags.contains(s))
            {
                tagsInBookmarksGroupPanel.getChildren().add(getRegularTagPane(s));
            }
        }
        //add all tags that those bookmarks had, minus the available tags.
        //add those tags to the tags in group panel

        //get all bookmarks that contain all selected tags
        //get all tags that those bookmarks contain, minus the selected tags.
        //add those to the tags in bookmarks group panel.
    }

    public Set<String> getSelectedTags()
    {
        return selectedTags;
    }

    private Pane getRegularTagPane(final String tag)
    {
        Pane pane = new Pane();
        pane.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                try
                {
                    selectedTags.add(tag);
                    updateUI();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
        Label label = new Label(tag);
        label.setStyle("-fx-border-color: black");
        pane.setStyle("-fx-background-color: " + tagColorString);
        pane.getChildren().add(label);
        return pane;
    }

    private Pane getSelectedTagPane(String tag)
    {
        Pane pane = new Pane();
        pane.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                try
                {
                    selectedTags.remove(tag);
                    newTagSet.remove(tag);
                    updateUI();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
        Label label = new Label(tag);
        label.setStyle("-fx-border-color: black");
        if (newTagSet.contains(tag))
        {
            pane.setStyle("-fx-background-color: " + newTagColorString);
        }
        else
        {
            pane.setStyle("-fx-background-color: " + tagColorString);
        }

        pane.getChildren().add(label);
        return pane;
    }

    private void createPane()
    {
        VBox content = new VBox();
        content.setSpacing(3);

        HBox tagSearch = new HBox();
        Label label = new Label("Tag");
        name = new TextField();
        Button addButton = new Button("add");

        name.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    updateUI();
                    if (availableTags.contains(name.getText()))
                    {
                        addButton.setDisable(true);
                    }
                    else
                    {
                        addButton.setDisable(false);
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });


        addButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                selectedTags.add(name.getText());
                newTagSet.add(name.getText());
                name.clear();
                try
                {
                    updateUI();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        HBox.setMargin(label, new Insets(5, 10, 0, 0));
        HBox.setMargin(name, new Insets(0, 2, 10, 0));
        HBox.setHgrow(name, Priority.ALWAYS);
        tagSearch.getChildren().addAll(label, name, addButton);

        content.getChildren().add(tagSearch);

        availableTagsPanel = new FlowPane();
        availableTagsPanel.setHgap(1);
        availableTagsPanel.setVgap(1);

        tagsIngroupPanel = new FlowPane();
        tagsIngroupPanel.setHgap(1);
        tagsIngroupPanel.setVgap(1);

        selectedTagsPanel = new FlowPane();
        selectedTagsPanel.setHgap(1);
        selectedTagsPanel.setVgap(1);

        tagsInBookmarksGroupPanel = new FlowPane();
        tagsInBookmarksGroupPanel.setHgap(1);
        tagsInBookmarksGroupPanel.setVgap(1);

        ScrollPane availableTagsScroll = new ScrollPane();
        availableTagsScroll.setContent(availableTagsPanel);
        availableTagsPanel.maxWidthProperty().bind(availableTagsScroll.widthProperty().subtract(15));

        ScrollPane tagsInGroupScroll = new ScrollPane();
        tagsInGroupScroll.setContent(tagsIngroupPanel);
        tagsIngroupPanel.maxWidthProperty().bind(tagsInGroupScroll.widthProperty().subtract(15));

        ScrollPane selectedTagsScroll = new ScrollPane();
        selectedTagsScroll.setContent(selectedTagsPanel);
        selectedTagsPanel.maxWidthProperty().bind(selectedTagsScroll.widthProperty().subtract(15));

        ScrollPane tagsInBookmarksGroupScroll = new ScrollPane();
        tagsInBookmarksGroupScroll.setContent(tagsInBookmarksGroupPanel);
        tagsInBookmarksGroupPanel.maxWidthProperty().bind(tagsInBookmarksGroupScroll.widthProperty().subtract(15));

        HBox firstRow = new HBox();
        firstRow.setSpacing(3);
        firstRow.getChildren().addAll(tagsInGroupScroll, tagsInBookmarksGroupScroll);
        HBox.setHgrow(tagsIngroupPanel, Priority.ALWAYS);
        HBox.setHgrow(tagsInBookmarksGroupPanel, Priority.ALWAYS);
        firstRow.setPrefHeight(180);

        HBox secondRow = new HBox();
        secondRow.setSpacing(3);
        secondRow.getChildren().addAll(availableTagsScroll, selectedTagsScroll);
        HBox.setHgrow(availableTagsPanel, Priority.ALWAYS);
        HBox.setHgrow(selectedTagsPanel, Priority.ALWAYS);
        secondRow.setPrefHeight(180);

        VBox.setVgrow(firstRow, Priority.ALWAYS);
        VBox.setVgrow(secondRow, Priority.ALWAYS);

        content.getChildren().addAll(firstRow, secondRow);

        this.setContent(content);
    }

    public static TagPanel getNew(Set<String> selectedTags)
        throws Exception
    {
        return new TagPanel(selectedTags);
    }

}
