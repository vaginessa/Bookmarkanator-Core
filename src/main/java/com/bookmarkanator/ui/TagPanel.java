package com.bookmarkanator.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

/**
 * Created by micah on 4/15/16.
 */
public class TagPanel extends JPanel {
    private JLabel label;

    public TagPanel(String tagText) {
        label = new JLabel(tagText);
        final TagPanel thisPan = this;
        thisPan.setLayout(new BoxLayout(thisPan, BoxLayout.PAGE_AXIS));
//        thisPan.setPreferredSize(new Dimension(80,50));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                System.out.println("Bookmark "+thisPan.getLabel().getText());
                //TODO add code to send an event to the item that will modify the list of tags.
                System.out.println("Tag clicked! "+thisPan.getLabel().getText());
            }
        });
        this.add(label);
    }

    public JLabel getLabel() {
        return label;
    }
}
