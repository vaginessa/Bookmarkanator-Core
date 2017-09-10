package com.bookmarking.ui;

import java.util.*;

public class ListInputOption<T> extends AbstractInputOption
{
    private List<T> availableOptions;
    private T selectedOption;

    public T getSelectedOption()
    {
        return selectedOption;
    }

    public void setSelectedOption(T selectedOption)
    {
        this.selectedOption = selectedOption;
    }

    public List<T> getAvailableOptions()
    {
        return availableOptions;
    }

    public void setAvailableOptions(List<T> availableOptions)
    {
        this.availableOptions = availableOptions;
    }
}
