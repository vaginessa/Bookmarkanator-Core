package com.bookmarkanator.ui.fxui;

import java.awt.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.ui.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main extends Application
{
    private GUIController guiController;
    private ContextInterface context;

    @Override
    public void start(Stage primaryStage)
        throws Exception
    {
        Dimension bestWindowSize = getBestWindowSize();
        Bootstrap bootstrap = new Bootstrap();
        guiController = new GUIController(bootstrap);

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: steelblue");
        gridPane.setGridLinesVisible(true);

        VBox vBox = new VBox();
        vBox.setFillWidth(true);

        Scene scene = new Scene(vBox, bestWindowSize.getWidth(), bestWindowSize.getHeight(), javafx.scene.paint.Paint.valueOf("RED"));

        MenuUI menuUI = new MenuUI();
        guiController.setMenuUi(menuUI);

        MenuBar menuBar = menuUI.getMenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        vBox.getChildren().addAll(menuBar);
//
        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        SearchUI searchUI = new SearchUI();
        guiController.setSearchUI(searchUI);
        searchUI.setPrefHeight(bestWindowSize.getHeight()*.15);
        gridPane.add(searchUI, 0, 0, 5, 1);

        TypesUI typesUI = new TypesUI();
        guiController.setTypesUI(typesUI);
        gridPane.add(typesUI, 0, 1, 1, 4);
        typesUI.setPrefWidth(bestWindowSize.getWidth()*.15);

        SelectedTagsUI selectedTagsUI = new SelectedTagsUI();
        guiController.setSelectedTagsUI(selectedTagsUI);
        gridPane.add(selectedTagsUI, 1, 1, 2, 2);

        AvailableTagsUI availableTagsUI = new AvailableTagsUI();
        guiController.setAvailableTagsUI(availableTagsUI);

        gridPane.add(availableTagsUI, 1, 3, 2, 2);

        BookmarksListUI bookmarksListUI = new BookmarksListUI();
        guiController.setBookmarksListUI(bookmarksListUI);

        bookmarksListUI.setPrefWidth(bestWindowSize.getWidth()*.5);
        gridPane.add(bookmarksListUI, 3, 1, 2, 4);

        setRowConstraints(gridPane);
        setColConstraints(gridPane);
        primaryStage.setScene(scene);

        primaryStage.setMinHeight(bestWindowSize.getHeight());
        primaryStage.setMinWidth(bestWindowSize.getWidth());

        primaryStage.show();
    }

    private void setRowConstraints(GridPane gridPane)
    {
        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.NEVER);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);

        RowConstraints row3 = new RowConstraints();
        row3.setVgrow(Priority.ALWAYS);

        RowConstraints row4 = new RowConstraints();
        row4.setVgrow(Priority.ALWAYS);

        RowConstraints row5 = new RowConstraints();
        row5.setVgrow(Priority.ALWAYS);
        gridPane.getRowConstraints().addAll(row1, row2, row3, row4, row5);
    }

    private void setColConstraints(GridPane gridPane)
    {
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col4 = new ColumnConstraints();
        col4.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col5 = new ColumnConstraints();
        col5.setHgrow(Priority.SOMETIMES);

        gridPane.getColumnConstraints().addAll(col1, col2, col3, col4, col5);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private Dimension getBestWindowSize()
    {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        Dimension d = new Dimension((int) (width * .5), (int) (height * .5));
        return d;
    }

}
