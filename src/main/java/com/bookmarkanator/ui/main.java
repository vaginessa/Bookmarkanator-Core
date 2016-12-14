package com.bookmarkanator.ui;

import java.awt.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class Main extends Application
{
    private UIController uiController;

    @Override
    public void start(Stage primaryStage)
        throws Exception
    {
        Dimension bestWindowSize = getBestWindowSize();

        uiController = new UIController();
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: steelblue");
        gridPane.setGridLinesVisible(true);

        VBox vBox = new VBox();
        vBox.setFillWidth(true);

        Scene scene = new Scene(vBox, bestWindowSize.getWidth(), bestWindowSize.getHeight(), Color.RED);

        MenuBar menuBar = getMenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        vBox.getChildren().addAll(menuBar);

        vBox.getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        MenuPanel menuPanel = new MenuPanel();
        menuPanel.setPrefHeight(bestWindowSize.getHeight()*.15);
        gridPane.add(menuPanel, 0, 0, 5, 1);

        TypesPanel typesPanel = new TypesPanel();
        gridPane.add(typesPanel, 0, 1, 1, 4);
        typesPanel.setPrefWidth(bestWindowSize.getWidth()*.15);

        SelectedPanel selectedPanel = new SelectedPanel();
        gridPane.add(selectedPanel, 1, 1, 2, 2);

        AvailablePanel availablePanel = new AvailablePanel();
        gridPane.add(availablePanel, 1, 3, 2, 2);

        BookmarksPanel bookmarksPanel = new BookmarksPanel();
//        bookmarksPanel.setPrefWidth(bestWindowSize.getWidth()*.5);
        gridPane.add(bookmarksPanel, 3, 1, 2, 4);

        setRowConstraints(gridPane);
        setColConstraints(gridPane);

        uiController.setMenuPanel(menuPanel);
        uiController.setTypesPanel(typesPanel);
        uiController.setSelectedPanel(selectedPanel);
        uiController.setAvailablePanel(availablePanel);
        uiController.setBookmarksPanel(bookmarksPanel);

        primaryStage.setScene(scene);

        primaryStage.setMinHeight(bestWindowSize.getHeight());
        primaryStage.setMinWidth(bestWindowSize.getWidth());

        primaryStage.show();
    }

    private MenuBar getMenuBar()
    {
        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(getFileMenu(), getEditMenu(), getViewMenu(), getHelpMenu());
        return menuBar;
    }
    private Menu getFileMenu()
    {
        Menu menuFile = new Menu("File");

        MenuItem menuItem = new MenuItem("New Bookmark");
        menuFile.getItems().addAll(menuItem);

        menuItem = new SeparatorMenuItem();
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Import Bookmarks");
        menuItem.setDisable(true);
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Export Bookmarks");
        menuItem.setDisable(true);
        menuFile.getItems().addAll(menuItem);

        menuItem = new SeparatorMenuItem();
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Exit");
        menuFile.getItems().addAll(menuItem);

        return menuFile;
    }

    private Menu getEditMenu()
    {
        Menu menuFile = new Menu("Edit");

        MenuItem menuItem = new MenuItem("Undo");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Redo");
        menuFile.getItems().addAll(menuItem);

        menuItem = new SeparatorMenuItem();
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Tag Editor");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Settings");
        menuFile.getItems().addAll(menuItem);

        menuItem = new SeparatorMenuItem();
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Enter Edit Mode");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Exit Edit Mode");
        menuFile.getItems().addAll(menuItem);

        menuItem = new SeparatorMenuItem();
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Search");
        menuFile.getItems().addAll(menuItem);

        return menuFile;
    }

    private Menu getViewMenu()
    {
        Menu menuFile = new Menu("View");

        MenuItem menuItem = new MenuItem("Quick Panel Settings");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Hide Search Panel");
        menuFile.getItems().addAll(menuItem);

        return menuFile;
    }

    private Menu getHelpMenu()
    {
        Menu menuFile = new Menu("Help");

        MenuItem menuItem = new MenuItem("About");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Wizard");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Documentation");
        menuFile.getItems().addAll(menuItem);

        return menuFile;
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
