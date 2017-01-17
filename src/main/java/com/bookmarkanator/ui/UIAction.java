package com.bookmarkanator.ui;

public class UIAction
{
    private UIInterface item;
    private String action;

    public UIAction(UIInterface item, String action)
    {
        this.item = item;
        this.action = action;
    }

    public UIInterface getItem()
    {
        return item;
    }

    public String getAction()
    {
        return action;
    }
}
