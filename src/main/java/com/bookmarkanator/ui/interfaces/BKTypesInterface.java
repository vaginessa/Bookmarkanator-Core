package com.bookmarkanator.ui.interfaces;

import java.util.*;

public interface BKTypesInterface extends GUIItemInterface
{
    void setTypes(Set<String> bookmarkTypes);
    Set<String> getTypes();
}
