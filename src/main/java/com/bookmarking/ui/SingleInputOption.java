package com.bookmarking.ui;

import java.util.*;
import com.bookmarking.settings.*;

/**
 * Represents a single input field, The value in the AbstractSetting would be initially empty, and
 * after the user enters data it would be sent back populated with that data.
 *
 * If available options is null then the field would be expected to receive any valid value for that type.
 * If not the input should be restricted to the available options.
 */
public class SingleInputOption<T extends AbstractSetting> extends AbstractInputOption
{
    private List<? extends T> availableOptions;
    private T selectedValue;

    public AbstractSetting getSelectedValue()
    {
        return selectedValue;
    }

    public void setSelectedValue(T selectedValue)
    {
        this.selectedValue = selectedValue;
    }

    public List<? extends T> getAvailableOptions()
    {
        return availableOptions;
    }

    public void setAvailableOptions(List<? extends T> availableOptions)
    {
        this.availableOptions = availableOptions;
    }
}
