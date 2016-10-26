package com.bookmarkanator.io;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

public interface BKIOInterface {
    void loadBookmarks();
    void saveBookmarks(List<AbstractBookmark> bookmarks);
    List<AbstractBookmark> getBookmarks();

}
