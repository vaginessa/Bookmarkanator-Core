package com.bookmarkanator.ui.interfaces;

import java.util.*;

public interface AvailableTagsInterface extends GUIItemInterface
{
    void setAvailableTags(Set<String> availableTags);
    Set getAvailableTags();
}
