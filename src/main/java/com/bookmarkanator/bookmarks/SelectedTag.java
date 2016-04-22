package com.bookmarkanator.bookmarks;

import com.bookmarkanator.interfaces.ListableItem;

import javax.swing.*;
import java.util.Date;
import java.util.Observable;

/**
 * Created by micah on 4/21/16.
 */
public class SelectedTag extends Observable implements ListableItem {
    private String text;

    public SelectedTag(String text) {
        this.text = text;
    }

    @Override
    public void setLastAccessedDate(Date lastAccessedDate) {
        //do nothing
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Selected tag clicked: "+text);
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getName() {
        return text;
    }

    @Override
    public String getTypeString() {
        return null;
    }

    @Override
    public Icon getIcon() {
        return null;
    }
}
