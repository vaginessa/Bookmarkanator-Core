package com.bookmarkanator.ui.interfaces;

import java.util.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;

public interface BKTypesInterface extends UIItemInterface
{
    void setTypes(Set<AbstractUIBookmark> types, Set<String> selectedTypes, Set<String> highlightedTypes);
}
