package com.bookmarkanator.ui;

import com.bookmarkanator.bookmarks.Bookmark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BookmarksPanel extends JPanel {
    private JScrollPane scroll;
    private List<Bookmark> bookmarkList;
    private JPanel pan;
    private JComboBox search;


    public BookmarksPanel() {
        super();
        this.setBackground(Color.yellow);
        this.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();


        this.setBackground(Color.red);
        setBorder(BorderFactory.createLineBorder(Color.red));

        search  = new JComboBox(new String[]{null,"B","C"});
        search.setEditable(true);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked jcombobox "+search.getSelectedIndex());
            }
        });

        scroll = new JScrollPane();

        pan = new JPanel();
        pan.setBorder(BorderFactory.createLineBorder(Color.blue));
        pan.setLayout(new ModifiedFlowLayout(1,10,10));

        scroll.getViewport().add(pan);
        con.fill = GridBagConstraints.BOTH;
        con.weighty = .01;
        con.weightx = 1;
        con.gridx = 0;
        con.gridy = 0;

        this.add(search, con);
        con.weighty = 1;
        con.gridy = 2;
        this.add(scroll, con);
    }



    public void refresh()
    {
        pan.removeAll();
        for (Bookmark b: getBookmarkList())
        {
            BookmarkPanel bp = new BookmarkPanel(b);
            bp.setAlignmentX(Component.CENTER_ALIGNMENT);
            pan.add(bp);
        }
    }

    public List<Bookmark> getBookmarkList() {
        return bookmarkList;
    }

    public void setBookmarkList(List<Bookmark> bookmarkList) {
        this.bookmarkList = bookmarkList;
        refresh();
    }
}
