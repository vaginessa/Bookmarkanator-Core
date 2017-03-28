package com.bookmarkanator.ui.interfaces;

import com.bookmarkanator.util.*;

public interface SearchInterface extends UIItemInterface
{
    void setSearchOptions(SearchOptions searchOptions);
    void highlightSearchTerm(boolean highlight);
}
