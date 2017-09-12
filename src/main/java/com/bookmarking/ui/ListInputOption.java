package com.bookmarking.ui;

import java.util.*;
import com.bookmarking.settings.*;

public class ListInputOption<T extends AbstractSetting> extends AbstractInputOption
{
    private List<? extends T> availableOptions;
    private T selectedOption;

    public T getSelectedOption()
    {
        return selectedOption;
    }

    public void setSelectedOption(T selectedOption)
    {
        this.selectedOption = selectedOption;
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
