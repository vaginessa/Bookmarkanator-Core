package com.bookmarkanator.ui.interfaces;

import java.util.*;

public interface BKTypes extends GuiItemInterface
{
    void setTypes(Set<String> types, Set<String> showTypes);
    Set<String> getVisibleTypes();
    Set<String> getAllTypes();
}
