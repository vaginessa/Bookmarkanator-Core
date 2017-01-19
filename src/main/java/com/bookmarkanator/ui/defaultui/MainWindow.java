package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.core.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

public class MainWindow
{
    private GUIController guiController;

    public MainWindow()
        throws Exception
    {
        init();
    }

    public void init()
        throws Exception
    {
        Bootstrap bootstrap = new Bootstrap();
        guiController = new GUIController();
        guiController.setBootstrap(bootstrap);

        Terminal terminal = new DefaultTerminalFactory().createTerminal();

        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        final MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));

        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutManager(new GridLayout(1));

        //File Menu Panel
        MenuPanel menuPanel = new MenuPanel(gui);
        menuPanel.setGUIController(this.guiController);
        panel.addComponent(menuPanel.getPanel().withBorder(Borders.singleLine()));

        //Search Panel
        SearchPanel searchPanel = new SearchPanel(gui);
        panel.addComponent(searchPanel.getPanel().withBorder(Borders.singleLine()));


        Panel tempPan = new Panel(new LinearLayout(Direction.HORIZONTAL));
        //Types Panel
        BKTypesPanel types = new BKTypesPanel(gui);
        tempPan.addComponent(types.getPanel().withBorder(Borders.singleLine()));

        Panel tagsPan = new Panel(new LinearLayout(Direction.VERTICAL));

        //Selected tags panel
        SelectedTagsPanel selectedTagsPanel = new SelectedTagsPanel(gui);
        tagsPan.addComponent(selectedTagsPanel.getPanel().withBorder(Borders.singleLine()));

        //Available tags panel
        AvailableTagsPanel availableTagsPanel = new AvailableTagsPanel(gui);
        tagsPan.addComponent(availableTagsPanel.getPanel().withBorder(Borders.singleLine()));

        tempPan.addComponent(tagsPan);

        //Bookmarks panel
        BookmarksPanel bookmarksPanel = new BookmarksPanel(gui);
        tempPan.addComponent(bookmarksPanel.getPanel().withBorder(Borders.singleLine()));

        panel.addComponent(tempPan);

        BasicWindow window = new BasicWindow();
        window.setComponent(panel.withBorder(Borders.singleLine()));
        window.setHints(Arrays.asList(Window.Hint.FIT_TERMINAL_WINDOW,Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));
        gui.addWindowAndWait(window);
    }

}
