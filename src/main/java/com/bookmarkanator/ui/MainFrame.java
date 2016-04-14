package com.bookmarkanator.ui;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.resourcetypes.*;

public class MainFrame  {
    private JFrame frame;
    private GridBagConstraints con;

    public MainFrame()
    {
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(800,800));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);

        init();

        frame.pack();
        frame.setVisible(true);
    }


    private void init()
    {
        con = new GridBagConstraints();
        BookmarksPanel bookmarksPan = new BookmarksPanel(new Dimension(500,500));

        Bookmark web = new Bookmark();
        web.setName("yahoo.com");
        DefaultSystemResource dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("yahoo.com");
        dsr.setText("http://www.yahoo.com");
        web.setResource(dsr);


        Bookmark terminal = new Bookmark();
        terminal.setName("pwd");
        TerminalResource tr = new TerminalResource();
        tr.setText("pwd");
        terminal.setResource(tr);

        Bookmark terminal2 = new Bookmark();
        terminal2.setName("ls");
        tr = new TerminalResource();
        tr.setText("ls");
        terminal2.setResource(tr);

        Bookmark terminal3 = new Bookmark();
        terminal3.setName("mkdir");
        tr = new TerminalResource();
        tr.setText("mkdir hello");
        terminal3.setResource(tr);

        Bookmark terminal4 = new Bookmark();
        terminal4.setName("remove hello");
        tr = new TerminalResource();
        tr.setText("rm hello");
        terminal4.setResource(tr);

        Bookmark fileOpen = new Bookmark();
        fileOpen.setName("open home");

        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
        dsr.setText("/home");

        fileOpen.setResource(dsr);

        List<Bookmark> bm = new ArrayList<>();
        bm.add(web);
        bm.add(terminal);
        bm.add(terminal2);
        bm.add(terminal3);
        bm.add(fileOpen);

        bookmarksPan.setBookmarkList(bm);

//        JPanel bookmarksPan = new JPanel();
//        bookmarksPan.setBackground(Color.cyan);
        frame.add(bookmarksPan, BorderLayout.CENTER);
    }

}
