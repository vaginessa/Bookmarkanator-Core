package com.bookmarkanator.bookmarks;

import com.bookmarkanator.core.*;

public class EncryptedBookmark extends AbstractBookmark{
    private String passwordHash;
    private long valid;//stores how long after entering this password, until the bookmark text is encrypted again.

    /**
     * Add field to the context if this bookmark is unencrypted so that the user doesn't have to unencrypt it each time.
     */

    @Override
    public String getTypeName() {
        return "encrypted";
    }

    @Override
    public void action(FileContext context) throws Exception {

    }

    @Override
    public AbstractBookmark getNew()
    {
        return new EncryptedBookmark();
    }

    @Override
    public String toXML() {
        return null;
    }

    @Override
    public void fromXML(String xml) {

    }

}
