package com.bookmarking.ui;

/**
 * Represents a text input field
 */
public class SingleInputOption<T> extends AbstractInputOption
{
    private T selectedValue;

    public T getSelectedValue()
    {
        return selectedValue;
    }

    public void setSelectedValue(T selectedValue)
    {
        this.selectedValue = selectedValue;
    }

}
