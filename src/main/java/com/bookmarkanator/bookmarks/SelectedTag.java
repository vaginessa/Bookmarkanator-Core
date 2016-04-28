package com.bookmarkanator.bookmarks;

import java.util.*;
import javax.swing.*;
import com.bookmarkanator.interfaces.*;

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
