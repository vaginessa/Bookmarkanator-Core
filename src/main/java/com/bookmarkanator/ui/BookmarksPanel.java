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


    public BookmarksPanel(Dimension dimension) {
        super();
        scroll = new JScrollPane();
        scroll.setPreferredSize(dimension);
        pan = new JPanel();
//        GridLayout gl = new GridLayout();
//        gl.setHgap(20);
//        gl.setVgap(10);
//        gl.setColumns(4);
//        gl.setRows(5);
//
//        pan.setLayout(gl);
        pan.setBackground(Color.red);
        pan.setPreferredSize(dimension);

//        for (int c=0;c<10;c++)
//        {
//            JPanel tmpPan = new JPanel();
//            JLabel label = new JLabel("");
//            tmpPan.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    super.mouseClicked(e);
//                    System.out.println("Mouse clicked!");
//                    if (label.getText().equals("clicked"))
//                    {
//                        label.setText("Un clicked");
//                    }
//                    else
//                    {
//                        label.setText("clicked");
//                    }
//
//                }
//            });
//
//            tmpPan.setBackground(Color.getHSBColor(c*10, c*20, c*30));
//            tmpPan.setPreferredSize(new Dimension(80, 30));
//            tmpPan.add(label);
//            pan.add(tmpPan);
//        }
//
//        for (int c=0;c<10;c++)
//        {
//            JPanel tmpPan = new JPanel();
//            JButton button = new JButton("          ");
//            button.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    super.mouseClicked(e);
//                    System.out.println("Mouse clicked!");
//                    if (button.getText().equals("clicked"))
//                    {
//                        button.setText("Un clicked");
//                    }
//                    else
//                    {
//                        button.setText("clicked");
//                    }
//
//                }
//            });
//
//            tmpPan.setBackground(Color.getHSBColor(c*10, c*20, c*30));
//            tmpPan.setPreferredSize(new Dimension(80, 30));
//            tmpPan.add(button);
//            pan.add(tmpPan);
//        }

        scroll.getViewport().add(pan);
        this.add(scroll);
        this.setBackground(Color.yellow);
    }

    public void refresh()
    {
//        pan.removeAll();
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
