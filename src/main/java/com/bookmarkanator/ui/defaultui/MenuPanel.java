package com.bookmarkanator.ui.defaultui;

import com.bookmarkanator.ui.interfaces.*;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;

public class MenuPanel extends BasicUIItem implements MenuInterface
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;

    public MenuPanel()
    {
    }

    public MenuPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;
    }

    public Panel getPanel()
    {
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        mainPanel.addComponent(getFileMenu());
        mainPanel.addComponent(getEditMenu());
        mainPanel.addComponent(getViewMenu());
        mainPanel.addComponent(getHelpMenu());

        return mainPanel;
    }

    private Panel getFileMenu()
    {
        Panel panel = new Panel(new GridLayout(1));


        Button file = new Button("File", new Runnable() {
            @Override
            public void run() {
                new ActionListDialogBuilder()
                    .setTitle("File Menu")
                    .addAction("New", new Runnable() {
                        @Override
                        public void run() {
                            // Do 1st thing...
                            System.out.println("new");
                        }
                    })
                    .addAction("Import", new Runnable() {
                        @Override
                        public void run() {
                            // Do 2nd thing...
                            System.out.println("import");
                        }
                    })
                    .addAction("Export", new Runnable() {
                        @Override
                        public void run() {
                            // Do 3rd thing...
                            System.out.println("export");
                        }
                    })
                    .addAction("Exit", new Runnable() {
                        @Override
                        public void run() {
                            // Do 3rd thing...
                            System.out.println("exit");
                            System.exit(0);
                        }
                    })
                    .build()
                    .showDialog(gui);
            }});

        panel.addComponent(file);

        return panel;
    }

    private Panel getEditMenu()
    {
        Panel panel = new Panel(new GridLayout(1));


        Button file = new Button("Edit", new Runnable() {
            @Override
            public void run() {
                new ActionListDialogBuilder()
                    .setTitle("Edit Menu")
                    .addAction("Undo", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .addAction("Redo", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .addAction("Enter Edit Mode", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .addAction("Leave Edit Mode", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .addAction("Tag Editor", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .addAction("Settings", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .build()
                    .showDialog(gui);
            }});

        panel.addComponent(file);

        return panel;
    }

    private Panel getViewMenu()
    {
        Panel panel = new Panel(new GridLayout(1));


        Button file = new Button("View", new Runnable() {
            @Override
            public void run() {
                new ActionListDialogBuilder()
                    .setTitle("View Menu")
                    .addAction("Switch To Quick Panel", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .build()
                    .showDialog(gui);
            }});

        panel.addComponent(file);

        return panel;
    }

    private Panel getHelpMenu()
    {
        Panel panel = new Panel(new GridLayout(1));


        Button file = new Button("Help", new Runnable() {
            @Override
            public void run() {
                new ActionListDialogBuilder()
                    .setTitle("Help Menu")
                    .addAction("About", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .addAction("Tutorial", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .addAction("Documentation", new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .build()
                    .showDialog(gui);
            }});

        panel.addComponent(file);

        return panel;
    }


}
