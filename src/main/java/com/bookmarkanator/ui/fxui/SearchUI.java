package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.interfaces.*;
import com.bookmarkanator.util.*;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class SearchUI extends HBox implements SearchInterface
{
    private boolean editMode = false;
    private String colorString = "#3fccdb";
    private TextField textField;
    private HBox searchBox;
    private Pane searchOptionsPane;
    private Button newButton, quickPanelButton;
    private HBox editModeTogglePane;
    private HBox editModePane;
    private SearchOptions searchOptions;
    private UIControllerInterface controller;
    private CheckBox sBookmarkTypeNames;
    private CheckBox sBookmarkText;
    private CheckBox sBookmarkNames;
    private CheckBox sTags;
    private boolean highlightSearchTerm;
    private SimilarItemIterator similarItemIterator;

    public SearchUI(UIControllerInterface controller)
    {
        Objects.requireNonNull(controller);
        this.controller = controller;
        this.searchOptions = controller.getSearchOptions();
        this.getChildren().add(getSearch());
        this.setAlignment(Pos.CENTER_LEFT);
        //        searchOptionsPane = new SearchOptions();
        //        this.setStyle("-fx-background-color: crimson");
    }

    private Pane getSearch()
    {
        this.searchOptionsPane = getSearchOptionsPane();

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
                    searchOptions.setSearchTerm(newValue);
                    controller.setSearchOptions(searchOptions);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
        textField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent ke)
            {
                if (ke.getCode() == KeyCode.ENTER)
                {
                    try
                    {
                        controller.selectTag(searchOptions.getSearchTerm());
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                    textField.clear();
                    similarItemIterator = null;

                }
                else if (ke.getCode() == KeyCode.TAB)
                {
                    if (similarItemIterator==null)
                    {
                        similarItemIterator = controller.getSimilarItemIterator(textField.getText());
                    }

                    textField.setText(similarItemIterator.getNextSimilar());

                    ke.consume();
                }
                else if (ke.getCode() == KeyCode.ESCAPE)
                {
                    textField.clear();
                    searchOptions.setSearchTerm("");
                    try
                    {
                        controller.setSearchOptions(searchOptions);
                        similarItemIterator = null;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
//                else {
//                    similarItemIterator = null;
//                }
                //
                //                String text = "Key Typed: " + ke.getCharacter();
                //                if (ke.isAltDown()) {
                //                    text += " , alt down";
                //                }
                //                if (ke.isControlDown()) {
                //                    text += " , ctrl down";
                //                }
                //                if (ke.isMetaDown()) {
                //                    text += " , meta down";
                //                }
                //                if (ke.isShiftDown()) {
                //                    text += " , shift down";
                //                }

            }
        });

        textFieldContainer.getChildren().add(textField);

        Button clearText = new Button("X");
        clearText.setStyle(
            "-fx-border-color: black;-fx-border-radius:15;-fx-background-radius:15;-fx-background-color:white;");//-fx-min-width: 20px;-fx-min-height: 20px;-fx-max-width: 20px; -fx-max-height: 20px;-fx-background-radius: 20em;");
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
                    searchOptions.setSearchTerm("");
                    Platform.runLater(textField::requestFocus);
                    controller.setSearchOptions(searchOptions);
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
                        searchBox.getChildren().addAll(searchOptionsPane, button, newButton, editModePane);
                        button.setText("<-");
                    }
                    else
                    {
                        searchBox.getChildren().removeAll(searchOptionsPane, button, editModePane, newButton);
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
                    controller.getNewBookmarkSelectorUI().show();
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
                        controller.setEditMode(editMode);
                    }
                    else
                    {
                        editModeTogglePane.setAlignment(Pos.CENTER_RIGHT);
                        button.setText("On");
                        editMode = true;
                        controller.setEditMode(editMode);
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

    private Pane getSearchOptionsPane()
    {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        sTags = new CheckBox("Tags");
//        sTags.setSelected(true);
        sTags.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    searchOptions.setSearchTags(sTags.isSelected());
                    controller.setSearchOptions(searchOptions);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        sBookmarkNames = new CheckBox("Bookmark Names");
//        sBookmarkNames.setSelected(true);
        sBookmarkNames.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    searchOptions.setSearchBookmarkNames(sBookmarkNames.isSelected());
                    controller.setSearchOptions(searchOptions);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        sBookmarkText = new CheckBox("Bookmark Text");
//        sBookmarkText.setSelected(true);
        sBookmarkText.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    searchOptions.setSearchBookmarkText(sBookmarkText.isSelected());
                    controller.setSearchOptions(searchOptions);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        sBookmarkTypeNames = new CheckBox("Bookmark Type Names");
        sBookmarkTypeNames.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    searchOptions.setSearchBookmarkTypes(sBookmarkTypeNames.isSelected());
                    controller.setSearchOptions(searchOptions);
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

    public void setSearchOptions(SearchOptions searchOptions)
    {
        this.searchOptions = searchOptions;
        textField.setText(searchOptions.getSearchTerm());
        sTags.setSelected(searchOptions.isSearchTags());
        sBookmarkNames.setSelected(searchOptions.isSearchBookmarkNames());
        sBookmarkTypeNames.setSelected(searchOptions.isSearchBookmarkTypes());
        sBookmarkText.setSelected(searchOptions.isSearchBookmarkText());
    }

    @Override
    public void highlightSearchTerm(boolean highlight)
    {
        this.highlightSearchTerm = highlight;

        if (highlight)
        {
            textField.setStyle("-fx-border-color: orange;");
        }
        else
        {
            textField.setStyle("-fx-border-color: white;");
        }
    }
}
