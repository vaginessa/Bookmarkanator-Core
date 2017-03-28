package com.bookmarkanator.ui.interfaces;

import java.util.*;
import com.bookmarkanator.util.*;

public interface SelectedTagsInterface extends UIItemInterface
{
    void setSelectedTags(List<SearchOptions.TagsInfo> selectedTagGroups, Set<String> tagsToHighlight, Set<SearchOptions.TagsInfo> groupsToHighlight, Set<String> tagsToHighlightBorder);
}
