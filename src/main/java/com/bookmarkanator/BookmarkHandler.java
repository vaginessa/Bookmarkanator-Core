package com.bookmarkanator;

import java.util.*;

public class BookmarkHandler {
    private Map<UUID, Bookmark> bookmarks;
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
