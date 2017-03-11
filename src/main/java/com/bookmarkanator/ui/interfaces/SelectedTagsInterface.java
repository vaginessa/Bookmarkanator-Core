package com.bookmarkanator.ui.interfaces;

import java.util.*;

public interface SelectedTagsInterface extends GuiItemInterface
{
    void setSelectedTags(Set<String> selectedTags);
    Set<String> getSelectedTags();
}
