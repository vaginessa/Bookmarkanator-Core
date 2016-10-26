package com.bookmarkanator.bookmarks;

import com.bookmarkanator.core.*;

/**
 * The text of this bookmark would represent a web address.
 */
public class WebBookmark extends AbstractBookmark <Object> {

    @Override
    public String getTypeName() {
        return "web";
    }

    @Override
    public Object action(Context context) {

        //open web address with default browser.
        return null;
    }
}
