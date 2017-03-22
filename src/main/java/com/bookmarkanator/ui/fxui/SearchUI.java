package com.bookmarkanator.ui.fxui;

import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SearchUI extends HBox implements SearchInterface
{
    private boolean editMode = false;
    private String colorString = "#3fccdb";
    private TextField textField;
    private HBox searchBox;
    private Pane searchOptions;
    private Button newButton, quickPanelButton;
    private HBox editModeTogglePane;
    private HBox editModePane;

    public SearchUI()
    {
        this.getChildren().add(getSearch());
        this.setAlignment(Pos.CENTER_LEFT);
        //        this.setStyle("-fx-background-color: crimson");
    }

    private Pane getSearch()
    {
        this.searchOptions = getSearchOptions();

        this.searchBox = new HBox();
        searchBox.setSpacing(5);

        //        searchBox.setStyle("-fx-background-color:" + colorString);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        HBox textFieldContainer = new HBox();
        textFieldContainer.setStyle("-fx-background:white");

        textField = new TextField();
        textField.setStyle("-fx-text-box-border: transparent;-fx-background-color: -fx-control-inner-background;");
        textField.setPromptText("Search");
        textField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    UIController.use().setSearchTerm(newValue);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        textFieldContainer.getChildren().add(textField);

        Button clearText = new Button("X");
        clearText.setStyle("-fx-border-color: black;-fx-border-radius:15;-fx-background-radius:15;-fx-background-color:white;");//-fx-min-width: 20px;-fx-min-height: 20px;-fx-max-width: 20px; -fx-max-height: 20px;-fx-background-radius: 20em;");
//        clearText.setMinSize(25,25);
//        clearText.setMaxSize(25,25);
        clearText.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    textField.clear();
                    UIController.use().setSearchTerm(textField.getText());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        textFieldContainer.getChildren().add(clearText);
        textFieldContainer.setStyle("-fx-border-color: black;-fx-background-color:white;");//-fx-background-radius:15;-fx-border-radius:15");

        final Button button = new Button("->");

        button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    if (button.getText().equals("->"))
                    {
                        searchBox.getChildren().removeAll(button, newButton, quickPanelButton, editModePane);
                        searchBox.getChildren().addAll(searchOptions, button, newButton, editModePane);
                        button.setText("<-");
                    }
                    else
                    {
                        searchBox.getChildren().removeAll(searchOptions, button, editModePane, newButton);
                        searchBox.getChildren().addAll(button, newButton, quickPanelButton, editModePane);
                        button.setText("->");
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        this.newButton = new Button("+");
        newButton.setStyle("-fx-background-radius:15");

        newButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    UIController.use().getNewBookmarkSelectorUI().show();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        this.quickPanelButton = new Button("Quick Panel");
        quickPanelButton.setStyle("-fx-background-radius:15");

        searchBox.getChildren().add(textFieldContainer);
        searchBox.getChildren().addAll(button);
        searchBox.getChildren().add(newButton);
        searchBox.getChildren().add(quickPanelButton);
        searchBox.getChildren().add(getTogglePane());

        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.setFillHeight(false);
        return searchBox;
    }

    private HBox getTogglePane()
    {
        this.editModePane = new HBox();
        editModePane.setAlignment(Pos.CENTER);
        editModePane.setSpacing(5);

        this.editModeTogglePane = new HBox();

        editModeTogglePane.setPrefWidth(60);
        editModeTogglePane.setStyle("-fx-background-color:aliceblue;-fx-background-radius:15;-fx-border-color: black;-fx-border-radius:15");
        editModeTogglePane.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label("Edit Mode");

        Button button = new Button("Off");
        button.setStyle("-fx-background-radius:15");
        button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    if (editMode)
                    {
                        editModeTogglePane.setAlignment(Pos.CENTER_LEFT);
                        button.setText("Off");
                        editMode = false;
                        UIController.use().setEditMode(editMode);
                    }
                    else
                    {
                        editModeTogglePane.setAlignment(Pos.CENTER_RIGHT);
                        button.setText("On");
                        editMode = true;
                        UIController.use().setEditMode(editMode);
                    }

                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        editModeTogglePane.getChildren().add(button);
        editModePane.getChildren().addAll(label, editModeTogglePane);
        return editModePane;
    }

    private Pane getSearchOptions()
    {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        final CheckBox sTags = new CheckBox("Tags");
        sTags.setSelected(true);
        sTags.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    UIController.use().setSearchInclusions(UIController.SEARCH_TAGS_KEY, sTags.isSelected());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        final CheckBox sBookmarkNames = new CheckBox("Bookmark Names");
        sBookmarkNames.setSelected(true);
        sBookmarkNames.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    UIController.use().setSearchInclusions(UIController.SEARCH_BOOKMARK_NAMES_KEY, sBookmarkNames.isSelected());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        final CheckBox sBookmarkText = new CheckBox("Bookmark Text");
        sBookmarkText.setSelected(true);
        sBookmarkText.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    UIController.use().setSearchInclusions(UIController.SEARCH_BOOKMARK_TEXT_KEY, sBookmarkText.isSelected());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        final CheckBox sBookmarkTypeNames = new CheckBox("Bookmark Type Names");
        sBookmarkTypeNames.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    UIController.use().setSearchInclusions(UIController.SEARCH_TYPES_KEY, sBookmarkTypeNames.isSelected());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        gridPane.add(sTags, 0, 0, 1, 1);
        gridPane.add(sBookmarkNames, 1, 0, 1, 1);
        gridPane.add(sBookmarkTypeNames, 0, 1, 1, 1);
        gridPane.add(sBookmarkText, 1, 1, 1, 1);

        return gridPane;
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

}
