package com.bookmarkanator.ui;

import com.bookmarkanator.bookmarks.Bookmark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BookmarksPanel extends JPanel {
    private JScrollPane scroll;
    private List<Bookmark> bookmarkList;
    private JPanel pan;


    public BookmarksPanel() {
        super();
        setBorder(BorderFactory.createLineBorder(Color.black));


        scroll = new JScrollPane();
        scroll.setPreferredSize(new Dimension(500,500));
//        scroll.setMinimumSize(new Dimension(300,300));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pan = new JPanel();
//        GridLayout gl = new GridLayout();
//        gl.setHgap(20);
//        gl.setVgap(10);
//        gl.setColumns(4);
//        gl.setRows(5);
//
//        pan.setLayout(gl);
        pan.setBackground(new Color(214,214,214));

        scroll.getViewport().add(pan);
        this.add(scroll);
        this.setBackground(Color.yellow);
    }

    public void refresh()
    {
        for (Bookmark b: getBookmarkList())
        {
            BookmarkPanel bp = new BookmarkPanel(b);
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
