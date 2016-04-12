package com.bookmarkanator.gui;

import java.awt.*;
import javax.swing.*;

public class MainFrame  {
    private JFrame frame;

    public MainFrame()
    {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(500,500);
        frame.setPreferredSize(new Dimension(500,500));
        frame.getContentPane().add(new JLabel("hello"));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        Image image = Toolkit.getDefaultToolkit().getImage("resources/com/bookmarkanator/img/test__Icon.gif");
//        Image image = Toolkit.getDefaultToolkit().getImage("resources/com/bookmarkanator/img/Run.png");
//        Image image = new ImageIcon(getClass().getResource("com/bookmarkanator/resources/img/Run.png")).getImage();

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
            new TrayIcon(image, "Name of icon");
        final TrayIcon bob =
        new TrayIcon(image, "Name of icon");
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();



        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);


            tray.add(bob);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        System.out.println(tray.getTrayIcons().length+" "+tray.getTrayIconSize());
        trayIcon.displayMessage("message", "message", TrayIcon.MessageType.ERROR);
        frame.pack();
        frame.setVisible(true);
    }

}
