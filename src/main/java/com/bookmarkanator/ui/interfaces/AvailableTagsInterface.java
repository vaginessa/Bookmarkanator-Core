package com.bookmarkanator.ui.interfaces;

import java.util.*;

public interface AvailableTagsInterface extends UIItemInterface
{
    void setAvailableTags(Set<String> availableTags, Set<String> highlightTags, Set<String> tagsToHighlightBorders);
}
