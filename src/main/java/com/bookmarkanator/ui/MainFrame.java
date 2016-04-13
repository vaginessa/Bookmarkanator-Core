package com.bookmarkanator.ui;

import java.awt.*;
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
//        JPanel bookmarksPan = new JPanel();
//        bookmarksPan.setBackground(Color.cyan);
        frame.add(bookmarksPan, BorderLayout.CENTER);
    }

}
