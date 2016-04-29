package com.bookmarkanator.ui.panel.itempanel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class XButtonPanel<E> extends StringPanel
{
    private JButton button;
    private final XButtonPanel thisPan = this;

    public XButtonPanel()
    {
        super();
    }

    private XButtonPanel(E text, Observer observer, String type) {
        super(text, observer, type);

        final XButtonPanel thisPan = this;

        button = new JButton("x");
        button.setPreferredSize(new Dimension(15,15));
        button.setMinimumSize(new Dimension(15,15));
        button.setMargin(new Insets(0, 0, 0, 0));


        prepareListeners();

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
    
    public XButtonPanel getNew(Object item, Observer observer, String type) {
        return new XButtonPanel(item, observer, type);
    }

    public XButtonPanel getNew(StringPanel newPanel)
    {
        return new XButtonPanel(newPanel.getText(), newPanel.getObserver(), newPanel.getType());
    }
}
