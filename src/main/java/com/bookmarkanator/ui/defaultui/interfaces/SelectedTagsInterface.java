package com.bookmarkanator.ui.defaultui.interfaces;

import java.util.*;

public interface SelectedTagsInterface extends GUIItemInterface
{
    void setSelectedTags(Set<String> selectedTags);
    Set<String> getSelectedTags();
}
