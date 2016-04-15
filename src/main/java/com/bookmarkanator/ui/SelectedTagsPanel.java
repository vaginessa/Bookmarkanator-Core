package com.bookmarkanator.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by micah on 4/12/16.
 */
public class SelectedTagsPanel extends JPanel{
    public SelectedTagsPanel() {
        this.setPreferredSize(new Dimension(500,500));
        this.setBackground(Color.orange);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }
}
