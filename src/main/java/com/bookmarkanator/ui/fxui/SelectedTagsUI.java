package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.*;
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
    private FlowPane flowPane;
    private VBox vBox;
    private String colorString =  "#3fccff";
    private String currentSearchTerm;
    private boolean isFound;

    public SelectedTagsUI()
    {
        this.vBox = new VBox();
//        vBox.setStyle("-fx-background-color:"+colorString);

        this.flowPane = new FlowPane();
//        flowPane.setStyle("-fx-background-color:"+colorString);
        flowPane.setVgap(5);
        flowPane.setHgap(5);

//        this.setStyle("-fx-background:"+colorString);
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
                    UIController.use().setSelectedTags(null);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        ObservableList<String> options = FXCollections.observableArrayList();
        options.add(SearchParam.INCLUDE_BOOKMARKS_WITH_ALL_TAGS);
        options.add(SearchParam.INCLUDE_BOOKMARKS_WITH_ANY_TAGS);
        options.add(SearchParam.INCLUDE_BOOKMARKS_WITHOUT_TAGS);

        final ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.setStyle("-fx-background-radius:15");
        comboBox.getSelectionModel().select(SearchParam.INCLUDE_BOOKMARKS_WITH_ALL_TAGS);
        comboBox.getSelectionModel().selectedItemProperty().addListener((options1, oldValue, newValue) -> {
            try
            {
                UIController.use().setTagMode(newValue);
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

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(selectedTags);

        Collections.sort(tagsList, new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                if (o1==null || o1.isEmpty())
                {
                    if (o2==null || o2.isEmpty())
                    {
                        return 0;
                    }
                    else {
                        return -1;
                    }
                }
                else if (o2==null || o2.isEmpty())
                {
                    if (o1.isEmpty())
                    {
                        return 0;
                    }
                    else
                    {
                        return 1;
                    }
                }
                else {
                    return o1.compareToIgnoreCase(o2);
                }

            }
        });


        for (final String string: tagsList)
        {
            Pane pane = new Pane();
            String tmp = string.toLowerCase();

            pane.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    try
                    {
                        UIController.use().removeSelectedTag(string);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });
            Label label = new Label(string);
            label.setStyle("-fx-border-color: black");
            if (currentSearchTerm!=null && !currentSearchTerm.isEmpty() &&  (tmp.contains(currentSearchTerm) || currentSearchTerm.contains(tmp)))
            {
                pane.setStyle("-fx-background-color: lightgreen");
                isFound = true;
            }
            else
            {
                pane.setStyle("-fx-background-color: mintcream");
            }
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
        return isFound;
    }

}
