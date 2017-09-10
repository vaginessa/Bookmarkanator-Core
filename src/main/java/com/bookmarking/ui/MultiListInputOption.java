package com.bookmarking.ui;

import java.util.*;

public class MultiListInputOption<T> extends AbstractInputOption
{
    private List<T> availableOptions;
    private List<T> selectedOptions;

    public List<T> getAvailableOptions()
    {
        return availableOptions;
    }

    public void setAvailableOptions(List<T> availableOptions)
    {
        this.availableOptions = availableOptions;
    }

    public List<T> getSelectedOptions()
    {
        return selectedOptions;
    }

    public void setSelectedOptions(List<T> selectedOptions)
    {
        this.selectedOptions = selectedOptions;
    }
}
