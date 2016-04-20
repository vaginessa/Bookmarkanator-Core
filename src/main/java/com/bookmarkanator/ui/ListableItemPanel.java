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
//        label.setBorder(BorderFactory.createLineBorder(Color.black));//.createBevelBorder(BevelBorder.RAISED));

        sybmol = new JLabel();
        sybmol.setBorder(BorderFactory.createLineBorder(Color.blue));


//        init();

        this.add(sybmol);
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

//    private void init()
//    {
//        if (item.getResource() instanceof DefaultSystemResource)
//        {
//            DefaultSystemResource df = (DefaultSystemResource)item.getResource();
//            if (df.getType()==DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER)
//            {
//                sybmol.setText("Web");
//            }
//            else if (df.getType()==DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR)
//            {
//                sybmol.setText("Edit");
//            }
//            else if (df.getType()==DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER)
//            {
//                sybmol.setText("Folder");
//            }
//            else
//            {
//                sybmol.setText("SR ???");
//            }
//        }
//        else if (item.getResource() instanceof TerminalResource)
//        {
//            sybmol.setText("Term");
//        }
//        else if (item.getResource() instanceof CustomClass)
//        {
//            sybmol.setText("Custom");
//        }
//        else if (item.getResource() instanceof BasicResource)
//        {
//            sybmol.setText("Basic");
//        }
//        else
//        {
//            sybmol.setText("???");
//        }
//    }

    public JLabel getSybmol() {
        return sybmol;
    }
}
