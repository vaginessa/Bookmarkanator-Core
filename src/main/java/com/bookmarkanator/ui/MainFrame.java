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
        frame.getContentPane().setLayout(new GridBagLayout());
        JFrame.setDefaultLookAndFeelDecorated(true);

        init();

        frame.pack();
        frame.setVisible(true);
    }


    private void init()
    {

        con = new GridBagConstraints();
        con.fill = GridBagConstraints.BOTH;
        con.weightx = .20;
        con.weighty = 1.0;
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 1;
        con.gridheight = 2;
        BookmarkTypesPanel bookmarkTypes = new BookmarkTypesPanel();
        bookmarkTypes.setMinimumSize(new Dimension(80,500));
        bookmarkTypes.setMaximumSize(new Dimension(200,500));

        frame.add(bookmarkTypes, con);

        con.fill = GridBagConstraints.BOTH;
        con.weightx = 1.0;
        con.gridheight = 1;
        con.gridx = 1;
        con.gridy = 0;
        SelectedTagsPanel selectedTags = new SelectedTagsPanel();
        frame.add(selectedTags, con);

        con.gridy = 1;
        con.gridx = 1;
        TagsSelectionPanel tagsSelectionPan = getTestTagSelectionPanel();
        frame.add(tagsSelectionPan,con);

        con.weightx = .75;
        con.gridx = 2;
        con.gridy = 0;
        con.gridheight = 2;
        BookmarksPanel bookmarksPan = getTestBookmarks();
//        bookmarksPan.setPreferredSize(new Dimension(100, 1));
        JPanel tmpPan = new JPanel();
        tmpPan.setLayout(new GridLayout());
        tmpPan.add(bookmarksPan);
        frame.add(tmpPan,con);

//        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 3;
        con.gridheight = 1;
        con.weighty = .20;
        OptionsPanel options = new OptionsPanel();
        options.setMinimumSize(new Dimension(-1,80));
//        options.setPreferredSize(new Dimension(-1, 80));
        frame.add(options, con);
    }

    private TagsSelectionPanel getTestTagSelectionPanel()
    {
        TagsSelectionPanel tp = new TagsSelectionPanel();

        Set<String> tags = new HashSet<>();

        tags.add("hello");
        tags.add("bye");
        tags.add("yo!");

        tp.setTags(tags);
        return tp;
    }

    private BookmarksPanel getTestBookmarks()
    {
        BookmarksPanel bookmarksPan = new BookmarksPanel();

        Bookmark web = new Bookmark();
        web.setName("yahoo.com");
        DefaultSystemResource dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("yahoo.com");
        dsr.setText("http://www.yahoo.com");
        web.setResource(dsr);


        Bookmark terminal = new Bookmark();
        terminal.setName("pwd");
        TerminalResource tr = new TerminalResource(TerminalResource.OPEN_TERMINAL_ONLY);
        tr.setText("pwd");
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

        for (int c=0;c<100;c++)
        {
            bm.add(fileOpen);
        }

        bookmarksPan.setBookmarkList(bm);
        return bookmarksPan;
    }

}
