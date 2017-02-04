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
    private GUIControllerInterface guiController;
    private boolean editMode = false;
    private TextField textField;
    private HBox searchBox;
    private Pane searchOptions;
    private String colorString = "#3fccdb";

    public SearchUI()
    {
        this.getChildren().add(getSearch());
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-background-color: crimson");
    }

    private Pane getSearch()
    {
        this.searchOptions = getSearchOptions();

        this.searchBox = new HBox();
        searchBox.setStyle("-fx-background-color:" + colorString);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        textField = new TextField();
        textField.setPromptText("Search");
        textField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    getGUIController().setSearchTerm(newValue);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });


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
                        searchBox.getChildren().remove(button);
                        searchBox.getChildren().add(searchOptions);
                        searchBox.getChildren().add(button);
                        button.setText("<-");
                    }
                    else
                    {
                        searchBox.getChildren().remove(searchOptions);
                        button.setText("->");
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        searchBox.getChildren().add(textField);
        searchBox.getChildren().addAll(button);

        return searchBox;
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
                    getGUIController().setSearchInclusions(GUIController.SEARCH_TAGS_KEY, sTags.isSelected());
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
                    getGUIController().setSearchInclusions(GUIController.SEARCH_BOOKMARK_NAMES_KEY, sBookmarkNames.isSelected());
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
                    getGUIController().setSearchInclusions(GUIController.SEARCH_BOOKMARK_TEXT_KEY, sBookmarkText.isSelected());
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
                    getGUIController().setSearchInclusions(GUIController.SEARCH_TYPES_KEY, sBookmarkTypeNames.isSelected());
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
    public void setGUIController(GUIControllerInterface guiController)
    {
        this.guiController = guiController;
    }

    @Override
    public GUIControllerInterface getGUIController()
    {
        return this.guiController;
    }

    @Override
    public void enterEditMode()
    {
        this.editMode = true;
    }

    @Override
    public void exitEditMode()
    {
        this.editMode = false;
    }

    @Override
    public boolean isEditMode()
    {
        return this.editMode;
    }
}
