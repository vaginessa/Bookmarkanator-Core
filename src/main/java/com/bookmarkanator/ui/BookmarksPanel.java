package com.bookmarkanator.ui;

import com.bookmarkanator.bookmarks.Bookmark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BookmarksPanel extends JScrollPane {
    private JScrollPane scroll;
    private List<Bookmark> bookmarkList;
    private JPanel pan;


    public BookmarksPanel() {
        super();

        setBorder(BorderFactory.createLineBorder(Color.black));
//        this.setLayout(new GridBagLayout());
//        GridBagConstraints con = new GridBagConstraints();
//        con.fill = GridBagConstraints.BOTH;
//        con.weightx = 1;
//        con.weighty = 1;
        pan = new JPanel();
//        pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
//        pan.setPreferredSize(new Dimension(100,-1));
//        pan.getMaximumSize();
//        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
//        GridLayout grid = new GridLayout(0,1);
//        grid.setVgap(10);
//        grid.setHgap(10);
//        pan.setLayout(grid);

        scroll = new JScrollPane();
//        this.setLayout(new ModifiedFlowLayout(2));
//        this.setLayout(new GridLayout());
//        scroll.setMinimumSize(new Dimension(500,500));
//        scroll.setMaximumSize(new Dimension(100,500));
//        scroll.setPreferredSize(new Dimension(100,500));
//        scroll.getViewport().setMaximumSize(new Dimension(100,100));
//        pan.setBackground(new Color(214,214,214));
//        JPanel tmpPan = new JPanel();
//        tmpPan.setLayout(new BoxLayout(tmpPan, BoxLayout.PAGE_AXIS));
//        tmpPan.add(pan);
//        pan.setLayout(new GridLayout(0,1));
//        scroll.getViewport().add(pan);
        this.getViewport().add(pan);
        pan.setLayout(new ModifiedFlowLayout(1));
//        pan.setPreferredSize(new Dimension(150,-1));
        this.setBackground(Color.yellow);
//        this.setPreferredSize(new Dimension(150,500));
    }



    public void refresh()
    {
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
