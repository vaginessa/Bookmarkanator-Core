package com.bookmarkanator.core;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.util.*;

public class Context {
    private Map<UUID, TextBookmark> bookmarks;
    private TextAssociator tagsMap;
    private TextAssociator wordsMap;
    private TextAssociator partialTagsMap;
    private TextAssociator partialWordsMap;
    private Map<String, Object> contextObject;//the bookmarks can add a context object for use later. Such as in the case of unencrypting an encrypted bookmark. Kind of like an internal cookie.

    public Context() {
        bookmarks = new HashMap<>();
        tagsMap = new TextAssociator();
        wordsMap = new TextAssociator();
        partialTagsMap = new TextAssociator();
        partialWordsMap = new TextAssociator();
    }


}
