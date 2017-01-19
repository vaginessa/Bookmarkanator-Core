package com.bookmarkanator.ui.defaultui.interfaces;

import java.util.*;
import com.bookmarkanator.core.*;

public interface GUIControllerInterface
{
    void setBootstrap(Bootstrap bootstrap);
    void setSelectedTags(List<String> tags);
    void setSearchTerm(String searchTerm);
    void setShowType(String type, boolean show);

    Bootstrap getBootstrap();
    Set<String> getAvailableTags();
    Set<String> getTypes();


    List<BookmarkUIInterface> getVisibleBookmarks();


}
