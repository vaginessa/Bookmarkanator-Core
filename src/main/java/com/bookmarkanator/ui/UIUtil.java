package com.bookmarkanator.ui;

import com.bookmarkanator.App;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by micah on 4/17/16.
 */
public class UIUtil {
    public static Image getTerminalIcon()
    {
        URL imageURL = App.class.getResource("/img/test_Icon.gif");
//        Image image = Toolkit.getDefaultToolkit().getImage("resources/com/bookmarkanator/img/test_Icon.gif");
        return new ImageIcon(imageURL).getImage();
    }
}
