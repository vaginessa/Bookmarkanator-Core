package com.bookmarkanator.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.bookmarkanator.interfaces.*;

public class ListableItemPanel extends JPanel{
    private final ListableItem item;
    private JLabel label;
    private JLabel sybmol;
    private Icon icon;

    public ListableItemPanel(final ListableItem item) {
        this.item = item;
        final ListableItemPanel thisPan = this;
        thisPan.setBorder(BorderFactory.createRaisedBevelBorder());

        label = new JLabel(item.getName());

        if (item.getTypeString()!=null && !item.getTypeString().isEmpty())
        {
            sybmol = new JLabel(item.getTypeString());
            sybmol.setBorder(BorderFactory.createLineBorder(Color.blue));
            this.add(sybmol);
        }

        this.add(label);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                item.setLastAccessedDate(new Date());
                try {
                    item.execute();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

//        JButton button = new JButton();
//        button.setIcon(new ImageIcon(UIUtil.getTerminalIcon()));
//        this.add(button);
    }

    public ListableItem getItem() {
        return item;
    }

    public JLabel getLabel() {
        return label;
    }

    public JLabel getSybmol() {
        return sybmol;
    }
}
