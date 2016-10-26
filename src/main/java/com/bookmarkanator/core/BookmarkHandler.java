package com.bookmarkanator.core;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.util.*;

public class BookmarkHandler {
    private Map<UUID, TextBookmark> bookmarks;
    private TextAssociator tagsMap;
    private TextAssociator wordsMap;
    private TextAssociator partialTagsMap;
    private TextAssociator partialWordsMap;

    public BookmarkHandler() {
        bookmarks = new HashMap<>();
        tagsMap = new TextAssociator();
        wordsMap = new TextAssociator();
        partialTagsMap = new TextAssociator();
        partialWordsMap = new TextAssociator();
    }


}
