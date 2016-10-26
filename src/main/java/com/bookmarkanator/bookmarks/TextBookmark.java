package com.bookmarkanator.bookmarks;

public class TextBookmark extends AbstractBookmark <String>{

    @Override
    public String getTypeName() {
        return "text";
    }

    @Override
    public String action() {
        return getText();
    }
}
