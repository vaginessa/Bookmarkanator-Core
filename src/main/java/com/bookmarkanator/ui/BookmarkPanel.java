package com.bookmarkanator.ui;

import com.bookmarkanator.abstracted.CustomClass;
import com.bookmarkanator.bookmarks.Bookmark;
import com.bookmarkanator.resourcetypes.BasicResource;
import com.bookmarkanator.resourcetypes.CustomFileFilter;
import com.bookmarkanator.resourcetypes.DefaultSystemResource;
import com.bookmarkanator.resourcetypes.TerminalResource;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class BookmarkPanel extends JPanel{
    private Bookmark bookmark;
    private JLabel label;
    private JLabel sybmol;

    public BookmarkPanel(Bookmark bookmark) {
        this.bookmark = bookmark;
        BookmarkPanel thisPan = this;

        thisPan.setBorder(BorderFactory.createRaisedBevelBorder());

        this.setBackground(new Color(240, 238, 188));
        label = new JLabel(bookmark.getName());
        label.setBorder(BorderFactory.createLineBorder(Color.black));//.createBevelBorder(BevelBorder.RAISED));
        System.out.println("Bookmark "+label.getText());
        sybmol = new JLabel();
        sybmol.setBorder(BorderFactory.createLineBorder(Color.black));

//        this.setPreferredSize(new Dimension(165,35));
        System.out.println(bookmark.getResource().getClass().getName());

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
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public JLabel getLabel() {
        return label;
    }

    public JLabel getSybmol() {
        return sybmol;
    }
}
