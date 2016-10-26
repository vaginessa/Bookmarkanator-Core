package com.bookmarkanator.bookmarks;

import com.bookmarkanator.core.*;

public class TextBookmark extends AbstractBookmark <String>{

    @Override
    public String getTypeName() {
        return "text";
    }

    @Override
    public String action(Context context) {
        return getText();
    }
}
