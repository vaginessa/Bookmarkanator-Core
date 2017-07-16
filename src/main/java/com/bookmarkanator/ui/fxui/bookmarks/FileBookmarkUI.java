package com.bookmarkanator.ui.fxui.bookmarks;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.*;
import com.bookmarkanator.util.*;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class FileBookmarkUI extends AbstractUIBookmark {

    private File selectedfile;

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

        if (stage!=null)
        {
            stage.close();
        }

        return showView(this.getBookmark());
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
        return showView(null);
    }

    @Override
    public String getRequiredBookmarkClassName()
    {
        return FileBookmark.class.getCanonicalName();
    }

    private AbstractBookmark showView(AbstractBookmark bookmark)
        throws Exception
    {

        //TODO Only enable submit button when the web address is a real web address of some kind.
        Dialog<String> dialog = new Dialog<>();
        if (bookmark != null)
        {
            dialog.setTitle("Edit File Bookmark");
        }
        else
        {
            dialog.setTitle("New File Bookmark");
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

        Label locationLabel = new Label("(nothing selected yet)");
        Label fileLabel = new Label("File");
        //Text area
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select a file");
        if (bookmark != null) {
            File file = new File(bookmark.getContent());
            fileChooser.setInitialDirectory(file);
        }

        vBox.getChildren().add(fileLabel);

        Button fileChooserButton = new Button("Select a file");

        fileChooserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedfile = fileChooser.showOpenDialog(null);
                try
                {
                    locationLabel.setText(Util.compressString(selectedfile.getCanonicalPath(), 60));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Please select a folder");

        if (bookmark != null) {
            File file = new File(bookmark.getContent());
            directoryChooser.setInitialDirectory(file);
            locationLabel.setText(file.getCanonicalPath());
        }

        Button folderChooserButton = new Button("Select a folder");

        folderChooserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedfile = directoryChooser.showDialog(null);
                try
                {
                    locationLabel.setText(Util.compressString(selectedfile.getCanonicalPath(), 60));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        HBox fileButtonsBox = new HBox();
        fileButtonsBox.getChildren().addAll(fileChooserButton, folderChooserButton);
        vbox.getChildren().addAll(fileButtonsBox, locationLabel);

        //Tag selection panel
        TagPanel tagPanel;

        if (bookmark != null) {
            tagPanel = TagPanel.getNew(bookmark.getTags());
        } else {
            tagPanel = TagPanel.getNew(null);
        }
        tagPanel.setPrefWidth(400);
        tagPanel.setPrefHeight(400);

        container.getChildren().add(vbox);
        container.getChildren().add(tagPanel);

        dialog.getDialogPane().setContent(container);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    return selectedfile.getCanonicalPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (dialogButton == delete) {
                try {
                    Bootstrap.context().delete(this.getBookmark().getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        Optional<String> res = dialog.showAndWait();

        if (res.isPresent() && res.get() != null) {
            AbstractBookmark abstractBookmark;
            if (bookmark == null) {
                abstractBookmark = new FileBookmark();
                abstractBookmark.setName(name.getText());
                abstractBookmark.setContent(selectedfile.getCanonicalPath());
                abstractBookmark.setTags(tagPanel.getSelectedTags());
            } else {
                bookmark.setName(name.getText());
                bookmark.setContent(selectedfile.getCanonicalPath());
                bookmark.setTags(tagPanel.getSelectedTags());
                abstractBookmark = bookmark;
            }

            return abstractBookmark;
        }
        return null;
    }
}
