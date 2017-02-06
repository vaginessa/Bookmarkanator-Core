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
    private String colorString = "#3fccdb";
    private TextField textField;
    private HBox searchBox;
    private Pane searchOptions;
    private Button newButton, quickPanelButton;
    private HBox editModePane;

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
        searchBox.setSpacing(5);

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
                        searchBox.getChildren().removeAll(button, newButton, quickPanelButton, editModePane);
                        searchBox.getChildren().addAll(searchOptions, button, newButton, editModePane);
                        button.setText("<-");
                    }
                    else
                    {
                        searchBox.getChildren().removeAll(searchOptions, button,editModePane, newButton);
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

        this.quickPanelButton = new Button("Quick Panel");
        quickPanelButton.setStyle("-fx-background-radius:15");

        searchBox.getChildren().add(textField);
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

        editModePane.setPrefWidth(60);
        editModePane.setStyle("-fx-background-color:blue;-fx-background-radius:15");
        editModePane.setAlignment(Pos.CENTER_LEFT);


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
                        editModePane.setAlignment(Pos.CENTER_LEFT);
                        button.setText("Off");
                        editMode = false;
                    }
                    else
                    {
                        editModePane.setAlignment(Pos.CENTER_RIGHT);
                        button.setText("On");
                        editMode = true;
                    }


                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        editModePane.getChildren().add(button);
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
