package com.bookmarkanator.ui  ;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.interfaces.*;
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
        JPanel tagsSelectionPan = getTestTagSelectionPanel();
        frame.add(tagsSelectionPan,con);

        con.weightx = .75;
        con.gridx = 2;
        con.gridy = 0;
        con.gridheight = 2;
        ListableItemsPanel bookmarksPan = getTestBookmarks();
        bookmarksPan.setPreferredSize(new Dimension(200, 600));
        frame.add(bookmarksPan,con);

//        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 3;
        con.gridheight = 1;
        con.weighty = .20;
        OptionsPanel options = new OptionsPanel();
        options.setMinimumSize(new Dimension(-1,80));
        frame.add(options, con);
    }

    private JPanel getTestTagSelectionPanel()
    {
        ListableItemsPanel tagsPanel = new ListableItemsPanel();

        List<ListableItem> tags = new ArrayList<>();

        tags.add(new Tag("hello"));
        tags.add(new Tag("bye"));
        tags.add(new Tag("yo!"));

        tagsPanel.setItemsList(tags);
        return tagsPanel;
    }

    private ListableItemsPanel getTestBookmarks()
    {
        ListableItemsPanel bookmarksPan = new ListableItemsPanel();

        Bookmark web = new Bookmark();
        web.setName("yahoo.com");
        DefaultSystemResource dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("yahoo.com");
        dsr.setText("http://www.yahoo.com");
        web.setResource(dsr);

        Bookmark web1 = new Bookmark();
        web1.setName("google.com");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("google.com");
        dsr.setText("http://www.google.com");
        web1.setResource(dsr);

        Bookmark web2 = new Bookmark();
        web2.setName("msn");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("msn");
        dsr.setText("http://www.msn.com");
        web2.setResource(dsr);

        Bookmark web3 = new Bookmark();
        web3.setName("acronymfinder");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("acronymfinder");
        dsr.setText("http://www.acronymfinder.com");
        web3.setResource(dsr);

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

        List<ListableItem> bm = new ArrayList<>();
        bm.add(web);
        bm.add(web1);
        bm.add(web2);
        bm.add(web3);
        bm.add(terminal);
        bm.add(fileOpen);

        for (int c=0;c<10;c++)
        {
            bm.add(fileOpen);
        }

        Bookmark gitignore = new Bookmark();
        gitignore.setName("gitignore");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR);
        dsr.setName("gitignore");
        dsr.setText("/users/lloyd1/.gitignore_global");
        gitignore.setResource(dsr);

        bm.add(gitignore);

        bookmarksPan.setItemsList(bm);
        return bookmarksPan;
    }

}
