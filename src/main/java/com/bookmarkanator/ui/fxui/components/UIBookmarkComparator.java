package com.bookmarkanator.ui.fxui.components;

import java.util.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;

public class UIBookmarkComparator implements Comparator<AbstractUIBookmark>
{
    @Override
    public int compare(AbstractUIBookmark o1, AbstractUIBookmark o2)
    {
        return o1.getBookmark().getTypeName().compareTo(o2.getBookmark().getTypeName());
    }
}
