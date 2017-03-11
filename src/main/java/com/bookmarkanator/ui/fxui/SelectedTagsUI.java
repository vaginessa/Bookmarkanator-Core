package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class SelectedTagsUI extends ScrollPane implements SelectedTagsInterface
{
    private GUIControllerInterface guiController;
    private boolean editMode = false;
    private FlowPane flowPane;
    private VBox vBox;
    private String colorString =  "#3fccff";

    public SelectedTagsUI()
    {
        this.vBox = new VBox();
        vBox.setStyle("-fx-background-color:"+colorString);

        this.flowPane = new FlowPane();
        flowPane.setStyle("-fx-background-color:"+colorString);
        flowPane.setVgap(5);
        flowPane.setHgap(5);

        this.setStyle("-fx-background:"+colorString);
        this.setFitToWidth(true);

        vBox.getChildren().add(getOptionsPane());
        vBox.getChildren().add(flowPane);

        this.setContent(vBox);
    }

    private Pane getOptionsPane()
    {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color:lightgray");
        hBox.setAlignment(Pos.TOP_RIGHT);
        Button button = new Button("Clear");
        button.setStyle("-fx-background-radius:15");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try
                {
                    guiController.setSelectedTags(null);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        ObservableList<String> options = FXCollections.observableArrayList();
        options.add(GUIController.INCLUDE_BOOKMARKS_WITH_ALL_TAGS);
        options.add(GUIController.INCLUDE_BOOKMARKS_WITH_ANY_TAGS);
        options.add(GUIController.INCLUDE_BOOKMARKS_WITHOUT_TAGS);

        final ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.setStyle("-fx-background-radius:15");
        comboBox.getSelectionModel().select(GUIController.INCLUDE_BOOKMARKS_WITH_ALL_TAGS);
        comboBox.getSelectionModel().selectedItemProperty().addListener((options1, oldValue, newValue) -> {
            try
            {
                guiController.setTagMode(newValue);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        hBox.getChildren().add(comboBox);
        hBox.getChildren().add(button);

        return hBox;
    }

    @Override
    public void setSelectedTags(Set<String> selectedTags)
    {
        this.flowPane.getChildren().clear();
        for (final String string: selectedTags)
        {
            Pane pane = new Pane();
            pane.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    try
                    {
                        getGUIController().removeSelectedTag(string);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });
            Label label = new Label(string);
            label.setStyle("-fx-border-color: black");
            pane.setStyle("-fx-background-color: #8fbc61");
            pane.getChildren().add(label);

            this.flowPane.getChildren().add(pane);
        }
    }

    @Override
    public Set<String> getSelectedTags()
    {
        return null;
    }

    @Override
    public GUIControllerInterface getGUIController()
    {
        return this.guiController;
    }

    @Override
    public void setGUIController(GUIControllerInterface guiController)
    {
        this.guiController = guiController;
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
