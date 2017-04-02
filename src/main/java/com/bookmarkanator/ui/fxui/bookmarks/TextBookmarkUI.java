package com.bookmarkanator.ui.fxui.bookmarks;

import java.awt.*;
import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.*;
import com.bookmarkanator.ui.fxui.components.*;
import javafx.application.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.web.*;
import javafx.stage.*;

public class TextBookmarkUI extends AbstractUIBookmark
{
    private TextField name;
    private HTMLEditor textArea;

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

            Pane vbox = getbookmarkView(this.getBookmark());
            Platform.runLater(() -> textArea.requestFocus());
            Dimension bestWindowSize = UIUtil.use().getBestWindowSizeMap().get(0);
            Scene dialog = new Scene(vbox,bestWindowSize.getWidth()*.6, bestWindowSize.getHeight()*.75, javafx.scene.paint.Paint.valueOf("white"));
            stage = new Stage();

            //Add this stage to the stages map so that it will not open multiple windows per bookmark.
            openStagesMap.put(getBookmark().getId(), stage);

            stage.setScene(dialog);

            this.getBookmark().runAction();
            textArea.setHtmlText(this.getBookmark().getText());
            name.setText(this.getBookmark().getName());

            stage.setOnCloseRequest(new EventHandler<WindowEvent>()
            {
                public void handle(WindowEvent we)
                {
                    try {
                        getBookmark().setText(textArea.getHtmlText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getBookmark().setName(name.getText());
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

        return showTextBookmarkView(this.getBookmark());
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
        controller.updateUI();
    }

    @Override
    public AbstractBookmark newBookmarkView()
        throws Exception
    {
        return showTextBookmarkView(null);
    }

    private AbstractBookmark showTextBookmarkView(AbstractBookmark bookmark)
        throws Exception
    {
        Dialog<String> dialog = new Dialog<>();
        if (bookmark!=null)
        {
            dialog.setTitle("Edit Web Bookmark");
        }
        else
        {
            dialog.setTitle("New Web Bookmark");
        }

        // Set the button types.
        ButtonType delete = new ButtonType("Delete",ButtonBar.ButtonData.APPLY);

        if (bookmark==null)
        {
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        }
        else
        {
            addDeleteButton(dialog, delete);
        }

        HBox container = new HBox();
        container.setSpacing(10);
        Pane vbox = getbookmarkView(bookmark);

        //Tag selection panel
        TagPanel tagPanel = getTagPanel(bookmark);

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

    private Pane getbookmarkView(AbstractBookmark bookmark)
    {
        VBox vBox = new VBox();
        NameBoxPanel nameBoxPanel;

        if (bookmark!=null)
        {
            nameBoxPanel = new NameBoxPanel(bookmark.getName());
        }
        else
        {
            nameBoxPanel = new NameBoxPanel();
        }

        this.name = nameBoxPanel.getName();

        HTMLEditor textArea = getTextArea(bookmark);

        vBox.getChildren().add(nameBoxPanel);
        vBox.getChildren().add(textArea);

        VBox.setMargin(textArea, new Insets(5));
        VBox.setVgrow(textArea, Priority.ALWAYS);

        return vBox;
    }

    private TagPanel getTagPanel(AbstractBookmark bookmark)
        throws Exception
    {
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

        return tagPanel;
    }



    private HTMLEditor getTextArea(AbstractBookmark bookmark)
    {
        //Text area
        textArea = new HTMLEditor();
        textArea.setPrefWidth(600);
        textArea.setPrefHeight(400);
        if (bookmark!=null)
        {
            textArea.setHtmlText(bookmark.getText());
        }
        return textArea;
    }

    @Override
    public String getRequiredBookmarkClassName()
    {
        return TextBookmark.class.getCanonicalName();
    }
}
