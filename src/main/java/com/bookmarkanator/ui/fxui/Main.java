package com.bookmarkanator.ui.fxui;

import java.awt.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main extends Application
{
    public static final String UI_PREFIX_VALUE = Main.class.getCanonicalName();
    public static final String UI_CLASS_VALUE = "class";
    public static final String UI_STRING_SEPARATOR = "-";

    private UIControllerInterface controller;
    private Dimension bestWindowSize;

    //TODO: Add a drag and drop handler so that each bookmark must handle types dragged and dropped into it.

    @Override
    public void start(Stage primaryStage)
        throws Exception
    {
        Bootstrap.use().getSettings().importSettings(this.getDefaultSettings());
        controller = new UIController();

        VBox vBox = new VBox();
        vBox.setFillWidth(true);
        bestWindowSize = UIUtil.use().getBestWindowSizeMap().get(0);
        Scene scene = new Scene(vBox, bestWindowSize.getWidth(), bestWindowSize.getHeight(), javafx.scene.paint.Paint.valueOf("white"));

        //Set up menu
        MenuUI menuUI = new MenuUI(controller);
        controller.setMenuUi(menuUI);
        MenuBar menuBar = menuUI.getMenuBar();
        vBox.getChildren().addAll(menuBar);

        SearchUI searchUI = new SearchUI(controller);
        controller.setSearchUI(searchUI);
        searchUI.setPrefHeight(bestWindowSize.getHeight() * .15);
        vBox.getChildren().add(searchUI);

        HBox hBox = new HBox();
        hBox.setFillHeight(true);

        TypesUI typesUI = new TypesUI(controller);
        controller.setTypesUI(typesUI);
        hBox.getChildren().add(typesUI);
        typesUI.setPrefWidth(bestWindowSize.getWidth() * .15);

        //Setup tags panels
        VBox tagsBox = new VBox();
        SelectedTagsUI selectedTagsUI = new SelectedTagsUI(controller);
        selectedTagsUI.setPrefHeight(bestWindowSize.getHeight() * .35);
        //TODO CHANGE the selected tags UI to a different kind of pane, and add a scroll pane that will contain the tag groups so only they scroll.
        selectedTagsUI.setPrefWidth(bestWindowSize.getWidth()*.4);
        controller.setSelectedTagsUI(selectedTagsUI);
        tagsBox.getChildren().add(selectedTagsUI);
        VBox.setVgrow(selectedTagsUI, Priority.ALWAYS);

        AvailableTagsUI availableTagsUI = new AvailableTagsUI(controller);
        availableTagsUI.setPrefHeight(bestWindowSize.getHeight() * .4);
        controller.setAvailableTagsUI(availableTagsUI);
        tagsBox.getChildren().add(availableTagsUI);
        VBox.setVgrow(availableTagsUI, Priority.ALWAYS);

        hBox.getChildren().add(tagsBox);
        HBox.setHgrow(tagsBox, Priority.ALWAYS);

        BookmarksListUI bookmarksListUI = new BookmarksListUI(controller);
        controller.setBookmarksListUI(bookmarksListUI);
        hBox.getChildren().add(bookmarksListUI);
        HBox.setHgrow(bookmarksListUI, Priority.ALWAYS);

        vBox.getChildren().add(hBox);
        VBox.setVgrow(hBox, Priority.ALWAYS);

        NewBookmarkSelectorUI newBookmarkSelectorUI = new NewBookmarkSelectorUI(controller);
        controller.setNewBookmarkSelectorUI(newBookmarkSelectorUI);

        primaryStage.setScene(scene);

        primaryStage.setMinHeight(bestWindowSize.getHeight());
        primaryStage.setMinWidth(bestWindowSize.getWidth());

        controller.initUI();
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            public void handle(WindowEvent we)
            {
                try
                {
                    Bootstrap.use().saveSettingsFile();
                    Bootstrap.IOInterface().save();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }

//    public Dimension getBestWindowSize()
//    {
//        if (bestWindowSize==null)
//        {
//            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//            int width = gd.getDisplayMode().getWidth();
//            int height = gd.getDisplayMode().getHeight();
//
//            bestWindowSize = new Dimension((int) (width * .65), (int) (height * .5));
//        }
//       return bestWindowSize;
//    }

    private Settings getDefaultSettings()
        throws Exception
    {
        Settings settings = new Settings();

        SettingItem item = new SettingItem(Main.getUIClassString() + EncryptedBookmark.class.getCanonicalName());
        item.setValue(EncryptedBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        item = new SettingItem(Main.getUIClassString() + FileBookmark.class.getCanonicalName());
        item.setValue(FileBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        item = new SettingItem(Main.getUIClassString() + SequenceBookmark.class.getCanonicalName());
        item.setValue(SequenceBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        item = new SettingItem(Main.getUIClassString() + TerminalBookmark.class.getCanonicalName());
        item.setValue(TerminalBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        item = new SettingItem(Main.getUIClassString() + TextBookmark.class.getCanonicalName());
        item.setValue(TextBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        item = new SettingItem(Main.getUIClassString() + WebBookmark.class.getCanonicalName());
        item.setValue(WebBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        return settings;
    }

    public static String getUIClassString()
    {
        return Main.UI_PREFIX_VALUE + Main.UI_STRING_SEPARATOR + Main.UI_CLASS_VALUE + Main.UI_STRING_SEPARATOR;
    }

}
