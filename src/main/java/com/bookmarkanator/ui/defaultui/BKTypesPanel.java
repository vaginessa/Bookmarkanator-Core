package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.ui.defaultui.interfaces.*;
import com.googlecode.lanterna.gui2.*;

public class BKTypesPanel extends BasicUIItem implements BKTypesInterface
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;
    private CheckBoxList<String> checkBoxList;

    public BKTypesPanel()
    {
    }

    public BKTypesPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;

        checkBoxList = new CheckBoxList<>();
        checkBoxList.addItem("Text");
        checkBoxList.addItem("Terminal");
        checkBoxList.addItem("Sequence");
        checkBoxList.addItem("Web");
        checkBoxList.addListener(new CheckBoxList.Listener()
        {
            @Override
            public void onStatusChanged(int itemIndex, boolean checked)
            {
                //Change settings at this point.
                System.out.println("index "+itemIndex+" "+checked);
            }
        });
    }

    public Panel getPanel()
    {
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        mainPanel.addComponent(checkBoxList);

        return mainPanel;
    }

    @Override
    public void setTypes(Set<String> bookmarkTypes)
    {

    }

    @Override
    public Set<String> getTypes()
    {
        return null;
    }
}
