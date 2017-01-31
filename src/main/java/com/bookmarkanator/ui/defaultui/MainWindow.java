package com.bookmarkanator.ui.defaultui;

import java.io.*;
import java.util.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

public class MainWindow
{

    //values used to append separate default bookmarkanator settings from UI settings.
    public static final String UI_PREFIX_KEY = "ui-prefix-value";
    public static final String UI_PREFIX_VALUE = "ui-";
    public static final String UI_CLASS_PREFIX_KEY = "ui-class-prefix-value";
    public static final String UI_CLASS_PREFIX_VALUE = "class-";

    public static final String ENCRYPTED_BOOKMARK_BASECLASS = "com.bookmarkanator.bookmarks.EncryptedBookmark";
    public static final String ENCRYPTED_BOOKMARK_DEFAULT_CLASS = "com.bookmarkanator.ui.defaultui.bookmarks.EncryptedBookmarkUI";

    public static final String FILE_BOOKMARK_BASECLASS = "com.bookmarkanator.bookmarks.FileBookmark";
    public static final String FILE_BOOKMARK_DEFAULT_CLASS = "com.bookmarkanator.ui.defaultui.bookmarks.FileBookmarkUI";

    public static final String REMINDER_BOOKMARK_BASECLASS = "com.bookmarkanator.bookmarks.ReminderBookmark";
    public static final String REMINDER_BOOKMARK_DEFAULT_CLASS = "com.bookmarkanator.ui.defaultui.bookmarks.ReminderBookmarkUI";

    public static final String SEQUENCE_BOOKMARK_BASECLASS = "com.bookmarkanator.bookmarks.SequenceBookmark";
    public static final String SEQUENCE_BOOKMARK_DEFAULT_CLASS = "com.bookmarkanator.ui.defaultui.bookmarks.SequenceBookmarkUI";

    public static final String TERMINAL_BOOKMARK_BASECLASS = "com.bookmarkanator.bookmarks.TerminalBookmark";
    public static final String TERMINAL_BOOKMARK_DEFAULT_CLASS = "com.bookmarkanator.ui.defaultui.bookmarks.TerminalBookmarkUI";

    public static final String TEXT_BOOKMARK_BASECLASS = "com.bookmarkanator.bookmarks.TextBookmark";
    public static final String TEXT_BOOKMARK_DEFAULT_CLASS = "com.bookmarkanator.ui.defaultui.bookmarks.TextBookmarkUI";

    public static final String WEB_BOOKMARK_BASECLASS = "com.bookmarkanator.bookmarks.WebBookmark";
    public static final String WEB_BOOKMARK_DEFAULT_CLASS = "com.bookmarkanator.ui.defaultui.bookmarks.WebBookmarkUI";

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
        guiController = new GUIController(bootstrap);

        Settings settings = bootstrap.getSettings();
        if (settings.diffInto(getDefaultSettings()))
        {
            bootstrap.saveSettingsFile();
        }

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
        guiController.setMenuUi(menuPanel);

        //Search Panel
        SearchPanel searchPanel = new SearchPanel(gui);
        searchPanel.setGUIController(guiController);
        panel.addComponent(searchPanel.getPanel().withBorder(Borders.singleLine()));
        guiController.setSearchUI(searchPanel);

        Panel tempPan = new Panel(new LinearLayout(Direction.HORIZONTAL));
//
//        //Types Panel
//        BKTypesPanel types = new BKTypesPanel(gui);
//        types.setGUIController(guiController);
//        tempPan.addComponent(types.getPanel().withBorder(Borders.singleLine()));
//        guiController.setTypesUI(types);

        Panel tagsPan = new Panel(new LinearLayout(Direction.VERTICAL));

        //Selected tags panel
        SelectedTagsPanel selectedTagsPanel = new SelectedTagsPanel(gui);
        selectedTagsPanel.setGUIController(guiController);
        tagsPan.addComponent(selectedTagsPanel.getPanel().withBorder(Borders.singleLine()));
        guiController.setSelectedTagsUI(selectedTagsPanel);

        //Available tags panel
        AvailableTagsPanel availableTagsPanel = new AvailableTagsPanel(gui);
        availableTagsPanel.setGUIController(guiController);
        tagsPan.addComponent(availableTagsPanel.getPanel().withBorder(Borders.singleLine()));
        guiController.setAvailableTagsUI(availableTagsPanel);

        QuickPanelWindow quickPanelWindow = new QuickPanelWindow();
        quickPanelWindow.setGUIController(this.guiController);
        this.guiController.setQuickPanelUI(quickPanelWindow);

        tempPan.addComponent(tagsPan);

        //Bookmarks panel
        BookmarksPanel bookmarksPanel = new BookmarksPanel();
        guiController.setBookmarksListUI(bookmarksPanel);
        bookmarksPanel.setGUIController(guiController);

        tempPan.addComponent(bookmarksPanel.getPanel().withBorder(Borders.singleLine()));

        panel.addComponent(tempPan);

        BasicWindow window = new BasicWindow();
        window.setComponent(panel.withBorder(Borders.singleLine()));
        window.setHints(Arrays.asList(Window.Hint.FIT_TERMINAL_WINDOW, Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));

        guiController.initUI();

        gui.addWindowAndWait(window);
    }

    private Settings getDefaultSettings()
        throws FileNotFoundException
    {
        Settings res = new Settings();
        String base = MainWindow.UI_PREFIX_VALUE + "" + MainWindow.UI_CLASS_PREFIX_VALUE;
        res.putSetting(base + MainWindow.ENCRYPTED_BOOKMARK_BASECLASS, MainWindow.ENCRYPTED_BOOKMARK_DEFAULT_CLASS);
        res.putSetting(base + MainWindow.FILE_BOOKMARK_BASECLASS,MainWindow.FILE_BOOKMARK_DEFAULT_CLASS);
        res.putSetting(base + MainWindow.REMINDER_BOOKMARK_BASECLASS, MainWindow.REMINDER_BOOKMARK_DEFAULT_CLASS);
        res.putSetting(base + MainWindow.SEQUENCE_BOOKMARK_BASECLASS, MainWindow.SEQUENCE_BOOKMARK_DEFAULT_CLASS);
        res.putSetting(base + MainWindow.TERMINAL_BOOKMARK_BASECLASS, MainWindow.TERMINAL_BOOKMARK_DEFAULT_CLASS);
        res.putSetting(base + MainWindow.TEXT_BOOKMARK_BASECLASS, MainWindow.TEXT_BOOKMARK_DEFAULT_CLASS);
        res.putSetting(base + MainWindow.WEB_BOOKMARK_BASECLASS, MainWindow.WEB_BOOKMARK_DEFAULT_CLASS);

        return res;
    }
}
