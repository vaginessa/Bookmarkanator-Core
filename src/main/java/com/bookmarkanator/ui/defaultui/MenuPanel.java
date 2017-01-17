package com.bookmarkanator.ui.defaultui;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;

public class MenuPanel
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;

    //File menu items
    private Panel filePanel;
    private ActionListBox filePanelMenuItems;
    private Button tmp;
    private boolean showingFileMenuItems;

    public MenuPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;
    }

    public Panel getPanel()
    {
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new GridLayout(4));

        filePanel = getFileMenu();

        mainPanel.addComponent(filePanel);

        Button edit = new Button("Edit");
        mainPanel.addComponent(edit);
        Button view = new Button("View");
        mainPanel.addComponent(view);
        Button help = new Button("Help");
        mainPanel.addComponent(help);

        return mainPanel;
    }

    private Panel getFileMenu()
    {
        Panel panel = new Panel(new GridLayout(1));
        showingFileMenuItems = true;


        Button file = new Button("File", new Runnable() {
            @Override
            public void run() {
                System.out.println("Action asdkfja;lksdfj;laksdjf ");
//                if (showingFileMenuItems)
//                {
//                    filePanel.addComponent(filePanelMenuItems);
////                    filePanel.addComponent(tmp);
//                    showingFileMenuItems = false;
//                }
//                else
//                {
//                    filePanel.removeComponent(filePanelMenuItems);
////                    filePanel.removeComponent(tmp);
//                    showingFileMenuItems = true;
//                }
                new ActionListDialogBuilder()
                    .setTitle("Action List Dialog")
                    .setDescription("Choose an item")
                    .addAction("First Item", new Runnable() {
                        @Override
                        public void run() {
                            // Do 1st thing...
                            System.out.println("First item");
                        }
                    })
                    .addAction("Second Item", new Runnable() {
                        @Override
                        public void run() {
                            // Do 2nd thing...
                            System.out.println("Second item");
                        }
                    })
                    .addAction("Third Item", new Runnable() {
                        @Override
                        public void run() {
                            // Do 3rd thing...
                            System.out.println("Third item");
                        }
                    })
                    .build()
                    .showDialog(gui);
            }});

        panel.addComponent(file);

        filePanelMenuItems = new ActionListBox(new TerminalSize(1,2));
        filePanelMenuItems.addItem("hhh", null);
        filePanelMenuItems.addItem("iii",null);
        tmp = new Button("Tmp button");

        return panel;
    }

}
