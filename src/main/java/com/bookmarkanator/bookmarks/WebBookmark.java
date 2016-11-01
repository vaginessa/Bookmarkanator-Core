package com.bookmarkanator.bookmarks;

import com.bookmarkanator.core.*;

/**
 * The text of this bookmark would represent a web address.
 */
public class WebBookmark extends AbstractBookmark{

    @Override
    public String getTypeName() {
        return "web";
    }

    @Override
    public void action(Context context) throws Exception {

    }

    @Override
    public String toXML() {
        return null;
    }

    @Override
    public void fromXML(String xml) {

    }
}
