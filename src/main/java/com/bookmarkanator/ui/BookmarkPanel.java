package com.bookmarkanator.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.bookmarkanator.abstracted.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.resourcetypes.*;

public class BookmarkPanel extends JPanel{
    private final Bookmark bookmark;
    private JLabel label;
    private JLabel sybmol;
    private Icon icon;

    public BookmarkPanel(final Bookmark bookmark) {
        this.bookmark = bookmark;
        final BookmarkPanel thisPan = this;
        thisPan.setBorder(BorderFactory.createRaisedBevelBorder());
//        this.setBackground(new Color(240, 238, 138));

        label = new JLabel(bookmark.getName());
//        label.setBorder(BorderFactory.createLineBorder(Color.black));//.createBevelBorder(BevelBorder.RAISED));

        sybmol = new JLabel();
        sybmol.setBorder(BorderFactory.createLineBorder(Color.blue));


        init();

        this.add(sybmol);
        this.add(label);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("Bookmark "+thisPan.getLabel().getText());
                bookmark.setLastAccessedDate(new Date());
                try {
                    bookmark.getResource().execute();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

//        JButton button = new JButton();
//        button.setIcon(new ImageIcon(UIUtil.getTerminalIcon()));
//        this.add(button);
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public JLabel getLabel() {
        return label;
    }

    private void init()
    {
        if (bookmark.getResource() instanceof DefaultSystemResource)
        {
            DefaultSystemResource df = (DefaultSystemResource)bookmark.getResource();
            if (df.getType()==DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER)
            {
                sybmol.setText("Web");
            }
            else if (df.getType()==DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR)
            {
                sybmol.setText("Edit");
            }
            else if (df.getType()==DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER)
            {
                sybmol.setText("Folder");
            }
            else
            {
                sybmol.setText("SR ???");
            }
        }
        else if (bookmark.getResource() instanceof TerminalResource)
        {
            sybmol.setText("Term");
        }
        else if (bookmark.getResource() instanceof CustomClass)
        {
            sybmol.setText("Custom");
        }
        else if (bookmark.getResource() instanceof BasicResource)
        {
            sybmol.setText("Basic");
        }
        else
        {
            sybmol.setText("???");
        }
    }

    public JLabel getSybmol() {
        return sybmol;
    }
}
