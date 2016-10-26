package com.bookmarkanator.io;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

public interface BKIO {
    void loadBookmarks();
    void saveBookmarks(List<AbstractBookmark> bookmarks);
    List<AbstractBookmark> getBookmarks();

}
