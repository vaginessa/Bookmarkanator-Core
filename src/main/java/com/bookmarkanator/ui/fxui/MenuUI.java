package com.bookmarkanator.ui.fxui;

import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;

public class MenuUI implements MenuInterface
{
    private GUIControllerInterface guiController;
    private boolean editMode = false;

    @Override
    public void setGUIController(GUIControllerInterface guiController)
    {
        this.guiController = guiController;
    }

    @Override
    public boolean getEditMode()
    {
        return editMode;
    }

    @Override
    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }

    @Override
    public GUIControllerInterface getGUIController()
    {
        return this.guiController;
    }

    public MenuBar getMenuBar()
{
    javafx.scene.control.MenuBar menuBar = new MenuBar();

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

        menuItem = new MenuItem("Enter Edit Mode");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Exit Edit Mode");
        menuFile.getItems().addAll(menuItem);

        menuItem = new SeparatorMenuItem();
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Tag Editor");
        menuItem.setDisable(true);
        menuFile.getItems().addAll(menuItem);

        menuItem = new SeparatorMenuItem();
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Settings");
        menuFile.getItems().addAll(menuItem);
        //        menuItem = new MenuItem("Search");
        //        menuFile.getItems().addAll(menuItem);

        return menuFile;
    }

    private Menu getViewMenu()
    {
        Menu menuFile = new Menu("View");

        MenuItem menuItem = new MenuItem("Quick Panel Settings");
        menuFile.getItems().addAll(menuItem);

        //        menuItem = new MenuItem("Hide Search Panel");
        //        menuFile.getItems().addAll(menuItem);

        return menuFile;
    }

    private Menu getHelpMenu()
    {
        Menu menuFile = new Menu("Help");

        MenuItem menuItem = new MenuItem("About");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Tutorial");
        menuFile.getItems().addAll(menuItem);

        menuItem = new MenuItem("Technical Documentation");
        menuFile.getItems().addAll(menuItem);

        return menuFile;
    }
}
