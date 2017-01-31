package com.bookmarkanator.ui.interfaces;

import java.util.*;

public interface BKTypesInterface extends GUIItemInterface
{
    void setTypes(Set<String> types, Set<String> showTypes);
    Set<String> getVisibleTypes();
    Set<String> getTypes();
}
