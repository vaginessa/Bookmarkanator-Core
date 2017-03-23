package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class AvailableTagsUI extends ScrollPane implements AvailableTagsInterface
{
    private boolean editMode = false;
    private FlowPane flowPane;
    private String colorString =  "#8fbc8f";
    private String currentSearchTerm;
    private boolean isFound;

    public AvailableTagsUI()
    {
        this.flowPane = new FlowPane();
//        flowPane.setStyle("-fx-background-color:"+colorString);
        flowPane.setVgap(5);
        flowPane.setHgap(5);

//        this.setStyle("-fx-background:"+colorString);
        this.setFitToWidth(true);
        this.setContent(flowPane);
    }

    @Override
    public void setAvailableTags(Set<String> availableTags)
    {
        this.flowPane.getChildren().clear();

        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(availableTags);

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
                        UIController.use().addSelectedTag(string);
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
                if (currentSearchTerm.equals(tmp))
                {
                    label.setStyle("-fx-border-color: orange;-fx-border-width: 2px");
                }
//                else
//                {
                    pane.setStyle("-fx-background-color: lightgreen");
//                }

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
    public Set getAvailableTags()
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
