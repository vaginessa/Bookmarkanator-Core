package com.bookmarking.ui;

import java.util.*;
import com.bookmarking.settings.*;

public class MultiListInputOption<T extends AbstractSetting>  extends AbstractInputOption
{
    private List<? extends T> availableOptions;
    private List<? extends T> selectedOptions;

    public List<? extends T> getAvailableOptions()
    {
        return availableOptions;
    }

    public void setAvailableOptions(List<T> availableOptions)
    {
        this.availableOptions = availableOptions;
    }

    public List<? extends T> getSelectedOptions()
    {
        return selectedOptions;
    }

    public void setSelectedOptions(List<? extends T> selectedOptions)
    {
        this.selectedOptions = selectedOptions;
    }
}
