package com.bookmarkanator.ui.fxui.components;

import java.util.*;

public class TagComparator implements Comparator<String>
{
    @Override
    public int compare(String o1, String o2)
    {
        if (o1 == null || o1.isEmpty())
        {
            if (o2 == null || o2.isEmpty())
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
        else if (o2 == null || o2.isEmpty())
        {
            if (o1.isEmpty())
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return o1.compareToIgnoreCase(o2);
        }
    }
}
