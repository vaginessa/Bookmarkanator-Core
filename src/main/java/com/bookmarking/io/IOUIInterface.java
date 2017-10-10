package com.bookmarking.io;

import com.bookmarking.action.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.ui.*;

public interface IOUIInterface extends UIInterface
{
    BookmarkUIInterface getBookmarkUIInterface();
    ActionUIInterface getActionUIInterface();
}
