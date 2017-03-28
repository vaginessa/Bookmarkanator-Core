package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.fxui.components.*;
import com.bookmarkanator.ui.interfaces.*;
import com.bookmarkanator.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class SelectedTagsUI extends ScrollPane implements SelectedTagsInterface
{
    private boolean editMode = false;
    private HBox hBox;//Contains each tag group
    private VBox vBox;//Contains all the components
    private String colorString = "#3fccff";
    private UIControllerInterface controller;

    public SelectedTagsUI(UIControllerInterface controller)
    {
        Objects.requireNonNull(controller);
        this.controller = controller;
        this.vBox = new VBox();

        //        vBox.setStyle("-fx-background-color:"+colorString);

        this.hBox = new HBox();
        //        hBox.setStyle("-fx-background-color:"+colorString);

        //        this.setStyle("-fx-background:"+colorString);
        this.setFitToWidth(true);

        vBox.getChildren().add(getOptionsPane());

        vBox.getChildren().add(hBox);
        //        vBox.setStyle("-fx-border-color: blue");
        VBox.setVgrow(hBox, Priority.ALWAYS);
        //        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.setContent(vBox);
        this.setFitToHeight(true);
    }

    private Pane getOptionsPane()
    {
        HBox optionsPane = new HBox();
        optionsPane.setStyle("-fx-background-color:lightgray");
        optionsPane.setAlignment(Pos.TOP_RIGHT);

        Button clearButton = new Button("Clear");
        clearButton.setStyle("-fx-background-radius:15");
        clearButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    controller.clearAllSelectedTagGroups();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        ObservableList<String> options = FXCollections.observableArrayList();
        options.add(SearchOptions.INCLUDE_BOOKMARKS_WITH_ALL_TAGS);
        options.add(SearchOptions.INCLUDE_BOOKMARKS_WITH_ANY_TAGS);
        options.add(SearchOptions.INCLUDE_BOOKMARKS_WITHOUT_TAGS);

        final ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.setStyle("-fx-background-radius:15");
        comboBox.getSelectionModel().select(SearchOptions.INCLUDE_BOOKMARKS_WITH_ALL_TAGS);
        comboBox.getSelectionModel().selectedItemProperty().addListener((options1, oldValue, newValue) -> {
            try
            {
                controller.setTagModeForCurrentGroup(newValue);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        Button addButton = new Button("+");
        addButton.setStyle("-fx-background-radius:15");
        addButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    controller.addTagGroup();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        optionsPane.getChildren().add(addButton);
        optionsPane.getChildren().add(comboBox);
        optionsPane.getChildren().add(clearButton);

        return optionsPane;
    }

    @Override
    public void setSelectedTags(List<SearchOptions.TagsInfo> selectedTagGroups, Set<String> tagsToHighlight,
        Set<SearchOptions.TagsInfo> groupsToHighlight, Set<String> tagsToHighlightBorder)
    {
        this.hBox.getChildren().clear();
        //        this.hBox.setStyle("-fx-border-color: red");
        boolean showDeleteButton = selectedTagGroups.size() > 1;

        for (SearchOptions.TagsInfo tagsInfo : selectedTagGroups)
        {
            Pane p = getTagGroupPanel(tagsInfo, tagsToHighlight, groupsToHighlight.contains(tagsInfo), tagsToHighlightBorder, showDeleteButton);
            hBox.getChildren().add(p);
            HBox.setHgrow(p, Priority.ALWAYS);
        }
    }

    private Pane getTagGroupPanel(SearchOptions.TagsInfo tagsInfo, Set<String> highlightTags,boolean highlight, Set<String> tagsToHighlightBorder,  boolean showDeleteButton)
    {
        VBox groupPanelVBox = new VBox();

        if (highlight)
        {
            groupPanelVBox.setStyle("-fx-border-color: red");
        }
        else
        {
            groupPanelVBox.setStyle("-fx-border-color: lightblue");
        }

        groupPanelVBox.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent me)
            {
                try
                {
                    controller.setCurrentGroup(tagsInfo);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        groupPanelVBox.setFillWidth(true);

        FlowPane groupPanel = new FlowPane();
        groupPanel.setVgap(5);
        groupPanel.setHgap(5);

        //        if (highlight)
        //        {
        //            groupPanel.setStyle("-fx-border-color: lightblue");
        //        }

        if (showDeleteButton)
        {
            HBox deleteBox = new HBox();

            HBox labelBox = new HBox();
            Label operation = new Label("(" + tagsInfo.getOperation() + ")");
            labelBox.getChildren().add(operation);
            labelBox.setAlignment(Pos.CENTER);
            HBox.setHgrow(labelBox, Priority.ALWAYS);
            deleteBox.getChildren().add(labelBox);

            Button deleteButton = new Button("x");
            deleteButton.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    try
                    {
                        controller.removeTagGroup(tagsInfo);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });
            deleteBox.setAlignment(Pos.BOTTOM_RIGHT);
            deleteBox.getChildren().add(deleteButton);
            groupPanelVBox.getChildren().add(deleteBox);
        }

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tagsInfo.getTags());

        Collections.sort(tagsList, new TagComparator());

        for (final String string : tagsList)

        {
            Label label = new Label(string);

            label.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    try
                    {
                        controller.removeTagFromGroup(tagsInfo, string);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });


            if (highlightTags.contains(string))
            {
                if (tagsToHighlightBorder.contains(string))
                {
                    label.setStyle("-fx-background-color: lightgreen;-fx-border-color: orange;-fx-border-width: 2px");
                }
                else
                {
                    label.setStyle("-fx-background-color: lightgreen;-fx-border-color: black");
                }
            }
            else
            {
                label.setStyle("-fx-background-color: mintcream;-fx-border-color: black");
            }
            groupPanel.getChildren().add(label);
        }

        groupPanelVBox.getChildren().add(groupPanel);

        groupPanelVBox.setFillWidth(true);
        VBox.setVgrow(groupPanel, Priority.ALWAYS);
        VBox.setVgrow(groupPanelVBox, Priority.ALWAYS);

        return groupPanelVBox;
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
