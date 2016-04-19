package com.bookmarkanator.ui;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import com.bookmarkanator.*;

/**
 * Created by micah on 4/17/16.
 */
public class UIUtil {
    public static Image getTerminalIcon()
    {
        File f = new File(App.class.getCanonicalName());
        try
        {
            System.out.println(f.getCanonicalPath().toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        URL imageURL = App.class.getResource("/img/test__Icon.gif");
//        Image image = Toolkit.getDefaultToolkit().getImage("resources/com/bookmarkanator/img/test_Icon.gif");
        return new ImageIcon(imageURL).getImage();
    }
}
