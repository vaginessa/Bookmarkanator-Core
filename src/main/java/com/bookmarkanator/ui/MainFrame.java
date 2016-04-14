package com.bookmarkanator.ui;

import com.bookmarkanator.bookmarks.Bookmark;
import com.bookmarkanator.resourcetypes.DefaultSystemResource;
import com.bookmarkanator.resourcetypes.TerminalResource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;

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
        terminal.setName("/etc");

        TerminalResource tr = new TerminalResource();
        tr.setText("cd /etc");

        terminal.setResource(tr);

        Bookmark fileOpen = new Bookmark();
        fileOpen.setName("open home");

        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
        dsr.setText("/home");

        fileOpen.setResource(dsr);

        List<Bookmark> bm = new ArrayList<>();
        bm.add(web);
        bm.add(terminal);
        bm.add(fileOpen);

        bookmarksPan.setBookmarkList(bm);

//        JPanel bookmarksPan = new JPanel();
//        bookmarksPan.setBackground(Color.cyan);
        frame.add(bookmarksPan, BorderLayout.CENTER);
    }

}
