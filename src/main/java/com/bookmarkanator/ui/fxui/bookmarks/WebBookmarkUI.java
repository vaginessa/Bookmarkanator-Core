package com.bookmarkanator.ui.fxui.bookmarks;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class WebBookmarkUI extends AbstractUIBookmark
{
    @Override
    public Image getTypeIcon()
    {
        return null;
    }

    @Override
    public void show()
        throws Exception
    {
        this.getBookmark().runAction();
    }

    @Override
    public AbstractBookmark edit()
        throws Exception
    {
        Stage stage = openStagesMap.get(this.getBookmark().getId());

        if (stage != null)
        {
            stage.close();
        }

        return showWebBookmarkView(this.getBookmark());
    }

    @Override
    public void delete()
        throws Exception
    {
        Stage stage = openStagesMap.get(this.getBookmark().getId());
        if (stage != null)
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
        return showWebBookmarkView(null);
    }

    @Override
    public String getRequiredBookmarkClassName()
    {
        return WebBookmark.class.getCanonicalName();
    }

    private AbstractBookmark showWebBookmarkView(AbstractBookmark bookmark)
        throws Exception
    {

        //TODO Only enable submit button when the web address is a real web address of some kind.
        Dialog<String> dialog = new Dialog<>();
        if (bookmark != null)
        {
            dialog.setTitle("Edit Web Bookmark");
        }
        else
        {
            dialog.setTitle("New Web Bookmark");
        }

        // Set the button types.
        ButtonType delete = new ButtonType("Delete", ButtonBar.ButtonData.APPLY);

        if (bookmark == null)
        {
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        }
        else
        {
            addDeleteButton(dialog, delete);
        }

        VBox container = new VBox();
        container.setSpacing(10);
        Pane vbox = new VBox();

        //Create name panel
        Pane vBox = new VBox();
        Label label = new Label("Name");
        TextField name = new TextField();
        if (bookmark != null)
        {
            name.setText(bookmark.getName());
        }

        HBox.setMargin(label, new Insets(5, 10, 0, 0));
        HBox.setMargin(name, new Insets(0, 2, 10, 0));
        HBox.setHgrow(name, Priority.ALWAYS);
        vBox.getChildren().addAll(label, name);
        vbox.getChildren().add(vBox);
        Platform.runLater(() -> name.requestFocus());

        Label webAddressLabel = new Label("Web Address");
        //Text area
        TextField webAddress = new TextField();
        if (bookmark != null)
        {
            webAddress.setText(bookmark.getContent());
        }
        vBox.getChildren().add(webAddressLabel);
        vbox.getChildren().add(webAddress);

        //Tag selection panel
        TagPanel tagPanel;

        if (bookmark != null)
        {
            tagPanel = TagPanel.getNew(bookmark.getTags());
        }
        else
        {
            tagPanel = TagPanel.getNew(null);
        }
        tagPanel.setPrefWidth(400);
        tagPanel.setPrefHeight(400);

        container.getChildren().add(vbox);
        container.getChildren().add(tagPanel);

        dialog.getDialogPane().setContent(container);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == ButtonType.OK)
            {
                return webAddress.getText();
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
            if (bookmark == null)
            {
                abstractBookmark = new WebBookmark();
                abstractBookmark.setName(name.getText());
                abstractBookmark.setContent(webAddress.getText());
                abstractBookmark.setTags(tagPanel.getSelectedTags());
            }
            else
            {
                bookmark.setName(name.getText());
                bookmark.setContent(webAddress.getText());
                bookmark.setTags(tagPanel.getSelectedTags());
                abstractBookmark = bookmark;
            }

            return abstractBookmark;
        }
        return null;
    }
}
