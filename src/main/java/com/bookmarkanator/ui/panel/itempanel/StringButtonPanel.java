package com.bookmarkanator.ui.panel.itempanel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class StringButtonPanel<E> extends StringPanel
{
    private JButton button;
    private final StringButtonPanel thisPan = this;

    public StringButtonPanel()
    {
        super();
    }

    private StringButtonPanel(E text, Observer observer, String type) {
        super(text, observer, type);

        final StringButtonPanel thisPan = this;

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
    
    public StringPanel getNew(String text, Observer observer, String type) {
        return new StringButtonPanel(text, observer, type);
    }

    public StringPanel getNew(StringPanel newPanel)
    {
        return new StringButtonPanel(newPanel.getText(), newPanel.getObserver(), newPanel.getType());
    }
}
