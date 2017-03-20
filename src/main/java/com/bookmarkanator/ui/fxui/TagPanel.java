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
    TextField tagNameSearch;
    FlowPane availableTagsPanel;//All tags that match the search query
    FlowPane looselyRelatedTags;////All the other tags the bookmarks that contain at least one of the tags matched in the search
    FlowPane selectedTagsPanel;//Tags selected to be added to the bookmark
    FlowPane stronglyRelatedTags;//All other tags from bookmarks that contain all of the selected

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
        looselyRelatedTags.getChildren().clear();
        selectedTagsPanel.getChildren().clear();
        stronglyRelatedTags.getChildren().clear();

        if (tagNameSearch.getText().isEmpty())
        {
            availableTags = search.getFullTextWords();
        }
        else
        {
            availableTags = search.searchAll(tagNameSearch.getText(), 10000);//TODO ADD A SEARCH ALL SO THE SEARCH IS NOT LIMITED...
        }

        for (String s : availableTags)
        {
            if (!selectedTags.contains(s))
            {
                availableTagsPanel.getChildren().add(getRegularTagView(s));
            }
        }

        for (String s : selectedTags)
        {
            selectedTagsPanel.getChildren().add(getSelectedTagView(s));
        }

        //Get all bookmarks that have any tag in the available tags list.
        List<AbstractBookmark> bks = Filter.use(Bootstrap.context().getBookmarks()).keepWithAnyTag(selectedTags).results();

        Set<String> strongRelatedTags = getStronglyRelatedTags(bks);

        Set<String> relatedTags = Bootstrap.context().getTagsFromBookmarks(bks);
        relatedTags.removeAll(selectedTags);

        for (String s : relatedTags)
        {
            if (availableTags.contains(s) && !strongRelatedTags.contains(s))
            {//If it's in the available tags list then allow it here as well
                looselyRelatedTags.getChildren().add(getRegularTagView(s));
            }
        }

        for (String s : strongRelatedTags)
        {
            if (availableTags.contains(s))
            {
                stronglyRelatedTags.getChildren().add(getRegularTagView(s));
            }
        }
    }

    public Set<String> getSelectedTags()
    {
        return selectedTags;
    }

    private Set<String> getStronglyRelatedTags(List<AbstractBookmark> bks)
        throws Exception
    {

        //Get all bookmarks that have all tags in the available tags list.
        bks = Filter.use(Bootstrap.context().getBookmarks()).keepWithAllTags(selectedTags).results();

        Set<String> tags = Bootstrap.context().getTagsFromBookmarks(bks);
        tags.removeAll(selectedTags);
        return tags;
    }

    //this is the panel that represents each tag in any non selected tag panel.
    private Pane getRegularTagView(String tag)
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
                    tagNameSearch.clear();
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

    //This is the panel that represents each tag added to the tags panel
    private Pane getSelectedTagView(String tag)
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
        tagNameSearch = new TextField();
        Button addButton = new Button("add");

        tagNameSearch.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    updateUI();
                    if (availableTags.contains(tagNameSearch.getText()))
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
                if (tagNameSearch.getText()!=null && !tagNameSearch.getText().isEmpty())
                {
                    selectedTags.add(tagNameSearch.getText());
                    newTagSet.add(tagNameSearch.getText());
                    tagNameSearch.clear();
                    try
                    {
                        updateUI();
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        HBox.setMargin(label, new Insets(5, 10, 0, 0));
        HBox.setMargin(tagNameSearch, new Insets(0, 2, 10, 0));
        HBox.setHgrow(tagNameSearch, Priority.ALWAYS);
        tagSearch.getChildren().addAll(label, tagNameSearch, addButton);

        content.getChildren().add(tagSearch);

        HBox firstRow = new HBox();
        firstRow.setSpacing(3);
        firstRow.getChildren().addAll(getLooselyRelatedTagsPane(), getStronglyRelatedTagsPane());
        HBox.setHgrow(looselyRelatedTags, Priority.ALWAYS);
        HBox.setHgrow(stronglyRelatedTags, Priority.ALWAYS);
        firstRow.setPrefHeight(180);

        HBox secondRow = new HBox();
        secondRow.setSpacing(3);
        secondRow.getChildren().addAll(getAvailableTagsPane(), getSelectedTagsPane());
        HBox.setHgrow(availableTagsPanel, Priority.ALWAYS);
        HBox.setHgrow(selectedTagsPanel, Priority.ALWAYS);
        secondRow.setPrefHeight(180);

        VBox.setVgrow(firstRow, Priority.ALWAYS);
        VBox.setVgrow(secondRow, Priority.ALWAYS);

        content.getChildren().addAll(secondRow, firstRow);

        this.setContent(content);
    }

    private Pane getAvailableTagsPane()
    {
        availableTagsPanel = new FlowPane();
        availableTagsPanel.setHgap(1);
        availableTagsPanel.setVgap(1);

        ScrollPane availableTagsScroll = new ScrollPane();
        availableTagsScroll.setContent(availableTagsPanel);
        availableTagsPanel.maxWidthProperty().bind(availableTagsScroll.widthProperty().subtract(15));

        VBox vBox = new VBox();
        VBox.setVgrow(availableTagsScroll, Priority.ALWAYS);
        vBox.setStyle("-fx-border-color: black");

        Label label = new Label("All Existing Tags");

        vBox.getChildren().addAll(label, availableTagsScroll);

        return vBox;
    }

    private Pane getSelectedTagsPane()
    {
        selectedTagsPanel = new FlowPane();
        selectedTagsPanel.setHgap(1);
        selectedTagsPanel.setVgap(1);

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(selectedTagsPanel);
        selectedTagsPanel.maxWidthProperty().bind(scroll.widthProperty().subtract(15));

        VBox vBox = new VBox();
        VBox.setVgrow(scroll, Priority.ALWAYS);
        vBox.setStyle("-fx-border-color: black");

        Label label = new Label("Selected Tags");

        vBox.getChildren().addAll(label, scroll);

        return vBox;
    }

    private Pane getLooselyRelatedTagsPane()
    {
        looselyRelatedTags = new FlowPane();
        looselyRelatedTags.setHgap(1);
        looselyRelatedTags.setVgap(1);

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(looselyRelatedTags);
        looselyRelatedTags.maxWidthProperty().bind(scroll.widthProperty().subtract(15));

        VBox vBox = new VBox();
        VBox.setVgrow(scroll, Priority.ALWAYS);
        vBox.setStyle("-fx-border-color: black");

        Label label = new Label("Loosely Related Tags");

        vBox.getChildren().addAll(label, scroll);

        return vBox;
    }

    private Pane getStronglyRelatedTagsPane()
    {
        stronglyRelatedTags = new FlowPane();
        stronglyRelatedTags.setHgap(1);
        stronglyRelatedTags.setVgap(1);

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(stronglyRelatedTags);
        stronglyRelatedTags.maxWidthProperty().bind(scroll.widthProperty().subtract(15));

        VBox vBox = new VBox();
        VBox.setVgrow(scroll, Priority.ALWAYS);
        vBox.setStyle("-fx-border-color: black");

        Label label = new Label("Strongly Related Tags");

        vBox.getChildren().addAll(label, scroll);

        return vBox;
    }

    public static TagPanel getNew(Set<String> selectedTags)
        throws Exception
    {
        return new TagPanel(selectedTags);
    }

}
