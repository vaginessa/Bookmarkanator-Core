package com.bookmarkanator.ui.panel.itempanel;

import com.bookmarkanator.interfaces.ListableItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Created by micah on 4/22/16.
 */
public class ListableToggleButtonItemPanel extends JPanel {
    private final ListableItem item;
    private JLabel sybmol;
    private Icon icon;
    private JToggleButton button;

    public ListableToggleButtonItemPanel(final ListableItem item) {
        this.item = item;
        final ListableToggleButtonItemPanel thisPan = this;
        thisPan.setBorder(BorderFactory.createRaisedBevelBorder());

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
            //add an icon instead of type-text
        }

        button = new JToggleButton(item.getName());
        button.setMargin(new Insets(0, 0, 0, 0));

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
    }

    public JToggleButton getButton() {
        return button;
    }

    public ListableItem getItem() {
        return item;
    }

    public JLabel getSybmol() {
        return sybmol;
    }
}
