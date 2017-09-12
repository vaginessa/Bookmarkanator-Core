package com.bookmarking.ui;

import com.bookmarking.settings.*;

/**
 * Represents a text input field
 */
public class SingleInputOption<T extends AbstractSetting> extends AbstractInputOption
{
    private T selectedValue;

    public AbstractSetting getSelectedValue()
    {
        return selectedValue;
    }

    public void setSelectedValue(T selectedValue)
    {
        this.selectedValue = selectedValue;
    }

}
