package com.bookmarkanator.ui.defaultui.interfaces;

import java.util.*;

public interface BKTypesInterface extends GUIItemInterface
{
    void setTypes(Set<String> bookmarkTypes);
    Set<String> getTypes();
}
