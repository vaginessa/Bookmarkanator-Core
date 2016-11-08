package com.bookmarkanator.bookmarks;

import com.bookmarkanator.core.*;

public class TextBookmark extends AbstractBookmark{

    @Override
    public String getTypeName() {
        return "Text";
    }

    @Override
    public void action(FileContext context) throws Exception {

    }

    @Override
    public String toXML() {
        return null;
    }

    @Override
    public void fromXML(String xml) {

    }
}
