package com.bookmarkanator.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.bookmarkanator.interfaces.*;

public class ListableButtonItemPanel extends JPanel
{
    private final ListableItem item;
    private JLabel label;
    private JLabel sybmol;
    private Icon icon;
    private JButton button;

    public ListableButtonItemPanel(final ListableItem item) {
        this.item = item;
        final ListableButtonItemPanel thisPan = this;
        thisPan.setBorder(BorderFactory.createRaisedBevelBorder());

        label = new JLabel(item.getName());

        if (icon==null)
        {
            if (item.getTypeString() != null && !item.getTypeString().isEmpty())
            {
                sybmol = new JLabel(item.getTypeString());
                sybmol.setBorder(BorderFactory.createLineBorder(Color.blue));
                this.add(sybmol);
            }
        }
        else
        {
            //add an icon instead of type text
        }

        this.add(label);

//        this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e);
//                item.setLastAccessedDate(new Date());
//                try {
//                    item.execute();
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//
        button = new JButton("x");
        button.setPreferredSize(new Dimension(15,15));
        button.setMinimumSize(new Dimension(15,15));
        button.setMargin(new Insets(0, 0, 0, 0));
//        button.setBorderPainted(false);

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                item.setLastAccessedDate(new Date());
                try {
                    item.execute();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });


        this.add(button);

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
