package com.bookmarkanator.ui.fxui;

import java.awt.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main extends Application
{
    public static Main main;
    public static final String UI_PREFIX_VALUE = Main.class.getCanonicalName();
    public static final String UI_CLASS_VALUE = "class";
    public static final String UI_STRING_SEPARATOR = "-";

    private UIController uiController;
    private Dimension bestWindowSize;
    //TODO: Add a drag and drop handler so that each bookmark must handle types dragged and dropped into it.
    //TODO: Add a one way backup or a versioned backup scheme for bookmarks.

    @Override
    public void start(Stage primaryStage)
        throws Exception
    {
        //Setting global reference to this here because the JavaFX thread must create this object.
        main = this;

        bestWindowSize = getBestWindowSize();
        Bootstrap.use().getSettings().importSettings(this.getDefaultSettings());

        uiController = UIController.use();

        VBox vBox = new VBox();
        vBox.setFillWidth(true);

        Scene scene = new Scene(vBox, bestWindowSize.getWidth(), bestWindowSize.getHeight(), javafx.scene.paint.Paint.valueOf("white"));

        //Set up menu
        MenuUI menuUI = new MenuUI();
        uiController.setMenuUi(menuUI);
        MenuBar menuBar = menuUI.getMenuBar();
        vBox.getChildren().addAll(menuBar);

        SearchUI searchUI = new SearchUI();
        uiController.setSearchUI(searchUI);
        searchUI.setPrefHeight(bestWindowSize.getHeight() * .15);
        vBox.getChildren().add(searchUI);

        HBox hBox = new HBox();
        hBox.setFillHeight(true);

        TypesUI typesUI = new TypesUI();
        uiController.setTypesUI(typesUI);
        hBox.getChildren().add(typesUI);
        typesUI.setPrefWidth(bestWindowSize.getWidth() * .15);

        //Setup tags panels
        VBox tagsBox = new VBox();
        SelectedTagsUI selectedTagsUI = new SelectedTagsUI();
        selectedTagsUI.setPrefHeight(bestWindowSize.getHeight() * .35);
        uiController.setSelectedTagsUI(selectedTagsUI);
        tagsBox.getChildren().add(selectedTagsUI);
        VBox.setVgrow(selectedTagsUI, Priority.ALWAYS);

        AvailableTagsUI availableTagsUI = new AvailableTagsUI();
        availableTagsUI.setPrefHeight(bestWindowSize.getHeight() * .4);
        uiController.setAvailableTagsUI(availableTagsUI);
        tagsBox.getChildren().add(availableTagsUI);
        VBox.setVgrow(availableTagsUI, Priority.ALWAYS);

        hBox.getChildren().add(tagsBox);
        HBox.setHgrow(tagsBox, Priority.ALWAYS);

        BookmarksListUI bookmarksListUI = new BookmarksListUI();
        uiController.setBookmarksListUI(bookmarksListUI);
        hBox.getChildren().add(bookmarksListUI);
        HBox.setHgrow(bookmarksListUI, Priority.ALWAYS);

        vBox.getChildren().add(hBox);
        VBox.setVgrow(hBox, Priority.ALWAYS);

        NewBookmarkSelectorUI newBookmarkSelectorUI = new NewBookmarkSelectorUI(uiController);
        uiController.setNewBookmarkSelectorUI(newBookmarkSelectorUI);

        primaryStage.setScene(scene);

        primaryStage.setMinHeight(bestWindowSize.getHeight());
        primaryStage.setMinWidth(bestWindowSize.getWidth());

        uiController.initUI();
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

    public Dimension getBestWindowSize()
    {
        if (bestWindowSize==null)
        {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = gd.getDisplayMode().getWidth();
            int height = gd.getDisplayMode().getHeight();

            bestWindowSize = new Dimension((int) (width * .65), (int) (height * .5));
        }
       return bestWindowSize;
    }

    private Settings getDefaultSettings()
        throws Exception
    {
        Settings settings = new Settings();

        SettingItem item = new SettingItem(Main.getUIClassString() + BashHistoryBookmark.class.getCanonicalName());
        item.setValue(BashHistoryBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        item = new SettingItem(Main.getUIClassString() + EncryptedBookmark.class.getCanonicalName());
        item.setValue(EncryptedBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        item = new SettingItem(Main.getUIClassString() + FileBookmark.class.getCanonicalName());
        item.setValue(FileBookmarkUI.class.getCanonicalName());
        item.setType("mainUI");
        settings.putSetting(item);

        item = new SettingItem(Main.getUIClassString() + ReminderBookmark.class.getCanonicalName());
        item.setValue(ReminderBookmarkUI.class.getCanonicalName());
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

    public static Main use()
    {
        return main;
    }
}
