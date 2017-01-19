package com.bookmarkanator.ui.defaultui;

import com.bookmarkanator.ui.defaultui.interfaces.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;

public class SearchPanel extends BasicUIItem implements SearchInterface
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;
    private boolean showCheckboxes;
    private CheckBoxList<String> checkBoxList;
    private Panel quickPanel;
    private Panel editModePanel;
    private boolean isEditMode;

    public SearchPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;
        showCheckboxes = true;
        isEditMode = true;

        TerminalSize size = new TerminalSize(20, 4);
        checkBoxList = new CheckBoxList<String>(size);
        checkBoxList.addItem("Types");
        checkBoxList.addItem("Available Tags");
        checkBoxList.addItem("Selected Tags");
        checkBoxList.addItem("Bookmarks");
        checkBoxList.addListener(new CheckBoxList.Listener()
        {
            @Override
            public void onStatusChanged(int itemIndex, boolean checked)
            {
                //Change settings at this point.
                System.out.println("index "+itemIndex+" "+checked);
            }
        });

        quickPanel = getQuickPanel();
        editModePanel = getEditModePanel();
    }

    public Panel getPanel()
    {
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        mainPanel.addComponent(getNewBookmarkPanel());
        mainPanel.addComponent(getSearchPanel());
        mainPanel.addComponent(quickPanel);
        mainPanel.addComponent(editModePanel);

        return mainPanel;
    }

    private Panel getNewBookmarkPanel()
    {
        Panel panel = new Panel(new GridLayout(1));


        Button file = new Button("+", new Runnable() {
            @Override
            public void run() {
                System.out.println("clicked new bk button");
            }});

        panel.addComponent(file);

        return panel;
    }

    private Panel getSearchPanel()
    {
        Panel panel = new Panel(new LinearLayout(Direction.HORIZONTAL));

        final TextBox searchField = new TextBox(new TerminalSize(15,1));
        Button button = new Button("Go", new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("Searching for "+searchField.getText());
            }
        });

        Button opts = new Button("*", new Runnable()
        {
            @Override
            public void run()
            {
                if (showCheckboxes)
                {
                    System.out.println("show settings");
                    mainPanel.removeComponent(quickPanel);
                    mainPanel.removeComponent(editModePanel);
                    mainPanel.addComponent(checkBoxList);
                    mainPanel.addComponent(quickPanel);
                    mainPanel.addComponent(editModePanel);
                    showCheckboxes = false;
                }
                else
                {
                    System.out.println("hide settings");
                    mainPanel.removeComponent(checkBoxList);
                    showCheckboxes = true;
                }
            }
        });

        panel.addComponent(searchField);
        panel.addComponent(button);
        panel.addComponent(opts);

        return panel;
    }

    private Panel getQuickPanel()
    {
        Panel panel = new Panel(new GridLayout(1));


        Button file = new Button("Quick Panel", new Runnable() {
            @Override
            public void run() {
                System.out.println("Switch to quick panel.");
            }});

        panel.addComponent(file);

        return panel;
    }

    private Panel getEditModePanel()
    {
        Panel panel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        final Label label = new Label("Off");

        Button editModeButton = new Button("Enter Edit Mode", new Runnable() {
            @Override
            public void run() {
                if (isEditMode)
                {
                    label.setText("On");
                    isEditMode = false;
                }
                else
                {
                    label.setText("Off");
                    isEditMode = true;
                }
            }});


        panel.addComponent(editModeButton);
        panel.addComponent(label);

        return panel;
    }
}
