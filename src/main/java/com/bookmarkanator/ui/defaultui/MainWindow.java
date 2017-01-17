package com.bookmarkanator.ui.defaultui;

import com.bookmarkanator.core.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

public class MainWindow
{
    private Bootstrap bootstrap;

    public MainWindow()
        throws Exception
    {
        init();
    }

    public void init()
        throws Exception
    {
//        bootstrap = new Bootstrap();

        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        final MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));


        MenuPanel menuPanel = new MenuPanel(gui);

        Panel panel = new Panel();
        panel.setPreferredSize(new TerminalSize(60,10));
        panel.setLayoutManager(new GridLayout(1));

        panel.addComponent(menuPanel.getPanel());

        BasicWindow window = new BasicWindow();
        window.setComponent(panel);


        gui.addWindowAndWait(window);
    }

    private Panel getContentPanel()
    {
        Panel panel = new Panel();
        return panel;
    }
}
