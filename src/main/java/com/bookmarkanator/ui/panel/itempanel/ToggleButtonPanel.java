package com.bookmarkanator.ui.panel.itempanel;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ToggleButtonPanel<E> extends StringPanel
{
    private JToggleButton button;
    private final ToggleButtonPanel thisPan = this;

    public ToggleButtonPanel()
    {
        super();
    }

    private ToggleButtonPanel(E text, Observer observer, String type) {
        super(text, observer, type);

        final ToggleButtonPanel thisPan = this;


//        button.setPreferredSize(new Dimension(15,15));
//        button.setMinimumSize(new Dimension(15,15));
//        button.setMargin(new Insets(0, 0, 0, 0));


        prepareListeners();


    }
    @Override
    protected void prepareUI()
    {
        button = new JToggleButton(getItem().toString());
        this.setBorder(BorderFactory.createRaisedBevelBorder());

        this.add(button);
    }

    @Override
    public void prepareListeners()
    {
        if (button!=null)
        {
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    getObserver().update(null, thisPan);
                }
            });
        }
    }

    public ToggleButtonPanel getNew(Object item, Observer observer, String type) {
        return new ToggleButtonPanel(item, observer, type);
    }

    public ToggleButtonPanel getNew(StringPanel newPanel)
    {
        return new ToggleButtonPanel(newPanel.getText(), newPanel.getObserver(), newPanel.getType());
    }
}
