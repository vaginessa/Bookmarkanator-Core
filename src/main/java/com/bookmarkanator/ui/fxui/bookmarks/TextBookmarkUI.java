package com.bookmarkanator.ui.fxui.bookmarks;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class TextBookmarkUI extends AbstractUIBookmark
{
    public TextBookmarkUI()
    {
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
        this.getBookmark().action();
    }

    @Override
    public AbstractBookmark edit()
        throws Exception
    {
        return null;
    }

    @Override
    public void delete()
        throws Exception
    {

    }

    @Override
    public AbstractBookmark newBookmarkView()
        throws Exception
    {
        System.out.println(this.getClass().getCanonicalName());

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New Text Bookmark");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        Pane vbox = new VBox();

        TextField name = new TextField();
        name.setPromptText("Bookmark Name");
        vbox.getChildren().add(name);
        Platform.runLater(() -> name.requestFocus());

        TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(30);
        textArea.setPrefRowCount(15);
        vbox.getChildren().add(textArea);

        dialog.getDialogPane().setContent(vbox);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> res = dialog.showAndWait();

        if (res.isPresent() && res.get()!=null)
        {
            AbstractBookmark abstractBookmark = new TextBookmark();
            abstractBookmark.setName(name.getText());
            abstractBookmark.setText(textArea.getText());
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
