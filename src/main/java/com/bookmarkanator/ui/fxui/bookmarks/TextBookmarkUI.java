package com.bookmarkanator.ui.fxui.bookmarks;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.fxui.*;
import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;
import javafx.stage.*;

public class TextBookmarkUI extends AbstractUIBookmark
{
    private static Map<UUID, Stage> openStagesMap;//<Bookmark id, Stage that is showing it>

    public TextBookmarkUI()
    {
        if (openStagesMap == null)
        {
            openStagesMap = new HashMap<>();
        }
    }

    public TextBookmarkUI(ContextInterface context)
    {
        super(context);
    }

    @Override
    public Image getTypeIcon()
    {
        return null;
    }

    @Override
    public void show()
        throws Exception
    {
        Stage stage = openStagesMap.get(this.getBookmark().getId());

        if (stage==null)
        {

            Pane vbox = new VBox();

            HTMLEditor textArea = new HTMLEditor();
            textArea.setPrefWidth(500);
            textArea.setPrefHeight(300);
            vbox.getChildren().add(textArea);
            Platform.runLater(textArea::requestFocus);

            Scene dialog = new Scene(vbox);
            stage = new Stage();

            //Add this stage to the stages map so that it will not open multiple windows per bookmark.
            openStagesMap.put(getBookmark().getId(), stage);

            stage.setScene(dialog);

            this.getBookmark().runAction();
            textArea.setHtmlText(this.getBookmark().getText());

            stage.setOnCloseRequest(new EventHandler<WindowEvent>()
            {
                public void handle(WindowEvent we)
                {
                    getBookmark().setText(textArea.getHtmlText());
                }
            });
        }

        stage.show();
        stage.toFront();
    }

    @Override
    public AbstractBookmark edit()
        throws Exception
    {
        Stage stage = openStagesMap.get(this.getBookmark().getId());

        if (stage!=null)
        {
            stage.close();
        }

        return showBookmarkView(this.getBookmark());
    }

    @Override
    public void delete()
        throws Exception
    {
        Stage stage = openStagesMap.get(this.getBookmark().getId());
        if (stage!=null)
        {
            stage.close();
        }

        Bootstrap.context().delete(this.getBookmark().getId());
        UIController.use().updateUI();
    }

    @Override
    public AbstractBookmark newBookmarkView()
        throws Exception
    {
        return showBookmarkView(null);
    }

    private AbstractBookmark showBookmarkView(AbstractBookmark bookmark)
        throws Exception
    {
        Dialog<String> dialog = new Dialog<>();
        if (bookmark!=null)
        {
            dialog.setTitle("Edit Text Bookmark");
        }
        else
        {
            dialog.setTitle("New Text Bookmark");
        }

        // Set the button types.
        ButtonType delete = new ButtonType("Delete",ButtonBar.ButtonData.APPLY);


        if (bookmark==null)
        {
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        }
        else
        {
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, delete, ButtonType.CANCEL);
            final Button tmp =  (Button)dialog.getDialogPane().lookupButton(delete);
            tmp.setStyle("-fx-background-color:red");
            tmp.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("This bookmark will be deleted.");
                    alert.setContentText("Continue?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() != ButtonType.OK){
                        event.consume();
                    }
                }
            );
        }

        HBox container = new HBox();
        container.setSpacing(10);
        Pane vbox = new VBox();

        //Create name panel
        Pane hbox = new HBox();
        Label label = new Label("Name");
        TextField name = new TextField();
        if (bookmark!=null)
        {
            name.setText(bookmark.getName());
        }

        HBox.setMargin(label, new Insets(5, 10, 0, 0));
        HBox.setMargin(name, new Insets(0, 2, 10, 0));
        HBox.setHgrow(name, Priority.ALWAYS);
        hbox.getChildren().addAll(label, name);
        vbox.getChildren().add(hbox);
        Platform.runLater(() -> name.requestFocus());

        //Text area
        HTMLEditor textArea = new HTMLEditor();
        textArea.setPrefWidth(600);
        textArea.setPrefHeight(400);
        if (bookmark!=null)
        {
            textArea.setHtmlText(bookmark.getText());
        }

        vbox.getChildren().add(textArea);

        //Tag selection panel
        TagPanel tagPanel;

        if (bookmark!=null)
        {
            tagPanel = TagPanel.getNew(bookmark.getTags());
        }
        else
        {
            tagPanel = TagPanel.getNew(null);
        }
        tagPanel.setPrefWidth(400);
        tagPanel.setPrefHeight(400);

        container.getChildren().add(tagPanel);
        container.getChildren().add(vbox);

        dialog.getDialogPane().setContent(container);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
            {
                return textArea.getHtmlText();
            }
            else if (dialogButton == delete)
            {
                try
                {
                    Bootstrap.context().delete(this.getBookmark().getId());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        Optional<String> res = dialog.showAndWait();

        if (res.isPresent() && res.get() != null)
        {
            AbstractBookmark abstractBookmark;
            if (bookmark==null)
            {
                abstractBookmark = new TextBookmark();
                abstractBookmark.setName(name.getText());
                abstractBookmark.setText(textArea.getHtmlText());
                abstractBookmark.setTags(tagPanel.getSelectedTags());
            }
            else
            {
                bookmark.setName(name.getText());
                bookmark.setText(textArea.getHtmlText());
                bookmark.setTags(tagPanel.getSelectedTags());
                abstractBookmark = bookmark;
            }

            return abstractBookmark;
        }
        return null;
    }

    @Override
    public String getRequiredBookmarkClassName()
    {
        return TextBookmark.class.getCanonicalName();
    }
}
