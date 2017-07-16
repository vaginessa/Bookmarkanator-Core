package com.bookmarkanator.ui.interfaces;

import java.util.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;

public interface NewBookmarkSelectionInterface extends UIItemInterface
{
    void setTypes(Set<AbstractUIBookmark> types);

    Set<AbstractUIBookmark> getTypes();

    AbstractUIBookmark getSelectedBookmarkType();

    void show()
        throws Exception;

    void hide();
}
