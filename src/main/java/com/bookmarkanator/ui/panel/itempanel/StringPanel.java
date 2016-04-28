package com.bookmarkanator.ui.panel.itempanel;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.bookmarkanator.interfaces.*;

/**
 * Created by micah on 4/23/16.
 * <p>
 * This class is a panel to show a string. When it is clicked the supplied observer is notified.
 */
public class StringPanel<E> extends JPanel implements StringPanelInterface {

    private Observer observer;//only one observer per panel... Or should it have many?
    private JLabel label;//What will be displayed in the jpanel
    private String type;//used for identifying this panel.
    private final StringPanelInterface thisPan = this;
    private E item;

    public StringPanel() {
        super();
    }

    public StringPanel(E item, Observer observer, String type) {
        this.item = item;
        this.observer = observer;
        label = new JLabel(item.toString());
        this.type = type;

        this.setBorder(BorderFactory.createRaisedBevelBorder());

        this.add(label);

        prepareListeners();
    }

    @Override
    public void prepareListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                observer.update(null, thisPan);
            }
        });
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getText() {
        return label.getText();
    }

    @Override
    public void setText(String text) {
        this.label.setText(text);
    }

    @Override
    public E getItem() {
        return item;
    }

    @Override
    public void setItem(Object item) {
        this.item = (E)item;
        setText(item.toString());
    }

    @Override
    public Observer getObserver() {
        return observer;
    }

    @Override
    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public StringPanel getNew(StringPanelInterface spi) {
        return new StringPanel(spi.getText(), spi.getObserver(), spi.getType());
    }

    @Override
    public StringPanel getNew(String text, Observer observer, String type) {
        return new StringPanel(text, observer, type);
    }
}
